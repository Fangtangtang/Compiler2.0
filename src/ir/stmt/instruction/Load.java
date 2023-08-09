package ir.stmt.instruction;

import ir.IRVisitor;
import ir.entity.*;
import ir.entity.ptr.*;
import utility.error.InternalException;


/**
 * @author F
 * 将指针指向的值赋值给result
 * <result> = load <ty>, ptr <pointer>
 * + -----------------------------------
 * |
 * |    int a = b;
 * |    %0 = load i32, ptr %b
 * |    store i32 %0, ptr %a
 * |
 * + ------------------------------------
 */
public class Load extends Instruction {
    public Ptr resultPtr;
    public Ptr pointer;

    public Load(Ptr resultPtr,
                Ptr pointer) {
        this.resultPtr = resultPtr;
        this.pointer = pointer;
    }

    @Override
    public void print() {
        System.out.println(resultPtr.toString() + " = load "
                +  resultPtr.storage.toString() + ", ptr " + pointer.toString());
    }

    @Override
    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }
}
