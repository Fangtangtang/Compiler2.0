package ir.stmt.instruction;

import ir.IRVisitor;
import ir.entity.register.Register;
import ir.irType.PtrType;
import utility.error.InternalException;


/**
 * @author F
 * 将指针指向的值赋值给result
 * <result> = load <ty>, ptr <pointer>
 */
public class Load extends Instruction {
    public Register resultReg;
    public Register pointerReg;

    public Load(Register resultReg,
                Register pointerReg) {
        if (!(pointerReg.type instanceof PtrType)) {
            throw new InternalException("should load from a pointer");
        }
        this.resultReg = resultReg;
        this.pointerReg = pointerReg;
    }

    @Override
    public void print() {
        System.out.println(resultReg.toString() + " = load " + resultReg.type.toString()
                + ", ptr " + pointerReg.toString());
    }

    @Override
    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }
}
