package midend.optimizer;

import ir.BasicBlock;
import ir.entity.*;
import ir.entity.constant.*;
import ir.entity.var.GlobalVar;
import ir.entity.var.LocalTmpVar;
import ir.entity.var.Ptr;
import ir.function.Function;
import ir.irType.VoidType;
import ir.stmt.Stmt;
import ir.stmt.instruction.*;
import ir.stmt.terminal.*;
import utility.Counter;
import utility.live.GlobalLiveRange;
import utility.Pair;
import utility.dominance.DomTree;
import utility.dominance.DomTreeNode;
import utility.error.InternalException;

import java.util.*;

/**
 * @author F
 * promote memory to register
 * 1. 没有被使用的 alloca 可以直接去掉；
 * 2. TODO:如果只被定义了一次，那么所有的使用都可以用定义的值代替；
 * 3. TODO:如果一个 alloca 的定义使用只出现在一个块中，那么每个 use 可以被最近的 def 替换；
 * 4. 如果一个 alloca 只被 load 和 store，那么可以通过在支配边界插入 phi 指令，并将所有的 use 替换为对
 * 应的 phi 指令的结果。
 * <p>
 * TODO:更多操作
 */
public class Mem2Reg {
    Function function;
    //在多于一个BB中被use的变量名 -> 变量
    HashMap<String, Entity> globalName;
    //变量名 -> 所有def该变量的BB
    HashMap<String, HashSet<String>> blocksSets;
    //blockLabel -> 所有def在block的变量名
    HashMap<String, HashSet<String>> defInBlockSets;
    //未被使用的alloca
    HashSet<String> unusedAlloca;

    public void execute(Function function) {
        this.function = function;
        setUnusedAlloca(function.entry);
        findGlobalNames();
        removeUnusedAlloca(function.entry);
        insertPhiFunction(function.domTree);
        rename();
//        TODO：处理关键边?
//        breakCriticalEdge(function);
    }

    String getVarName(Entity entity) {
        //非变量
        if (entity instanceof Constant || entity instanceof GlobalVar) {
            return null;
        }
        if (entity instanceof Ptr ptr) {
            return ptr.toString();
        }
        if (entity instanceof LocalTmpVar tmpVar) {
            if (tmpVar.type instanceof VoidType) {
                return null;
            }
            return tmpVar.toString();
        }
        throw new InternalException("invalid entity");
    }

    void setUnusedAlloca(BasicBlock entry) {
        unusedAlloca = new HashSet<>();
        Stmt stmt;
        for (int i = 0; i < entry.statements.size(); ++i) {
            stmt = entry.statements.get(i);
            if (stmt instanceof Alloca alloca) {
                unusedAlloca.add(getVarName(alloca.result));
            }
        }
    }

    void removeUnusedAlloca(BasicBlock entry) {
        Stmt stmt;
        for (int i = 0; i < entry.statements.size(); ++i) {
            stmt = entry.statements.get(i);
            if (stmt instanceof Alloca alloca) {
                if (unusedAlloca.contains(getVarName(alloca.result))) {
                    entry.statements.remove(stmt);
                }
            }
        }
    }

    void addVarDefInBlock(String varName, String blockLabel) {
        if (blocksSets.containsKey(varName)) {
            blocksSets.get(varName).add(blockLabel);
            return;
        }
        HashSet<String> set = new HashSet<>();
        set.add(blockLabel);
        blocksSets.put(varName, set);
    }

