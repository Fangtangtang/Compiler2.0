package asm.instruction;

import asm.ASMVisitor;
import asm.operand.*;

import java.io.PrintStream;

/**
 * @author F
 * ASM指令
 */
public abstract class ASMInstruction {

    public abstract void print(PrintStream out);

    public abstract void accept(ASMVisitor visitor);
}
