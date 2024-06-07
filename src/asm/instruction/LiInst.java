package asm.instruction;

import asm.ASMVisitor;
import asm.operand.Imm;
import asm.operand.Register;

import java.io.PrintStream;
import java.util.ArrayList;

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
        this.rd.size = this.imm.size;
    }

    public LiInst(Register rd, Imm imm, boolean isRet) {
        this.rd = rd;
        this.imm = imm;
        this.rd.size = this.imm.size;
        aliveByNature = isRet;
    }

    @Override
    public void print(PrintStream out) {
        out.println("\tli\t" + rd + ", " + imm);
    }

    @Override
    public void printRegColoring(PrintStream out) {
        out.println("\tli\t" + rd.toRegColoringString() + ", " + imm);
    }

    @Override
    public void accept(ASMVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public ArrayList<Register> getUse() {
        return null;
    }

    @Override
    public Register getDef() {
        return rd;
    }

    @Override
    public void setUse(ArrayList<Register> use) {

    }

    @Override
    public void setDef(Register def) {
        rd = def;
    }
}