    void findGlobalNames() {
        LinkedHashMap<String, BasicBlock> blockMap = function.blockMap;
        globalName = new HashMap<>();
        blocksSets = new HashMap<>();
        defInBlockSets = new HashMap<>();
        Iterator<Map.Entry<String, BasicBlock>> iter = blockMap.entrySet().iterator();
        Map.Entry<String, BasicBlock> pair;
        boolean flag = true;
        while (iter.hasNext()) {
            pair = iter.next();
            HashSet<String> defInBlock = new HashSet<>();//在BB中被定义的
            BasicBlock block = pair.getValue();
            defInBlockSets.put(block.label, defInBlock);
            //入参作为在第一个BB中的def
            if (flag) {
                flag = false;
                for (Entity entity : function.parameterList) {
                    String name = getVarName(entity);
//                    unusedAlloca.remove(name);
                    defInBlock.add(name);
                    addVarDefInBlock(name, block.label);
                }
            }
            Stmt stmt;
            Entity varDef;
            ArrayList<Entity> varUse;
            //for each stmt
            for (int i = 0; i < block.statements.size(); ++i) {
                stmt = block.statements.get(i);
                varDef = stmt.getDef();
                varUse = stmt.getUse();
                //def
                if (varDef != null) {
                    String name = getVarName(varDef);
                    if (name != null) {
                        unusedAlloca.remove(name);
                        defInBlock.add(name);
                        addVarDefInBlock(name, block.label);
                    }
                }
                //use
                if (varUse != null) {
                    for (Entity entity : varUse) {
                        String name = getVarName(entity);
                        if (name != null) {
                            unusedAlloca.remove(name);
                            if (!defInBlock.contains(name)) {
                                globalName.put(name, entity);
                            }
                        }
                    }
                }
            }
        }
    }

    void insertPhiFunction(DomTree domTree) {
        HashSet<String> workList;
        HashSet<String> defInBlock;
        DomTreeNode node, dfNode;
        Iterator<String> iterator;
        for (Map.Entry<String, Entity> var : globalName.entrySet()) {
            String name = var.getKey();
            workList = blocksSets.get(name);
            while (!workList.isEmpty()) {
                //删除结点n
                iterator = workList.iterator();
                String label = iterator.next();
                iterator.remove();
                node = domTree.label2node.get(label);
                defInBlock = defInBlockSets.get(label);
                //遍历DF[n]
                for (int i = 0; i < node.domFrontier.size(); i++) {
                    dfNode = node.domFrontier.get(i);
                    if (!dfNode.phiFuncDef.containsKey(name)) {
                        dfNode.phiFuncDef.put(name, var.getValue());
                        int predecessorSize = dfNode.block.predecessorList.size();
                        dfNode.block.phiMap.put(name,
                                new Pair<>(null,
                                        new Pair<>(new String[predecessorSize], new SSAEntity[predecessorSize])
                                ));
                        if (!defInBlock.contains(name)) {
                            workList.add(label);
                        }
                    }
                }
            }
        }
    }

    //记录不同变量名的重命名数
    HashMap<String, Counter> counterMap = new HashMap<>();

    //记录不同变量名的当前重命名
    HashMap<String, Stack<SSAEntity>> renameStack = new HashMap<>();
    HashMap<String, SSAEntity> ssaMap = new HashMap<>();

    //def
    Pair<String, SSAEntity> toSsaEntity(Entity var) {
        String varName = getVarName(var);
        if (varName == null) {
            return new Pair<>(null, new SSAEntity(var));
        }
        if (!counterMap.containsKey(varName)) {
            if (ssaMap.containsKey(varName)) {
                return new Pair<>(varName, ssaMap.get(varName));
//                throw new InternalException("multi defined");
            }
            SSAEntity ssa = new SSAEntity(var, new GlobalLiveRange(varName));
            ssaMap.put(varName, ssa);
            return new Pair<>(varName, ssa);
        }
        Counter counter = counterMap.get(varName);
        Stack<SSAEntity> stack = renameStack.get(varName);
        ++counter.cnt;
        String rename = varName + "." + counter.cnt;
        SSAEntity ssaEntity = new SSAEntity(
                var,
                new GlobalLiveRange(rename)
        );
        stack.push(ssaEntity);
        return new Pair<>(varName, ssaEntity);
    }

    //use
    SSAEntity getSsaEntity(Entity var) {
        String varName = getVarName(var);
        if (varName == null) {
            return new SSAEntity(var);
        }
        if (renameStack.containsKey(varName)) {
            return renameStack.get(varName).peek();
        }
        if (ssaMap.containsKey(varName)) {
            return ssaMap.get(varName);
        }
        throw new InternalException("ssa not found");
    }

    HashMap<String, DomTreeNode> label2node;

