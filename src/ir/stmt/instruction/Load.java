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
    public Entity resultPtr;
    public Entity pointer;

    public Load(Entity resultPtr,
                Entity pointer) {
        if (!(pointer instanceof GlobalPtr || pointer instanceof LocalPtr)) {
            throw new InternalException("should load from a pointer");
        }
        if (!(resultPtr instanceof GlobalPtr || resultPtr instanceof LocalPtr)) {
            throw new InternalException("should load to a pointer");
        }
        this.resultPtr = resultPtr;
        this.pointer = pointer;
    }

    @Override
    public void print() {
        String ty;
        if (resultPtr instanceof GlobalPtr ptr) {
            ty = ptr.storage.toString();
        } else {
            ty = ((LocalPtr) resultPtr).storage.toString();
        }
        System.out.println(resultPtr.toString() + " = load "
                + ty + ", ptr " + pointer.toString());
    }

    @Override
    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }
}
