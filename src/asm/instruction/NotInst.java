package asm.instruction;

import asm.ASMVisitor;
import asm.operand.PhysicalRegister;
import asm.operand.Register;

import java.io.PrintStream;

/**
 * @author F
 * 逻辑取反
 */
public class NotInst extends ASMInstruction {
    public Register rd;
    public Register rs1;

    public NotInst(Register rd, Register rs1) {
        this.rd = rd;
        this.rs1 = rs1;
    }

    @Override
    public void print(PrintStream out) {
        out.println("\tnot\t" + rd + ", " + rs1);
    }

    @Override
    public void accept(ASMVisitor visitor) {
        visitor.visit(this);
    }
}
