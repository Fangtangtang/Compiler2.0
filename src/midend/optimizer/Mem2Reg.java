package midend.optimizer;

import ir.BasicBlock;
import ir.entity.Storage;
import ir.entity.constant.ConstInt;
import ir.entity.constant.Null;
import ir.entity.var.*;
import ir.function.Function;
import ir.irType.IRType;
import ir.stmt.*;
import ir.stmt.instruction.*;
import utility.Pair;
import utility.dominance.*;
import utility.error.InternalException;

import java.util.*;

/**
 * @author F
 * llvm ir的localVar通过alloca的方式，每次使用的时候做load和store
 * 将store看成破坏了ssa的def，load看成use
 * mem2reg消去alloca并保持ssa特性
 */
public class Mem2Reg {

    Function function;
    ConstInt zero = new ConstInt("0");
    HashMap<String, Stack<Storage>> allocaDefMap = null;
    HashMap<String, Storage> loads = null;
    // BlockName -> < LocalVarNAme , newLiveOutDef >
    HashMap<String, HashMap<String, Storage>> newDefInBlock = null;
    HashSet<String> visited = null;
    Storage undefinedVar = new Null();

    /**
     * 以函数为单位做操作
     * - collectAlloca：不额外做，所有LocalVar类型的
     * - insertDomPhi
     * |    + store: localVar的真赋值
     * |    + alloca: localVar的null赋值
     * - rename
     *
     * @param func local function
     */
    public void execute(Function func) {
        function = func;
        allocaDefMap = new HashMap<>();
        loads = new HashMap<>();
        newDefInBlock = new HashMap<>();
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
            // phi
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
                    if (!dfBlock.domPhiMap.containsKey(pair.getFirst())) {
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
        for (Map.Entry<String, BasicBlock> entry : function.blockMap.entrySet()) {
            visited = new HashSet<>();
            BasicBlock block = entry.getValue();
            renameDfs(block);
        }
    }

    /**
     * dfs on CFG
     * - rename name in use(若visited，不用再做)
     * - insert phi [value,label]
     * [!] dfs from each node instead of the root of the domTree
     *
     * @param block node in CFG
     */
    void renameDfs(BasicBlock block) {
        // in the entry of the block, var should be renamed to what?
        HashMap<String, Storage> allocaDefInBlock = new HashMap<>();
        // new def in current block
        HashSet<String> newDef = new HashSet<>();
        // def in block (name -> latest value)
        for (Map.Entry<String, Stack<Storage>> entry : allocaDefMap.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                allocaDefInBlock.put(entry.getKey(), entry.getValue().peek());
            }
        }
        for (Map.Entry<String, DomPhi> entry : block.domPhiMap.entrySet()) {
            allocaDefInBlock.put(entry.getKey(), entry.getValue().result);
            newDef.add(entry.getKey());
        }
        // replace statements
        // -------------------------------------------------------------------------------------------------------------
        LinkedList<Stmt> newStatements = new LinkedList<>();
        for (Stmt stmt : block.statements) {
            // use of localVar：不再需要load语句，将load的result的使用替换为replace的使用
            if (stmt instanceof Load load &&
                    load.pointer instanceof LocalVar localVar) {
                Storage replace = allocaDefInBlock.get(localVar.identity);
                while (replace instanceof LocalTmpVar var &&
                        loads.containsKey(var.toString())) {
                    replace = loads.get(var.toString());
                }
                loads.put(load.result.toString(), replace);
            }
            // def of localVar (replace def statements)：向alloca地址存的def语句不再需要
            else if (stmt instanceof Store store &&
                    store.pointer instanceof LocalVar localVar) {
                allocaDefInBlock.put(localVar.identity, (Storage) store.value);
                newDef.add(localVar.identity);
            }
            // Alloca不再需要
            else if (stmt instanceof Alloca alloca) {
                allocaDefInBlock.put(alloca.result.identity, undefinedVar);
                newDef.add(alloca.result.identity);
            } else {
                newStatements.add(stmt);
            }
        }
        block.statements = newStatements;
        // -------------------------------------------------------------------------------------------------------------
        // update allocaDefMap: push
        HashMap<String, Storage> map;
        if (!newDefInBlock.containsKey(block.label)) {
            map = new HashMap<>();
            for (String newVar : newDef) {
                // only care about used localVar
                if (allocaDefMap.containsKey(newVar)) {
                    allocaDefMap.get(newVar).push(allocaDefInBlock.get(newVar));
                    map.put(newVar, allocaDefInBlock.get(newVar));
                }
            }
            newDefInBlock.put(block.label, map);
        } else {
            map = newDefInBlock.get(block.label);
            for (Map.Entry<String, Storage> defEntry : map.entrySet()) {
                newDef.add(defEntry.getKey());
                allocaDefInBlock.put(defEntry.getKey(), defEntry.getValue());
                allocaDefMap.get(defEntry.getKey()).push(defEntry.getValue());
            }
        }
        // 向后继插入phi过去的值
        for (BasicBlock successor : block.successorList) {
            for (Map.Entry<String, DomPhi> entry : successor.domPhiMap.entrySet()) {
                if (allocaDefInBlock.containsKey(entry.getKey())) {
                    entry.getValue().put(
                            block.label,
                            allocaDefInBlock.get(entry.getKey())
                    );
                } else if (allocaDefMap.containsKey(entry.getKey()) &&
                        !allocaDefMap.get(entry.getKey()).isEmpty()) {
                    entry.getValue().put(
                            block.label,
                            allocaDefMap.get(entry.getKey()).peek()
                    );
                } else {
                    entry.getValue().put(
                            block.label,
                            undefinedVar
                    );
                }
            }
            // dfs(跨BB的phi值)
            if (!visited.contains(successor.label)) {
                visited.add(successor.label);
                renameDfs(successor);
            }
        }
        // update allocaDefMap: pop
        if (!newDefInBlock.containsKey(block.label)) {
            throw new InternalException("unexpected block rename visit!");
        } else {
            for (String newVar : newDef) {
                if (allocaDefMap.containsKey(newVar)) {
                    allocaDefMap.get(newVar).pop();
                }
            }
        }

    }

    /**
     * add DomPhi to statements
     */
    void updateStmts() {
        for (Map.Entry<String, BasicBlock> entry : function.blockMap.entrySet()) {
            BasicBlock block = entry.getValue();
            for (Map.Entry<String, DomPhi> phiEntry : block.domPhiMap.entrySet()) {
                block.statements.addFirst(phiEntry.getValue());
            }
            for (Stmt stmt : block.statements) {
                stmt.replaceUse(loads);
            }
            block.tailStmt.replaceUse(loads);
        }
        for (Map.Entry<String, DomPhi> phiEntry : function.ret.domPhiMap.entrySet()) {
            function.ret.statements.addFirst(phiEntry.getValue());
        }
        for (Stmt stmt : function.ret.statements) {
            stmt.replaceUse(loads);
        }
        function.ret.tailStmt.replaceUse(loads);
    }

}
