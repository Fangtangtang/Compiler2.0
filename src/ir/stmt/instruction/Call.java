package ir.stmt.instruction;

import ir.IRVisitor;
import ir.entity.Storage;
import ir.entity.var.LocalTmpVar;
import ir.function.Function;

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
    public void print() {
        StringBuilder str = new StringBuilder(
                result.toString() + " = call " + function.retType
                        + "@" + function.funcName + "("
        );
        parameterList.forEach(
                parameter -> str.append(parameter.type).append(" ")
        );
        str.append(")");
        System.out.println(str);
    }

    @Override
    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }
}
