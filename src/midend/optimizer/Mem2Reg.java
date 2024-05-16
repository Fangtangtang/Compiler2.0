package midend.optimizer;

import ir.BasicBlock;
import ir.entity.Storage;
import ir.entity.constant.ConstInt;
import ir.entity.var.*;
import ir.function.Function;
import ir.irType.IRType;
import ir.stmt.*;
import ir.stmt.instruction.*;
import utility.Pair;
import utility.dominance.*;

import java.util.*;

/**
 * @author F
 * llvm ir的localVar通过alloca的方式，每次使用的时候做load和store
 * 将store看成破坏了ssa的def，load看成use
 * mem2reg消去alloca并保持ssa特性
 * todo：暂时认为很早很早以前写的 dom tree 相关的没问题
 */
public class Mem2Reg {

    Function function;
    ConstInt zero = new ConstInt("0");
    HashMap<String, Stack<Storage>> allocaDefMap = null;
    HashMap<LocalTmpVar, Storage> loads = null;
    HashSet<String> visited = null;

    /**
     * 以函数为单位做操作
     * - collectAlloca：不额外做，所有LocalVar类型的
     * - insertDomPhi
     * - rename
     *
     * @param func local function
     */
    public void execute(Function func) {
        function = func;
        allocaDefMap = new HashMap<>();
        loads = new HashMap<>();
        visited = new HashSet<>();
        insertDomPhi();
        rename();
        updateStmts();
    }

    /**
     * 遍历一遍
     * 在def所在block的DF block中插入对应变量的phi（声明又有phi需求）
     * phi也为def，直接创建变量（localTmpVar，使用bb名作为后缀保证唯一性）
     * + 工作表，更新至收敛
     */
    void insertDomPhi() {
        HashSet<DomTreeNode> workList = new HashSet<>(function.reorderedBlock);
        while (!workList.isEmpty()) {
            DomTreeNode node = workList.iterator().next();
            workList.remove(node);
            ArrayList<Pair<String, IRType>> defList = new ArrayList<>();
            for (Map.Entry<String, DomPhi> entry : node.block.domPhiMap.entrySet()) {
                defList.add(new Pair<>(entry.getKey(), entry.getValue().result.type));
            }
            for (Stmt stmt : node.block.statements) {
                // alloca def
                if (stmt instanceof Store store &&
                        store.pointer instanceof LocalVar localVar) {
                    String localVarName = localVar.identity;
                    if (!allocaDefMap.containsKey(localVarName)) {
                        allocaDefMap.put(localVarName, new Stack<>());
                    }
                    defList.add(new Pair<>(localVarName, localVar.storage.type));
                }
            }
            for (Pair<String, IRType> pair : defList) {
                for (DomTreeNode dfNode : node.domFrontier) {
                    BasicBlock dfBlock = dfNode.block;
                    String varName = pair.getFirst() + "_" + dfBlock.label;
                    if (!dfBlock.domPhiMap.containsKey(varName)) {
                        // insert new phi (new def added to dfBlock)
                        DomPhi domPhi = new DomPhi(new LocalTmpVar(pair.getSecond(), varName));
                        dfBlock.domPhiMap.put(pair.getFirst(), domPhi);
                        workList.add(dfNode);
                    }
                }
            }
        }
    }

    /**
     * 将 def(store) 和 use(load) 重命名
     * 向每句 phi 中插入定值和label
     * 从entry开始的dfs，遍历完所有的控制流边
     * - newDef：
     * |    + localTmpVar直接复用
     * |    + 新的 phiDef
     */
    void rename() {
        renameDfs(function.entry);
    }

    /**
     * dfs on CFG
     * - rename name in use(若visited，不用再做)
     * - insert phi [value,label]
     *
     * @param block node in CFG
     */
    void renameDfs(BasicBlock block) {
        HashMap<String, Storage> allocaDefInBlock = new HashMap<>();
        HashSet<String> newDef = new HashSet<>();
        for (Map.Entry<String, Stack<Storage>> entry : allocaDefMap.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                allocaDefInBlock.put(entry.getKey(), entry.getValue().peek());
            }
        }
        for (Map.Entry<String, DomPhi> entry : block.domPhiMap.entrySet()) {
            allocaDefInBlock.put(entry.getKey(), entry.getValue().result);
            newDef.add(entry.getKey());
        }
        // rename
        LinkedList<Stmt> newStatements = new LinkedList<>();
        for (Stmt stmt : block.statements) {
            // use of localVar
            if (stmt instanceof Load load &&
                    load.pointer instanceof LocalVar localVar) {
                Storage replace = allocaDefInBlock.get(localVar.identity);
                while (replace instanceof LocalTmpVar var &&
                        loads.containsKey(var)) {
                    replace = loads.get(var);
                }
                loads.put(load.result, replace);
            }
            // def of localVar
            else if (stmt instanceof Store store &&
                    store.pointer instanceof LocalVar localVar) {
                allocaDefInBlock.put(localVar.identity, (Storage) store.value);
                newDef.add(localVar.identity);
            } else if (!(stmt instanceof Alloca)) {
                newStatements.add(stmt);
            }
        }
        block.statements = newStatements;
        // update allocaDefMap: push
        for (String newVar : newDef) {
            allocaDefMap.get(newVar).push(allocaDefInBlock.get(newVar));
        }
        for (BasicBlock successor : block.successorList) {
            // insert phi
            for (Map.Entry<String, DomPhi> entry : successor.domPhiMap.entrySet()) {
                entry.getValue().put(block.label, allocaDefInBlock.get(entry.getKey()));
            }
            // dfs
            if (!visited.contains(successor.label)) {
                visited.add(successor.label);
                renameDfs(successor);
            }
        }
        // update allocaDefMap: pop
        for (String newVar : newDef) {
            allocaDefMap.get(newVar).pop();
        }
    }

    /**
     * add DomPhi it statements
     */
    void updateStmts() {
        for (Map.Entry<String, BasicBlock> entry : function.blockMap.entrySet()) {
            BasicBlock block = entry.getValue();
            for (Map.Entry<String, DomPhi> phiEntry : block.domPhiMap.entrySet()) {
                block.statements.addFirst(phiEntry.getValue());
            }
            for (Stmt stmt : block.statements) {
                stmt.replaceUse(loads, null);
            }
            block.tailStmt.replaceUse(loads, null);
        }
        for (Map.Entry<String, DomPhi> phiEntry : function.ret.domPhiMap.entrySet()) {
            function.ret.statements.addFirst(phiEntry.getValue());
        }
        for (Stmt stmt : function.ret.statements) {
            stmt.replaceUse(loads, null);
        }
        function.ret.tailStmt.replaceUse(loads, null);
    }

}
