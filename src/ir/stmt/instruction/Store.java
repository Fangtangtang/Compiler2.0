package ir.stmt.instruction;

import ir.IRVisitor;
import ir.entity.Entity;
import ir.entity.var.*;

import java.io.PrintStream;

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
    public Entity pointer;

    public Store(Entity value,
                 Entity pointer) {
        this.value = value;
        this.pointer = pointer;
    }

    @Override
    public void print(PrintStream out) {
        String str;
        if (value instanceof LocalTmpVar) {
            str = value.type.toString() + " " + value.toString();
        } else if (value instanceof Ptr ptr) {
            str = ptr.storage.type + " " + ptr.toString();
        } else {
            str = value.toString();
        }
        out.println("\tstore " + str
                + ", ptr " + pointer.toString());
    }

    @Override
    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }
}
