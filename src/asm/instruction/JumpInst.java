package asm.instruction;

import asm.ASMVisitor;

import java.io.PrintStream;

/**
 * @author F
 * 无条件跳转指令
 */
public class JumpInst extends ASMInstruction {
    public String desName;

    @Override
    public void print(PrintStream out) {
        out.println("\tj\t" + desName);
    }

    @Override
    public void accept(ASMVisitor visitor) {

    }
}
