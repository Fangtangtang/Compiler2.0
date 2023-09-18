package asm.instruction;

import asm.ASMVisitor;
import asm.operand.Register;

import java.io.PrintStream;
import java.util.ArrayList;

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
    public void printRegColoring(PrintStream out) {
        out.println("\tnot\t" + rd.toRegColoringString() + ", " + rs1.toRegColoringString());
    }

    @Override
    public void accept(ASMVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public ArrayList<Register> getUse() {
        ArrayList<Register> ret = new ArrayList<>();
        ret.add(rs1);
        return ret;
    }

    @Override
    public Register getDef() {
        return rd;
    }

    @Override
    public void setUse(ArrayList<Register> use) {
        rs1 = use.get(0);
    }

    @Override
    public void setDef(Register def) {
        rd = def;
    }
}
