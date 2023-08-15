package asm.instruction;

import asm.ASMVisitor;
import asm.operand.Register;

import java.io.PrintStream;

/**
 * @author F
 * 比较指令
 * 由多个基本指令组合的伪指令
 */
public class CmpInst extends ASMInstruction {

    enum Opcode {
        slt, sgt, sle, sge,
        eq, ne
    }

    public Register rs1, rs2;
    public Register rd;
    public Opcode op;

    @Override
    public void print(PrintStream out) {

    }

    @Override
    public void accept(ASMVisitor visitor) {

    }
}
