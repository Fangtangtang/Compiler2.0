package asm.instruction;

import asm.ASMVisitor;

import java.io.PrintStream;

/**
 * @author F
 * 返回caller
 */
public class RetInst extends ASMInstruction {

    @Override
    public void print(PrintStream out) {
        out.println("\tret");
    }

    @Override
    public void accept(ASMVisitor visitor) {
        visitor.visit(this);
    }
}