    void rename() {
        DomTree domTree = function.domTree;
        //initialize
        for (Map.Entry<String, Entity> var : globalName.entrySet()) {
            String name = var.getKey();
            counterMap.put(name, new Counter());
            renameStack.put(name, new Stack<>());
        }
        label2node = domTree.label2node;
        //入参rename
        ArrayList<SSAEntity> ssaEntityList = new ArrayList<>();
        for (Entity entity : function.parameterList) {
            Pair<String, SSAEntity> pair = toSsaEntity(entity);
            ssaEntityList.add(pair.getSecond());
        }
        function.ssaParameterList = ssaEntityList;
        //recursive
        recursiveRename(domTree.reorderedBlock.get(0));
        //phi

    }

    void renameOnStmt(Stmt stmt, HashMap<String, SSAEntity> defInBlock) {
        Entity varDef = stmt.getDef();
        ArrayList<Entity> varUse = stmt.getUse();
        //def
        if (varDef != null) {
            Pair<String, SSAEntity> pair = toSsaEntity(varDef);
            if (pair.getFirst() != null) {
                defInBlock.put(pair.getFirst(), pair.getSecond());
            }
            stmt.setDef(pair.getSecond());
        }
        //use: top(stack[var])
        if (varUse != null) {
            ArrayList<SSAEntity> ssaEntityList = new ArrayList<>();
            for (Entity entity : varUse) {
                ssaEntityList.add(getSsaEntity(entity));
            }
            stmt.setUse(ssaEntityList);
        }
        if (stmt instanceof Branch branch && branch.result != null) {
            ((Branch) stmt).ssaResult = getSsaEntity(branch.result);
        } else if (stmt instanceof Jump jump && jump.result != null) {
            ((Jump) stmt).ssaResult = getSsaEntity(jump.result);
        } else if (stmt instanceof Phi phi) {
            function.ssaPhiResult.put(phi.phiLabel, phi.ssaResult);
        } else if (stmt instanceof Call call) {
            call.ssaParameterList = new ArrayList<>();
            for (var param : call.parameterList) {
                call.ssaParameterList.add(getSsaEntity(param));
            }
        } else if (stmt instanceof Alloca alloca && "retVal".equals(alloca.result.identity)) {
            function.ssaRetVal = alloca.ssaResult;
        }
    }

    void addParamToPhi(String name, SSAEntity ssaVar,
                       DomTreeNode node,
                       BasicBlock successor,
                       int idx) {
        if (successor.phiMap.containsKey(name)) {
            Pair<String[], SSAEntity[]> phiList = successor.phiMap.get(name).getSecond();
            phiList.getFirst()[idx] = node.block.label;
            phiList.getSecond()[idx] = ssaVar;
        }
    }

    void recursiveRename(DomTreeNode node) {
        //origen name -> ssaEntity
        HashMap<String, SSAEntity> defInBlock = new HashMap<>();
        //rename variable in phi
//        int predecessorSize = node.block.predecessorList.size();
        for (Map.Entry<String, Entity> phiVar : node.phiFuncDef.entrySet()) {
            //TODO:确保不出现null？
            Pair<String, SSAEntity> pair = toSsaEntity(phiVar.getValue());
            defInBlock.put(pair.getFirst(), pair.getSecond());
            node.block.phiMap.get(phiVar.getKey()).setFirst(pair.getSecond());
        }
        //rename entity in stmt
        for (int i = 0; i < node.block.statements.size(); ++i) {
            renameOnStmt(node.block.statements.get(i), defInBlock);
        }
        renameOnStmt(node.block.tailStmt, defInBlock);
        //rename param in successors
        BasicBlock successor;
        Pair<String[], SSAEntity[]> phiList;
        for (int i = 0; i < node.block.successorList.size(); i++) {
            successor = node.block.successorList.get(i);
            //def&phi in block
            for (Map.Entry<String, SSAEntity> defVar : defInBlock.entrySet()) {
                addParamToPhi(defVar.getKey(), defVar.getValue(),
                        node,
                        successor,
                        successor.predecessorList.indexOf(node.block)
                );
            }
        }
        //recurse on dom tree
        node.successors.forEach(
                this::recursiveRename
        );
        //maintain def rename
        for (Map.Entry<String, SSAEntity> defVar : defInBlock.entrySet()) {
            if (renameStack.containsKey(defVar.getKey())) {
                renameStack.get(defVar.getKey()).pop();
            }
        }
    }

