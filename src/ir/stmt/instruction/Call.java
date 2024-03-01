package ir.stmt.instruction;

import ir.IRVisitor;
import ir.entity.Entity;
import ir.entity.SSAEntity;
import ir.entity.Storage;
import ir.entity.constant.Constant;
import ir.entity.var.GlobalVar;
import ir.entity.var.LocalTmpVar;
import ir.entity.var.Ptr;
import ir.function.Function;
import ir.irType.VoidType;

import java.io.PrintStream;
import java.util.ArrayList;

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
    public ArrayList<SSAEntity> ssaParameterList;
    public Function function;
    public LocalTmpVar result;
    public SSAEntity ssaResult;

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
        if (result.type instanceof VoidType) {
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
    public void printSSA(PrintStream out) {
        StringBuilder str = new StringBuilder("\t");
        if (ssaResult != null && !(ssaResult.origin.type instanceof VoidType)) {
            str.append(ssaResult.toString()).append(" = ");
        }
        str.append("call ").append(function.retType).append(" @").append(function.funcName)
                .append("(");
        SSAEntity param;
        if (ssaParameterList != null) {
            if (ssaParameterList.size() > 0) {
                param = ssaParameterList.get(0);
                if (param.origin instanceof LocalTmpVar tmp) {
                    str.append(tmp.type).append(" ").append(param.toString());
                } else {
                    str.append(param);
                }
            }
            for (int i = 1; i < ssaParameterList.size(); ++i) {
                str.append(", ");
                param = ssaParameterList.get(i);
                if (param.origin instanceof LocalTmpVar tmp) {
                    str.append(tmp.type).append(" ").append(param.toString());
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
        for (Storage para : parameterList) {
            if (para instanceof Ptr ptr) {
                para = ptr.valueInBasicBlock == null ? para : ptr.valueInBasicBlock;
            } else if (para instanceof LocalTmpVar tmpVar) {
                para = tmpVar.valueInBasicBlock == null ? para : tmpVar.valueInBasicBlock;
            }
        }
    }

    @Override
    public Constant getConstResult() {
        return null;
    }

    @Override
    public void setUse(ArrayList<SSAEntity> list) {
        ssaParameterList = list;
    }

    @Override
    public void setDef(SSAEntity entity) {
        ssaResult = entity;
    }

    @Override
    public ArrayList<SSAEntity> getSSAUse() {
        return ssaParameterList;
    }

    @Override
    public SSAEntity getSSADef() {
        return ssaResult;
    }


}
