package asm.instruction;

import asm.ASMVisitor;
import asm.operand.*;

import java.io.PrintStream;

/**
 * @author F
 * 加载
 */
public class LoadInst extends ASMInstruction {
    public Register rs1, rd;
    public Imm imm;

    @Override
    public void print(PrintStream out) {

    }

    @Override
    public void accept(ASMVisitor visitor) {

    }
}
