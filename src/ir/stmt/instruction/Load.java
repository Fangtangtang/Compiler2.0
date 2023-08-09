package ir.stmt.instruction;

import ir.IRVisitor;
import ir.entity.*;
import ir.irType.PtrType;
import utility.error.InternalException;


/**
 * @author F
 * 将指针指向的值赋值给result
 * <result> = load <ty>, ptr <pointer>
 */
public class Load extends Instruction {
    public MemStack resultStorage;
    public MemStack pointerStorage;

    public Load(MemStack resultStorage,
                MemStack pointerStorage) {
        if (!(pointerStorage.type instanceof PtrType)) {
            throw new InternalException("should load from a pointer");
        }
        this.resultStorage = resultStorage;
        this.pointerStorage = pointerStorage;
    }

    @Override
    public void print() {
        System.out.println(resultStorage.toString() + " = load " + resultStorage.type.toString()
                + ", ptr " + pointerStorage.toString());
    }

    @Override
    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }
}
