package asm.instruction;

import asm.ASMVisitor;
import asm.operand.*;

import java.io.PrintStream;

/**
 * @author F
 * 加载
 */
public class LoadInst extends ASMInstruction {
    public Register rs1, rd;
    public Imm imm;

    public LoadInst(Register rs1, Register rd,
                    Imm imm) {
        this.rs1 = rs1;
        this.rd = rd;
        this.imm = imm;
    }

    @Override
    public void print(PrintStream out) {
        out.println("\tlw\t" + rd + ", " + imm + "(" + rs1 + ")");
    }

    @Override
    public void accept(ASMVisitor visitor) {
        visitor.visit(this);
    }
}
