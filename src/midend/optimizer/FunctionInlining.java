package midend.optimizer;

import ir.*;
import ir.entity.*;
import ir.entity.var.*;
import ir.function.*;
import ir.stmt.*;
import ir.stmt.instruction.*;
import ir.stmt.terminal.*;
import utility.Pair;

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

    public FunctionInlining(IRRoot root) {
        this.irRoot = root;
    }

    public void execute() {
        analysisCalling();
        int pass = 1;
        while (pass > 0) {
            if (inliningPass()) {
                --pass;
            } else {
                break;
            }
        }
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
                        }
                    }
                }
            }
            curAllocaMap = null;
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
        //当前在处理的tar中block
        BasicBlock curBlock = new BasicBlock(callingBlock.label);
        tar.blockMap.remove(callingBlock.label);
        tar.blockMap.put(curBlock.label, curBlock);
        //call前的不变
        curBlock.statements.addAll(callingBlock.statements.subList(0, stmtIterator.previousIndex()));
        ListIterator<Stmt> iterInCurBlock = curBlock.statements.listIterator(
                curBlock.statements.size()
        );
        stmtIterator.previous();//指向call前
        //内联的localTmpVar需要创建副本
        HashMap<LocalTmpVar, Storage> copyMap = new HashMap<>();
        //函数入参
        for (int i = 0; i < src.parameterList.size(); i++) {
            LocalTmpVar param = src.parameterList.get(i);
            copyMap.put(param, call.parameterList.get(i));
        }
        boolean is_first = true;
        for (Map.Entry<String, BasicBlock> bbEntry : src.blockMap.entrySet()) {
            BasicBlock srcBlock = bbEntry.getValue();
            ListIterator<Stmt> iterator = srcBlock.statements.listIterator();
            //非src的第一个BB
            if (!is_first) {
                curBlock = new BasicBlock(srcBlock.label + "_" + num);
                tar.blockMap.put(curBlock.label, curBlock);
                iterInCurBlock = curBlock.statements.listIterator();
            } else {
                is_first = false;
            }
            //对BB中的每个语句处理
            while (iterator.hasNext()) {
                Stmt stmt = iterator.next();
                if (stmt instanceof Alloca alloca) {
                    //第一次inline到该函数中
                    if (!curAllocaMap.containsKey(alloca.result)) {
                        //转移localVar
                        Alloca allocaStmt = new Alloca(
                                alloca.result.type,
                                alloca.result.identity
                        );
                        curAllocaMap.put(alloca.result, allocaStmt.result);
                        tar.entry.statements.add(allocaStmt);
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
                    Pair<Stmt, LocalTmpVar> stmtCopy = stmt.creatCopy(newUse,"_"+num);
                    LocalTmpVar newDef = stmtCopy.getSecond();
                    if (newDef != null) {
                        LocalTmpVar def = (LocalTmpVar) stmt.getDef();
                        copyMap.put(def, newDef);
                    }
                    //insert stmt
                    Stmt newStmt = stmtCopy.getFirst();
                    iterInCurBlock.add(newStmt);
                }
            }
            TerminalStmt tailStmt = srcBlock.tailStmt;
            ArrayList<Entity> use = tailStmt.getUse();
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
            Pair<Stmt, LocalTmpVar> stmtCopy = tailStmt.creatCopy(newUse,"_"+num);
            LocalTmpVar newDef = stmtCopy.getSecond();
            if (newDef != null) {
                LocalTmpVar def = (LocalTmpVar) tailStmt.getDef();
                copyMap.put(def, newDef);
            }
            curBlock.tailStmt = (TerminalStmt) stmtCopy.getFirst();
        }
        //todo:src.ret != null能保证？
        curBlock = new BasicBlock(src.ret.label + "_" + num);
        tar.blockMap.put(curBlock.label, curBlock);
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
    }
}
