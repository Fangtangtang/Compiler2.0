package ir.stmt.instruction;

import ir.IRVisitor;
import ir.entity.register.*;
import ir.irType.*;

/**
 * @author F
 * 分配内存空间的指令
 * <result> = alloca <type>
 * result为指针类型
 * type为指针指向对象的类型
 */
public class Alloca extends Instruction {

    public Register resultReg;
    public IRType irType;

    public Alloca(Register register) {
        this.resultReg = register;
        this.irType = ((PtrType) register.type).type;
    }

    @Override
    public void print() {
        System.out.println(resultReg.toString() + " = alloca " + irType.toString());
    }

    @Override
    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }
}
