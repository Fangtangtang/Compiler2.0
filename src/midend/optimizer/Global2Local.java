package midend.optimizer;

import ir.*;
import ir.entity.*;
import ir.entity.var.GlobalVar;
import ir.function.Function;
import ir.stmt.Stmt;

import java.util.*;

/**
 * @author F
 * make global variables used only in one function local
 * pass1: 收集全局变量在各个现存local函数中的使用情况
 * pass2: 将未被使用的var删除，将仅在一个函数中使用的var改为local（删globalDef，加alloca，改使用）
 */
public class Global2Local {
    IRRoot irRoot;

    public Global2Local(IRRoot root) {
        this.irRoot = root;
    }

    /**
     * 执行优化
     */
    public void work() {
        analysisGlobalVarUsage();
        promoteGlobalToLocal();
    }

    /**
     * pass1
     * 遍历local function每个stmt中的entity
     */
    private void analysisGlobalVarUsage() {
        for (Map.Entry<String, Function> funcEntry : irRoot.funcDef.entrySet()) {
            Function func = funcEntry.getValue();
            //普通local function
            if (func.entry != null) {
                for (Map.Entry<String, BasicBlock> bbEntry : func.blockMap.entrySet()) {
                    BasicBlock block = bbEntry.getValue();
                    block.statements.forEach(stmt -> collectOnStmt(func.funcName, stmt));
                    collectOnStmt(func.funcName, block.tailStmt);
                }
            }
        }
        //globalVarInitFunction
        if (irRoot.globalVarInitFunction.entry != null) {
            for (Map.Entry<String, BasicBlock> bbEntry : irRoot.globalVarInitFunction.blockMap.entrySet()) {
                BasicBlock block = bbEntry.getValue();
                block.statements.forEach(stmt -> collectOnStmt(irRoot.globalVarInitFunction.funcName, stmt));
                collectOnStmt(irRoot.globalVarInitFunction.funcName, block.tailStmt);
            }
        }
    }

    private void collectOnStmt(String funcName, Stmt stmt) {
        Entity varDef = stmt.getDef();
        ArrayList<Entity> varUse = stmt.getUse();
        if (varDef instanceof GlobalVar globalVar) {
            globalVar.occurrence.add(funcName);
        }
        for (Entity element : varUse) {
            if (element instanceof GlobalVar globalVar) {
                globalVar.occurrence.add(funcName);
            }
        }
    }

    /**
     * pass2
     * 对global var有定值：globalVarDefBlock，globalVarInitFunction
     */
    private void promoteGlobalToLocal() {
//        TODO
        //

    }

}
