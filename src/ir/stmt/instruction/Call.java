package ir.stmt.instruction;

import ir.IRVisitor;
import ir.entity.Storage;
import ir.entity.var.LocalTmpVar;
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
    public Function function;
    public LocalTmpVar result;

    public Call(Function function,
                LocalTmpVar result) {
        this.function = function;
        this.result = result;
    }

    public Call(Function function,
                LocalTmpVar result,
                Storage parameter) {
        this.function = function;
        this.result = result;
        this.parameterList.add(parameter);
    }

    @Override
    public void print(PrintStream out) {
        StringBuilder str = new StringBuilder();

        if (!(result.type instanceof VoidType)) {
            str.append("\t").append(result.toString()).append(" = ");
        }
        str.append("call ").append(function.retType).append(" @").append(function.funcName).append("(");
        if (parameterList.size() > 0) {
            str.append(parameterList.get(0));
        }
        for (int i = 1; i < parameterList.size(); ++i) {
            str.append(", ").append(parameterList.get(i));
        }
        str.append(")");
        out.println(str);
    }

    @Override
    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }
}
