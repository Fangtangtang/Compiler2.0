package ir.stmt.instruction;

import ir.IRVisitor;
import ir.entity.Entity;
import ir.entity.register.Register;
import ir.irType.PtrType;
import utility.error.InternalException;

/**
 * @author F
 * 将entity存到指针指向位置
 * store <ty> <value>, ptr <pointer>
 */
public class Store extends Instruction {
    public Entity value;
    public Register pointerReg;

    public Store(Entity value,
                 Register pointerReg) {
        if (!(pointerReg.type instanceof PtrType)) {
            throw new InternalException("should store to pointer");
        }
        this.value = value;
        this.pointerReg = pointerReg;
    }

    @Override
    public void print() {
        System.out.println("store " + value.type.toString() + " " + value.toString()
                + ", ptr " + pointerReg.toString());
    }

    @Override
    public void accept(IRVisitor irVisitor) {

    }
}
