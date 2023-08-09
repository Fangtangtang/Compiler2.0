package ir.stmt.instruction;

import ir.IRVisitor;
import ir.entity.*;

import java.util.ArrayList;

/**
 * @author F
 * 函数调用指令
 * <result> = call <ResultType> @<FunctionName>(<arguments>)
 * call void @<FunctionName>(<arguments>)
 */
public class Call extends Instruction {
    public ArrayList<MemStack> parameterList = new ArrayList<>();
    public MemStack resultStorage = null;
    public String funcName;

    public Call(String funcName,
                MemStack memStack) {
        this.funcName = funcName;
        this.resultStorage = memStack;
    }

    @Override
    public void print() {
        StringBuilder str = new StringBuilder();
        if (resultStorage == null) {
            str.append("call void");
        } else {
            str.append(resultStorage.toString()).append(" = call ").append(resultStorage.type);
        }
        str.append(" @").append(funcName).append(" ");
        for (MemStack memStack : parameterList) {
            str.append(memStack.toString()).append(' ');
        }
        System.out.println(str.toString());
    }

    @Override
    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }
}
