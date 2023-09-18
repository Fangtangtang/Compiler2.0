package asm.instruction;

import asm.ASMVisitor;
import asm.operand.*;

import java.io.PrintStream;
import java.util.ArrayList;

/**
 * @author F
 * ASM指令
 */
public abstract class ASMInstruction {

    public abstract void print(PrintStream out);

    public abstract void accept(ASMVisitor visitor);

    public abstract ArrayList<Register> getUse();

    public abstract Register getDef();

    public abstract void setUse(ArrayList<Register> use);

    public abstract void setDef(Register def);
}
