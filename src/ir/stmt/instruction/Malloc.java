package ir.stmt.instruction;

import ir.IRVisitor;
import ir.entity.Entity;
import ir.entity.var.LocalTmpVar;
import ir.irType.PtrType;

/**
 * @author F
 * 一个自定义的内存分配指令
 * 调用_malloc
 */
public class Malloc extends Instruction {

    public LocalTmpVar result;

    public Entity length;

    public Malloc(LocalTmpVar result,
                  Entity length) {
        this.result = result;
        this.length = length;
    }

    @Override
    public void print() {
        System.out.println(
                result + " = malloc "
                        + length + ", " + ((PtrType) result.type).type
        );
    }

    @Override
    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }
}
