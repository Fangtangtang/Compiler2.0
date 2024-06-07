package asm.instruction;

import asm.ASMVisitor;
import asm.operand.Register;

import java.io.PrintStream;
import java.util.ArrayList;

/**
 * @author F
 * 返回caller
 */
public class RetInst extends ASMInstruction {

    public RetInst() {
        aliveByNature = true;
    }

    @Override
    public void print(PrintStream out) {
        out.println("\tret");
    }

    @Override
    public void printRegColoring(PrintStream out) {
        out.println("\tret");
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
        return null;
    }

    @Override
    public void setUse(ArrayList<Register> use) {

    }

    @Override
    public void setDef(Register def) {

    }
}
