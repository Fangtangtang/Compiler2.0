package asm.instruction;

import asm.ASMVisitor;
import asm.operand.Register;

import java.io.PrintStream;

/**
 * @author F
 * 寄存器赋值
 * mv	a0, sp
 * 用于call传参
 */
public class MoveInst extends ASMInstruction {
    public Register rd;
    public Register rs1;

    public MoveInst(Register rd, Register rs1) {
        this.rd = rd;
        this.rs1 = rs1;
        rd.size = rs1.size;
    }

    @Override
    public void print(PrintStream out) {
        out.println("\tmv\t" + rd + ", " + rs1);
    }

    @Override
    public void accept(ASMVisitor visitor) {
        visitor.visit(this);
    }
}
