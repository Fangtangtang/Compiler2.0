package asm.instruction;

import asm.ASMVisitor;
import asm.operand.Imm;
import asm.operand.Register;

import java.io.PrintStream;

/**
 * @author F
 * load imm
 * 将立即数加载到物理寄存器
 * （随后sw等）
 * -------------------
 * li	a0, 1
 * sb	a0, -11(s0)
 * -------------------
 */
public class LiInst extends ASMInstruction {

    public Register rd;
    public Imm imm;

    public LiInst(Register rd, Imm imm) {
        this.rd = rd;
        this.imm = imm;
    }

    @Override
    public void print(PrintStream out) {
        out.println("\tli\t" + rd + ", " + imm);
    }

    @Override
    public void accept(ASMVisitor visitor) {
        visitor.visit(this);
    }
}
