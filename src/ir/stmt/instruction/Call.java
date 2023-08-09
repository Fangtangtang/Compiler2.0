package ir.stmt.instruction;

import ir.IRVisitor;
import ir.entity.register.Register;

import java.util.ArrayList;

/**
 * @author F
 * 函数调用指令
 * <result> = call <ResultType> @<FunctionName>(<arguments>)
 * call void @<FunctionName>(<arguments>)
 */
public class Call extends Instruction {
    public ArrayList<Register> parameterList = new ArrayList<>();
    public Register resultReg = null;
    public String funcName;

    public Call(String funcName,
                Register register) {
        this.funcName = funcName;
        this.resultReg = register;
    }

    @Override
    public void print() {
        StringBuilder str = new StringBuilder();
        if (resultReg == null) {
            str.append("call void");
        } else {
            str.append(resultReg.toString()).append(" = call ").append(resultReg.type);
        }
        str.append(" @").append(funcName).append(" ");
        for (Register register : parameterList) {
            str.append(register.toString()).append(' ');
        }
        System.out.println(str.toString());
    }

    @Override
    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }
}
