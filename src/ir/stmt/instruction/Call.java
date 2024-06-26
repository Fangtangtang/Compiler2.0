package ir.stmt.instruction;

import ir.IRVisitor;
import ir.entity.Entity;
import ir.entity.Storage;
import ir.entity.constant.Constant;
import ir.entity.var.GlobalVar;
import ir.entity.var.LocalTmpVar;
import ir.entity.var.LocalVar;
import ir.entity.var.Ptr;
import ir.function.Function;
import ir.irType.VoidType;
import ir.stmt.Stmt;
import utility.Pair;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author F
 * 函数调用指令
 * <result> = call <ResultType> @<FunctionName>(<arguments>)
 * call void @<FunctionName>(<arguments>)
 * +-----------------------------------------------------------
 * |
 * |    int b=foo(1);   ->  %call = call i32 @foo(int)(i32 1)
 * |                        store i32 %call, ptr %b
 * |
 * +------------------------------------------------------------
 */
public class Call extends Instruction {
    public ArrayList<Storage> parameterList = new ArrayList<>();
    public Function function;
    public LocalTmpVar result;

    public Call(Function function,
                LocalTmpVar result) {
        this.function = function;
        this.result = result;
    }

    //无返回值
    public Call(Function function) {
        this.function = function;
    }

    public Call(Function function,
                LocalTmpVar result,
                ArrayList<Storage> parameterList) {
        this.function = function;
        if (result == null) {
            this.result = null;
        } else if (result.type instanceof VoidType) {
            this.result = null;
        } else {
            this.result = result;
        }
        this.parameterList = parameterList;
    }

    @Override
    public void print(PrintStream out) {
        StringBuilder str = new StringBuilder("\t");
        if (result != null && !(result.type instanceof VoidType)) {
            str.append(result.toString()).append(" = ");
        }
        str.append("call ").append(function.retType).append(" @").append(function.funcName)
                .append("(");
        Storage param;
        if (parameterList != null) {
            if (parameterList.size() > 0) {
                param = parameterList.get(0);
                if (param instanceof LocalTmpVar tmp) {
                    str.append(tmp.type).append(" ").append(tmp.toString());
                } else {
                    str.append(param);
                }
            }
            for (int i = 1; i < parameterList.size(); ++i) {
                str.append(", ");
                param = parameterList.get(i);
                if (param instanceof LocalTmpVar tmp) {
                    str.append(tmp.type).append(" ").append(tmp.toString());
                } else {
                    str.append(param);
                }
            }
        }
        str.append(")");
        out.println(str);
    }

    @Override
    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }

    @Override
    public ArrayList<Entity> getUse() {
        return new ArrayList<>(parameterList);
    }

    @Override
    public boolean hasDef() {
        return true;
    }

    @Override
    public Entity getDef() {
        return result;
    }

    @Override
    public void promoteGlobalVar() {
        for (Storage para : parameterList) {
            if (para instanceof GlobalVar globalVar && globalVar.convertedLocalVar != null) {
                para = globalVar.convertedLocalVar;
            }
        }
    }

    @Override
    public void propagateLocalTmpVar() {
        for (int i = 0; i < parameterList.size(); i++) {
            Storage para = parameterList.get(i);
            if (para instanceof Ptr ptr && ptr.valueInBasicBlock != null) {
                parameterList.set(i, ptr.valueInBasicBlock);
            } else if (para instanceof LocalTmpVar tmpVar && tmpVar.valueInBasicBlock != null) {
                parameterList.set(i, tmpVar.valueInBasicBlock);
            }
        }
    }

    @Override
    public Pair<Stmt, LocalTmpVar> creatCopy(String suffix) {
        LocalTmpVar newResult;
        if (result == null) {
            newResult = null;
        } else {
            newResult = new LocalTmpVar(result.type, result.identity + suffix);
        }
        Stmt stmt = new Call(function, newResult, parameterList);
        return new Pair<>(stmt, newResult);
    }

    @Override
    public void replaceUse(HashMap<String, Storage> constantMap) {
        ArrayList<Storage> prev = parameterList;
        parameterList = new ArrayList<>();
        for (Storage param : prev) {
            parameterList.add((Storage) replace(param, constantMap));
        }
    }

    @Override
    public void replaceUse(HashMap<LocalTmpVar, Storage> copyMap, HashMap<LocalVar, LocalVar> curAllocaMap) {
        ArrayList<Storage> prev = parameterList;
        parameterList = new ArrayList<>();
        for (Storage param : prev) {
            parameterList.add((Storage) replace(param, copyMap, curAllocaMap));
        }
    }

    @Override
    public Constant getConstResult() {
        return null;
    }

}
