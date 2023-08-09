package ir.stmt.instruction;

import ir.IRVisitor;
import ir.entity.Entity;
import ir.entity.*;
import ir.irType.PtrType;
import utility.error.InternalException;

/**
 * @author F
 * 将entity存到指针指向位置
 * store <ty> <value>, ptr <pointer>
 */
public class Store extends Instruction {
    public Entity value;
    public MemStack pointerStorage;

    public Store(Entity value,
                 MemStack pointerStorage) {
        if (!(pointerStorage.type instanceof PtrType)) {
            throw new InternalException("should store to pointer");
        }
        this.value = value;
        this.pointerStorage = pointerStorage;
    }

    @Override
    public void print() {
        System.out.println("store " + value.type.toString() + " " + value.toString()
                + ", ptr " + pointerStorage.toString());
    }

    @Override
    public void accept(IRVisitor irVisitor) {

    }
}
