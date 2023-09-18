package asm.instruction;

import asm.ASMVisitor;
import asm.operand.Imm;
import asm.operand.Register;

import java.io.PrintStream;
import java.util.ArrayList;

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
