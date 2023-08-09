package ir.stmt.instruction;

import ir.IRVisitor;
import ir.entity.*;
import ir.entity.ptr.GlobalPtr;
import ir.entity.ptr.LocalPtr;
import ir.entity.ptr.Ptr;

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
    public ArrayList<Ptr> parameterList = new ArrayList<>();
    public Ptr result = null;
    public String funcName;

    public Call(String funcName,
                Ptr result) {
        this.funcName = funcName;
        this.result = result;
    }

    @Override
    public void print() {
        StringBuilder str = new StringBuilder();
        if (result == null) {
            str.append("call void");
        } else {
            str.append(result.toString()).append(" = call ").append( result.storage.toString());
        }
        str.append(" @").append(funcName).append(" ");
        for (Ptr parameter : parameterList) {
            str.append(parameter.toString()).append(' ');
        }
        System.out.println(str.toString());
    }

    @Override
    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }
}
