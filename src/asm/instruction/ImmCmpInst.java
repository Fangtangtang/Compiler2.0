package asm.instruction;

import asm.ASMVisitor;
import asm.operand.Imm;
import asm.operand.Register;
import ir.stmt.instruction.Icmp;
import utility.error.InternalException;

import java.io.PrintStream;

/**
 * @author F
 * 和立即数比较
 */
public class ImmCmpInst extends ASMInstruction {
    enum Opcode {
        slti, sqti, slei, sqei,
        eqi, nei
    }

    public Register rs1;
    public Imm imm;
    public Register rd;
    public ImmCmpInst.Opcode op;

    public ImmCmpInst(Register rs1,
                      Imm imm,
                      Register rd,
                      Icmp.Cond operator) {
        this.rs1 = rs1;
        this.imm = imm;
        this.rd = rd;
        switch (operator) {
            case slt -> this.op = ImmCmpInst.Opcode.slti;
            case sgt -> this.op = ImmCmpInst.Opcode.sqti;
            case sle -> this.op = ImmCmpInst.Opcode.slei;
            case sge -> this.op = ImmCmpInst.Opcode.sqei;
            case eq -> this.op = ImmCmpInst.Opcode.eqi;
            case ne -> this.op = ImmCmpInst.Opcode.nei;
            default -> throw new InternalException("unexpected operator in ir icmp");
        }
    }

    @Override
    public void print(PrintStream out) {
        out.println("\t" + op + "\t" + rd + ", " + rs1 + ", " + imm);
    }

    @Override
    public void accept(ASMVisitor visitor) {
        visitor.visit(this);
    }
}
