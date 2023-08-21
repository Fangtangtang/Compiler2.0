package asm.instruction;

import asm.ASMVisitor;
import asm.operand.Imm;
import asm.operand.Register;

import java.io.PrintStream;

/**
 * @author F
 * Load Upper Imm
 */
public class LuiInst extends ASMInstruction {
    public Register rd;
    public Imm imm;

    public LuiInst(Register rd,
                   Imm imm) {
        this.rd = rd;
        this.imm = imm;
    }

    @Override
    public void print(PrintStream out) {
        out.println("\tlui\t" + rd + ", " + imm);
    }

    @Override
    public void accept(ASMVisitor visitor) {
        visitor.visit(this);
    }
}
