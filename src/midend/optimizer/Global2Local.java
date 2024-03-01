package midend.optimizer;

import ir.*;
import ir.entity.*;
import ir.entity.constant.ConstString;
import ir.entity.constant.Constant;
import ir.entity.var.GlobalVar;
import ir.entity.var.LocalVar;
import ir.function.Function;
import ir.stmt.Stmt;
import ir.stmt.instruction.Alloca;
import ir.stmt.instruction.Global;
import ir.stmt.instruction.Store;

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
    public void execute() {
        analysisGlobalVarUsage();
        simplifyGlobalVarDefBlock();
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
    }

    private void collectOnStmt(String funcName, Stmt stmt) {
        Entity varDef = stmt.getDef();
        ArrayList<Entity> varUse = stmt.getUse();
        if (varDef instanceof GlobalVar globalVar) {
            globalVar.occurrence.add(funcName);
        }
        if (varUse != null) {
            for (Entity element : varUse) {
                if (element instanceof GlobalVar globalVar) {
                    globalVar.occurrence.add(funcName);
                }
            }
        }
    }

    /**
     * 将不出现或只在一个函数中出现的global var从全局定义中删除
     * 全是Global stmt
     */
    private void simplifyGlobalVarDefBlock() {
        Iterator<Stmt> iterator = irRoot.globalVarDefBlock.statements.iterator();
        while (iterator.hasNext()) {
            Stmt stmt = iterator.next();
            //字符串常量不改
            if (stmt instanceof Global globalStmt) {
                if (!(globalStmt.result.storage instanceof ConstString)) {
                    if (globalStmt.result.occurrence.size() == 1) {
                        for (String funcName : globalStmt.result.occurrence) {
                            if (Objects.equals(funcName, "main")) {
                                iterator.remove();
                                Function func = irRoot.funcDef.get(funcName);//使用该全局变量的函数
                                GlobalVar var = globalStmt.result;
                                var.convertedLocalVar = new LocalVar(var.storage, var.identity);
                                if (var.storage instanceof Constant) {
                                    func.entry.statements.addFirst(new Store(var.storage, var.convertedLocalVar));
                                }
                                //alloca
                                func.entry.statements.addFirst(new Alloca(var.convertedLocalVar.storage.type, var.convertedLocalVar.identity));
                            }
                        }
                    } else if (globalStmt.result.occurrence.size() == 0) {
                        iterator.remove();
                    }
                }
            } else {
                throw new ClassCastException("stmt in globalVarDefBlock supposed to be Global");
            }
        }
    }

    /**
     * pass2
     * 对global var有定值：globalVarDefBlock，globalVarInitFunction
     */
    private void promoteGlobalToLocal() {
        for (Map.Entry<String, Function> funcEntry : irRoot.funcDef.entrySet()) {
            Function func = funcEntry.getValue();
            //普通local function
            if (func.entry != null) {
                for (Map.Entry<String, BasicBlock> bbEntry : func.blockMap.entrySet()) {
                    BasicBlock block = bbEntry.getValue();
                    block.statements.forEach(Stmt::promoteGlobalVar);
                    block.tailStmt.promoteGlobalVar();
                }
            }
        }
    }

}
