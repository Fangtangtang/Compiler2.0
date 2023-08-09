package ir.stmt.instruction;

import ir.IRVisitor;
import ir.entity.Entity;
import ir.entity.*;
import ir.entity.ptr.GlobalPtr;
import ir.entity.ptr.LocalPtr;
import ir.irType.PtrType;
import utility.error.InternalException;

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
        if (!(pointer instanceof GlobalPtr || pointer instanceof LocalPtr)) {
            throw new InternalException("should store to a pointer");
        }
        this.value = value;
        this.pointer = pointer;
    }

    @Override
    public void print() {
        String ty;
        if (pointer instanceof GlobalPtr ptr) {
            ty = ptr.storage.toString();
        } else {
            ty = ((LocalPtr) pointer).storage.toString();
        }
        System.out.println("store " + ty + " " + value.toString()
                + ", ptr " + pointer.toString());
    }

    @Override
    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }
}
