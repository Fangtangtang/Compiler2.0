package asm.instruction;

import asm.ASMVisitor;
import asm.operand.Register;

import java.io.PrintStream;

/**
 * @author F
 * 条件跳转
 */
public class BranchInst extends ASMInstruction {

    enum Opcode {
        beq, bne, blt, bge
    }

    public Register rs1, rs2;
    public String desName;

    public Opcode op;

    @Override
    public void print(PrintStream out) {

    }

    @Override
    public void accept(ASMVisitor visitor) {

    }
}
