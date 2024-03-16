package midend.optimizer;

import ir.*;
import ir.function.*;
import ir.stmt.*;
import ir.stmt.instruction.Call;

import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;

/**
 * @author F
 * 函数内联
 * - 暂不处理递归函数
 * - 函数调用关系成图，尝试将tail全缩掉？
 * TODO: 选择哪些做 inlining
 */
public class FunctionInlining {
    IRRoot irRoot;

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
                      ListIterator<Stmt> stmtIterator,
                      int num) {
        //未被inline到该函数中
        if (!src.inlinedToCaller.contains(tar.funcName)) {
            //转移localVar

            //重新打标签
            src.inlinedToCaller.add(tar.funcName);
        }


    }
}
