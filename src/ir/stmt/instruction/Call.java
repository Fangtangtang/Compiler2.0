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
    public ArrayList<Storage> parameterList = new ArrayList<>();
    public Storage resultStorage = null;
    public String funcName;

    public Call(String funcName,
                Storage storage) {
        this.funcName = funcName;
        this.resultStorage = storage;
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
        for (Storage storage : parameterList) {
            str.append(storage.toString()).append(' ');
        }
        System.out.println(str.toString());
    }

    @Override
    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }
}
