package asm.instruction;

import asm.ASMVisitor;
import asm.operand.Register;
import ir.stmt.instruction.Icmp;
import utility.error.InternalException;

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

    public CmpInst(Register rs1, Register rs2,
                   Register rd,
                   Icmp.Cond operator) {
        this.rs1 = rs1;
        this.rs2 = rs2;
        this.rd = rd;
        switch (operator) {
            case slt -> this.op = Opcode.slt;
            case sgt -> this.op = Opcode.sgt;
            case sle -> this.op = Opcode.sle;
            case sge -> this.op = Opcode.sge;
            case eq -> this.op = Opcode.eq;
            case ne -> this.op = Opcode.ne;
            default -> throw new InternalException("unexpected operator in ir icmp");
        }
    }

    @Override
    public void print(PrintStream out) {
        out.println("\t" + op + "\t" + rd + ", " + rs1 + ", " + rs2);
    }

    @Override
    public void accept(ASMVisitor visitor) {
        visitor.visit(this);
    }
}