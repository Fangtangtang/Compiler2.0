package ir.stmt.instruction;

import ir.IRVisitor;
import ir.entity.Entity;
import ir.entity.var.*;

/**
 * @author F
 * 将entity存到指针指向位置
 * store <ty> <value>, ptr <pointer>
 * + -----------------------------------
 * |
 * |    int a = b;
 * |    %0 = load i32, ptr %b
 * |    store i32 %0, ptr %a
 * |
 * |    b = 1;
 * |    store i32 1, ptr %b
 * |
 * + ------------------------------------
 */
public class Store extends Instruction {
    public Entity value;
    public Ptr pointer;

    public Store(Entity value,
                 Ptr pointer) {
        this.value = value;
        this.pointer = pointer;
    }

    @Override
    public void print() {
        System.out.println("store " + pointer.storage.toString() + " " + value.toString()
                + ", ptr " + pointer.toString());
    }

    @Override
    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }
}