    /**
     * 在特殊critical edge插入结点
     * - 带phi结点的前驱
     * - 前驱有多个后继
     *
     * @param function 函数的CFG
     */
    void breakCriticalEdge(Function function) {
        for (Map.Entry<String, BasicBlock> entry : function.blockMap.entrySet()) {
            BasicBlock block = entry.getValue();
            BasicBlock predecessor;
            if (block.phiMap.size() > 0) {
                for (int i = 0; i < block.predecessorList.size(); i++) {
                    predecessor = block.predecessorList.get(i);
                    if (predecessor.successorList.size() > 1) {
                        addBasicBlock(predecessor, block);
                    }
                }
            }
        }
    }

    void addBasicBlock(BasicBlock source, BasicBlock destination) {
        BasicBlock add = new BasicBlock(source.label + "_to_" + destination.label);
        add.tailStmt = new Jump(destination);
        //source
        if (source.tailStmt instanceof Jump jumpStmt) {
            jumpStmt.target = add;
            source.successorList.set(0, add);
        } else if (source.tailStmt instanceof Branch branchStmt) {
            if (branchStmt.falseBranch.label.equals(destination.label)) {
                branchStmt.falseBranch = destination;
                source.successorList.set(1, add);
            } else {
                branchStmt.trueBranch = destination;
                source.successorList.set(0, add);
            }
        } else {
            throw new InternalException("unexpected predecessor");
        }
        //destination
        for (int i = 0; i < destination.predecessorList.size(); i++) {
            if (destination.predecessorList.get(i).label.equals(source.label)) {
                destination.predecessorList.set(i, add);
            }
        }
    }

//    /**
//     * 所有的basic block消除phi
//     * 向前驱加入规划次序的mv指令
//     *
//     * @param function CFG
//     */
//    void eliminatePhi(Function function) {
//        for (Map.Entry<String, BasicBlock> entry : function.blockMap.entrySet()) {
//            BasicBlock block = entry.getValue();
//            if (block.phiMap.size() > 0) {
//                for (int i = 0; i < block.predecessorList.size(); i++) {
//                    block.predecessorList.get(i).mvInst = reorderPhi(i, block.phiMap);
//                }
//            }
//        }
//    }
//
//    /**
//     * 规划顺序，模拟”并行“
//     *
//     * @param phiMap mem2reg生成的phi
//     * @return mvInst
//     */
//    ArrayList<Pair<String, String>> reorderPhi(int index,
//                                               HashMap<String, Pair<String, Pair<String[], Entity[]>>> phiMap) {
//        ArrayList<Pair<String, String>> mvInst = new ArrayList<>();
//        //<des , <cnt（用到des的） , src>>
//        LinkedHashMap<String, Pair<Counter, String>> phi = new LinkedHashMap<>();
//        for (Map.Entry<String, Pair<String, Pair<String[], Entity[]>>> entry : phiMap.entrySet()) {
//            Pair<String, Pair<String[], Entity[]>> pair = entry.getValue();
//            Pair<Counter, String> cntSrc = new Pair<>(new Counter(0), pair.getSecond().getSecond()[index]);
//            phi.put(pair.getSecond().getFirst()[index], cntSrc);
//        }
//        for (Map.Entry<String, Pair<Counter, String>> entry : phi.entrySet()) {
//            String src = entry.getValue().getSecond();
//            if (phi.containsKey(src)) {
//                ++phi.get(src).getFirst().cnt;
//            }
//        }
//        //循环至所有phi都被转化
//        while (!phi.isEmpty()) {
//            ArrayList<String> added = new ArrayList<>();
//            for (Map.Entry<String, Pair<Counter, String>> entry : phi.entrySet()) {
//                String des = entry.getKey();
//                Pair<Counter, String> pair = entry.getValue();
//                if (pair.getFirst().cnt == 0) {
//                    added.add(des);
//                    mvInst.add(new Pair<>(des, pair.getSecond()));
//                    --phi.get(pair.getSecond()).getFirst().cnt;
//                }
//            }
//            for (String s : added) {
//                phi.remove(s);
//            }
//        }
//        return mvInst;
//    }
}
