package asm.instruction;

import asm.ASMVisitor;
import asm.operand.*;
import backend.optimizer.interferenceGraph.Colors;

import java.io.PrintStream;
import java.util.ArrayList;

/**
 * @author F
 * ASM指令
 */
public abstract class ASMInstruction {
    boolean aliveByNature = false;

    public abstract void print(PrintStream out);

    public abstract void printRegColoring(PrintStream out);

    public abstract void accept(ASMVisitor visitor);

    public abstract ArrayList<Register> getUse();

    public abstract Register getDef();

    public static ArrayList<Colors.Color> getUsePhysical(ASMInstruction instruction) {
        ArrayList<Colors.Color> ret = new ArrayList<>();
        ArrayList<Register> regs = instruction.getUse();
        if (regs != null) {
            for (Register reg : regs) {
                ret.add(reg.color);
            }
        }
        return ret;
    }

    public static Colors.Color getDefPhysical(ASMInstruction instruction) {
        if (instruction.getDef() == null) {
            return null;
        }
        return instruction.getDef().color;
    }

    public boolean isAliveByNature() {
        return aliveByNature;
    }

    public abstract void setUse(ArrayList<Register> use);

    public abstract void setDef(Register def);
}
