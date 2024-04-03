package midend.optimizer;

import ir.*;
import ir.entity.*;
import ir.entity.var.*;
import ir.function.*;
import ir.stmt.*;
import ir.stmt.instruction.*;
import ir.stmt.terminal.*;
import utility.Pair;
import utility.error.InternalException;

import java.util.*;

/**
 * @author F
 * 函数内联
 * - 暂不处理递归函数
 * - 函数调用关系成图，尝试将tail全缩掉？
 * TODO: 选择哪些做 inlining
 */
public class FunctionInlining {
    IRRoot irRoot;

    //记录局部变量在target中的alloca
    HashMap<LocalVar, LocalVar> curAllocaMap = null;

    LinkedList<Stmt> newAllocaStmt = null;

    HashMap<String, BasicBlock> newBlock = null;
    ArrayList<TerminalStmt> terminalStmts = null;

    //BB重命名
    HashMap<String, String> renameMap = null;

    public FunctionInlining(IRRoot root) {
        this.irRoot = root;
    }

    public void execute() {
        analysisCalling();
        int pass = 10;
        while (pass > 0) {
            if (inliningPass()) {
                --pass;
            } else {
                break;
            }
        }
        removeUnusedFunction();
    }

    /**
     * 分析（自定义）函数调用关系
     */
    public void analysisCalling() {
        for (Map.Entry<String, Function> funcEntry : irRoot.funcDef.entrySet()) {
            Function func = funcEntry.getValue();
            //普通local function
            if (func.entry != null) {
                func.calleeMap = new HashMap<>();
                for (Map.Entry<String, BasicBlock> bbEntry : func.blockMap.entrySet()) {
                    BasicBlock block = bbEntry.getValue();
                    for (Stmt stmt : block.statements) {
                        // call self-defined function
                        if (stmt instanceof Call callStmt &&
                                callStmt.function.entry != null) {
                            if (func.calleeMap.containsKey(callStmt.function)) {
                                func.calleeMap.put(
                                        callStmt.function,
                                        func.calleeMap.get(callStmt.function) + 1
                                );
                            } else {
                                func.calleeMap.put(callStmt.function, 1);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 执行一轮inlining
     *
     * @return true if updated
     */
    public boolean inliningPass() {
        boolean flag = false;
        for (Map.Entry<String, Function> funcEntry : irRoot.funcDef.entrySet()) {
            Function func = funcEntry.getValue();
            curAllocaMap = new HashMap<>();
            newAllocaStmt = new LinkedList<>();
            newBlock = new HashMap<>();
            terminalStmts = new ArrayList<>();
            renameMap = new HashMap<>();
            ArrayList<Phi> phis = new ArrayList<>();
            //普通local function
            if (func.entry != null) {
                for (Map.Entry<String, BasicBlock> bbEntry : func.blockMap.entrySet()) {
                    BasicBlock block = bbEntry.getValue();
                    ListIterator<Stmt> stmtIterator = block.statements.listIterator();
                    while (stmtIterator.hasNext()) {
                        Stmt stmt = stmtIterator.next();
                        if (stmt instanceof Call callStmt &&
                                callStmt.function.calleeMap != null &&
                                callStmt.function.calleeMap.isEmpty()) {
                            flag = true;
                            int num = func.calleeMap.get(callStmt.function);
                            inlineToFunc(callStmt.function, func,
                                    block,
                                    callStmt,
                                    stmtIterator,
                                    num
                            );
                            if (num == 1) {
                                func.calleeMap.remove(callStmt.function);
                            } else {
                                func.calleeMap.put(callStmt.function, num - 1);
                            }
                            break;
                        } else if (stmt instanceof Phi phi) {
                            phis.add(phi);
                        }
                    }
                }
                func.entry.statements.addAll(0, newAllocaStmt);
                func.blockMap.putAll(newBlock);
                targetRedirect(func);
                for (Phi phi : phis) {
                    for (int i = 0; i < phi.label1.size(); i++) {
                        if (renameMap.containsKey(phi.label1.get(i))) {
                            phi.label1.set(i, renameMap.get(phi.label1.get(i)));
                        }
                    }
                    for (int i = 0; i < phi.label2.size(); i++) {
                        if (renameMap.containsKey(phi.label2.get(i))) {
                            phi.label2.set(i, renameMap.get(phi.label2.get(i)));
                        }
                    }
                }
            }
            curAllocaMap = null;
            newAllocaStmt = null;
            newBlock = null;
            renameMap = null;
        }
        return flag;
    }

    /**
     * case1：已经被inline到该函数中过
     * case2：从未被inline到该函数中
     *
     * @param src          callee
     * @param tar          caller
     * @param callingBlock 发起call的block
     * @param stmtIterator 指向call后的位置的迭代器
     * @param num          在函数中调用次数的标记
     */
    void inlineToFunc(Function src, Function tar,
                      BasicBlock callingBlock,
                      Call call,
                      ListIterator<Stmt> stmtIterator,
                      int num) {
        //entry特殊重命名
        renameMap.put(src.entry.label + "_" + tar.funcName + num, callingBlock.label);
        //当前在处理的tar中block
        BasicBlock curBlock = new BasicBlock(callingBlock.label);
        ArrayList<BasicBlock> blocks = new ArrayList<>();
        blocks.add(curBlock);
        newBlock.put(curBlock.label, curBlock);
        if (callingBlock == tar.entry) {
            tar.entry = curBlock;
        }
        //call前的不变
        curBlock.statements.addAll(callingBlock.statements.subList(0, stmtIterator.previousIndex()));
        ListIterator<Stmt> iterInCurBlock = curBlock.statements.listIterator(
                curBlock.statements.size()
        );
        //内联的localTmpVar需要创建副本
        HashMap<LocalTmpVar, Storage> copyMap = new HashMap<>();
        //函数入参
        for (int i = 0; i < src.parameterList.size(); i++) {
            LocalTmpVar param = src.parameterList.get(i);
            copyMap.put(param, call.parameterList.get(i));
        }
        boolean isFirst = true;
        //可能需要bb重定向
        for (Map.Entry<String, BasicBlock> bbEntry : src.blockMap.entrySet()) {
            BasicBlock srcBlock = bbEntry.getValue();
            ListIterator<Stmt> iterator = srcBlock.statements.listIterator();
            //非src的第一个BB
            if (!isFirst) {
                curBlock = new BasicBlock(srcBlock.label + "_" + tar.funcName + num);
                newBlock.put(curBlock.label, curBlock);
                blocks.add(curBlock);
                iterInCurBlock = curBlock.statements.listIterator();
            } else {
                isFirst = false;
            }
            //对BB中的每个语句处理
            while (iterator.hasNext()) {
                Stmt stmt = iterator.next();
                if (stmt instanceof Alloca alloca) {
                    //第一次inline到该函数中
                    if (!curAllocaMap.containsKey(alloca.result)) {
                        Alloca allocaStmt;
                        if (tar.addedAlloca.containsKey(alloca.result)) {
                            allocaStmt = tar.addedAlloca.get(alloca.result);
                        } else {
                            allocaStmt = new Alloca(
                                    alloca.result.type,
                                    src.funcName + "_" + tar.funcName + "_" + alloca.result.identity
                            );
                            tar.addedAlloca.put(alloca.result, allocaStmt);
                            newAllocaStmt.add(allocaStmt);
                        }
                        //转移localVar
                        curAllocaMap.put(alloca.result, allocaStmt.result);
                    }
                } else {
                    ArrayList<Entity> use = stmt.getUse();
                    ArrayList<Entity> newUse = new ArrayList<>();
                    //replace
                    if (use != null) {
                        for (Entity element : use) {
                            if (element instanceof LocalVar localVar) {
                                newUse.add(curAllocaMap.get(localVar));
                            } else if (element instanceof LocalTmpVar localTmpVar) {
                                newUse.add(copyMap.get(localTmpVar));
                            } else {
                                newUse.add(element);
                            }
                        }
                    }
                    Pair<Stmt, LocalTmpVar> stmtCopy = stmt.creatCopy("_" + tar.funcName + num);
                    LocalTmpVar newDef = stmtCopy.getSecond();
                    if (newDef != null) {
                        LocalTmpVar def = (LocalTmpVar) stmt.getDef();
                        copyMap.put(def, newDef);
                    }
                    //insert stmt
                    Stmt newStmt = stmtCopy.getFirst();
                    if (newStmt instanceof Phi phi) {
                        for (int i = 0; i < phi.label1.size(); i++) {
                            if (renameMap.containsKey(phi.label1.get(i))) {
                                phi.label1.set(i, renameMap.get(phi.label1.get(i)));
                            }
                        }
                        for (int i = 0; i < phi.label2.size(); i++) {
                            if (renameMap.containsKey(phi.label2.get(i))) {
                                phi.label2.set(i, renameMap.get(phi.label2.get(i)));
                            }
                        }
                    }
                    iterInCurBlock.add(newStmt);
                }
            }
            TerminalStmt tailStmt = srcBlock.tailStmt;
            Entity newUse = null;
            ArrayList<Entity> newUseList = new ArrayList<>();
            if (tailStmt instanceof Jump jump) {
                newUse = jump.result;
            } else if (tailStmt instanceof Branch br) {
                if (br.condition instanceof LocalVar localVar) {
                    newUseList.add(curAllocaMap.get(localVar));
                } else if (br.condition instanceof LocalTmpVar localTmpVar) {
                    newUseList.add(copyMap.get(localTmpVar));
                }
                newUse = br.result;
            }
            if (newUse instanceof LocalVar localVar) {
                newUse = curAllocaMap.get(localVar);
            } else if (newUse instanceof LocalTmpVar localTmpVar) {
                newUse = copyMap.get(localTmpVar);
            }
            newUseList.add(newUse);
            Pair<Stmt, LocalTmpVar> stmtCopy = tailStmt.creatCopy("_" + tar.funcName + num);
            curBlock.tailStmt = (TerminalStmt) stmtCopy.getFirst();
            terminalStmts.add(curBlock.tailStmt);
        }
        //todo:src.ret != null能保证？
        curBlock = new BasicBlock(src.ret.label + "_" + tar.funcName + num);
        blocks.add(curBlock);
        newBlock.put(curBlock.label, curBlock);
        renameMap.put(callingBlock.label, curBlock.label);
        iterInCurBlock = curBlock.statements.listIterator();
        if (call.result != null) {
            Load loadStmt = (Load) src.ret.statements.get(0);
            iterInCurBlock.add(
                    new Load(call.result, curAllocaMap.get((LocalVar) loadStmt.pointer))
            );
        }
        curBlock.statements.addAll(
                callingBlock.statements.subList
                        (stmtIterator.nextIndex(), callingBlock.statements.size())
        );
        curBlock.tailStmt = callingBlock.tailStmt;
        replaceUse(copyMap, blocks);
        mergePhiResult(src, tar, copyMap, "_" + tar.funcName + num);
    }

    void mergePhiResult(Function src, Function tar, HashMap<LocalTmpVar, Storage> copyMap, String suffix) {
        for (Map.Entry<String, Storage> entry : src.phiResult.entrySet()) {
            tar.phiResult.put(entry.getKey() + suffix, (Storage) replace(entry.getValue(), copyMap, curAllocaMap));
        }
    }

    Entity replace(Entity entity, HashMap<LocalTmpVar, Storage> copyMap, HashMap<LocalVar, LocalVar> curAllocaMap) {
        if (entity instanceof LocalVar && curAllocaMap.containsKey(entity)) {
            entity = curAllocaMap.get(entity);
        } else if (entity instanceof LocalTmpVar && copyMap.containsKey(entity)) {
            entity = copyMap.get(entity);
        }
        return entity;
    }

    void replaceUse(HashMap<LocalTmpVar, Storage> copyMap, ArrayList<BasicBlock> blocks) {
        for (BasicBlock block : blocks) {
            for (Stmt stmt : block.statements) {
                stmt.replaceUse(copyMap, curAllocaMap);
            }
            block.tailStmt.replaceUse(copyMap, curAllocaMap);
        }
    }

    void targetRedirect(Function func) {
        for (Map.Entry<String, BasicBlock> entry : func.blockMap.entrySet()) {
            TerminalStmt tailStmt = entry.getValue().tailStmt;
            if (tailStmt instanceof Jump jump) {
                jump.target = getTarget(jump.targetName, func);
            } else if (tailStmt instanceof Branch branch) {
                branch.trueBranch = getTarget(branch.trueBranchName, func);
                branch.falseBranch = getTarget(branch.falseBranchName, func);
            }
        }
    }

    BasicBlock getTarget(String name, Function func) {
        if (func.blockMap.containsKey(name)) {
            return func.blockMap.get(name);
        } else if (name.equals(func.ret.label)) {
            return func.ret;
        } else {
            throw new InternalException("jump target not found");
        }
    }

    void removeUnusedFunction() {
        HashSet<String> usedFunc = new HashSet<>();
        usedFunc.add(irRoot.mainFunc.funcName);
        Queue<Function> queue = new LinkedList<>();
        queue.add(irRoot.mainFunc);
        while (!queue.isEmpty()) {
            Function func = queue.poll();
            for (Map.Entry<Function, Integer> entry : func.calleeMap.entrySet()) {
                Function callee = entry.getKey();
                if (!usedFunc.contains(callee.funcName)) {
                    usedFunc.add(callee.funcName);
                    queue.add(callee);
                }
            }
        }
        HashMap<String, Function> usedFunctions = new HashMap<>();
        for (String funcName : usedFunc) {
            usedFunctions.put(funcName, irRoot.funcDef.get(funcName));
        }
        // TODO: use builtin only
        for (Map.Entry<String, Function> funcEntry : irRoot.funcDef.entrySet()) {
            Function func = funcEntry.getValue();
            if (func.entry == null) {
                usedFunctions.put(func.funcName, func);
            }
        }
        irRoot.funcDef = usedFunctions;
    }
}
