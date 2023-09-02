package ir.stmt.instruction;

import ir.IRVisitor;
import ir.entity.Entity;
import ir.entity.constant.*;
import ir.entity.var.*;
import ir.irType.PtrType;

import java.io.PrintStream;
import java.util.ArrayList;

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
            str = ptr.storage.type + " " + ptr;
        }
        else {
            str = value.toString();
        }
        out.println("\tstore " + str
                + ", ptr " + pointer.toString());
    }

    @Override
    public void printSSA(PrintStream out) {
        String str;
        if (value instanceof LocalTmpVar) {
            str = value.type.toString() + " " + value.renamedToString();
        } else if (value instanceof Ptr ptr) {
            str = ptr.storage.type + " " + ptr;
        }
        else {
            str = value.renamedToString();
        }
        out.println("\tstore " + str
                + ", ptr " + pointer.renamedToString());
    }

    @Override
    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }

    @Override
    public ArrayList<Entity> getUse() {
        ArrayList<Entity> ret = new ArrayList<>();
        ret.add(value);
        ret.add(pointer);
        return ret;
    }

    @Override
    public Entity getDef() {
        return null;
    }
}
