package ir.stmt.instruction;

import ir.IRVisitor;
import ir.entity.*;
import ir.irType.*;

/**
 * @author F
 * 分配内存空间的指令
 * <result> = alloca <type>
 * result为指针类型
 * type为指针指向对象的类型
 */
public class Alloca extends Instruction {

    public Entity result;
    public IRType irType;

    public Alloca(Entity result) {
        this.result = result;
        this.irType = ((PtrType) result.type).type;
    }

    @Override
    public void print() {
        System.out.println(result.toString() + " = alloca " + irType.toString());
    }

    @Override
    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }
}
