package asm.instruction;

import asm.ASMVisitor;
import asm.operand.Register;
import ir.stmt.instruction.Icmp;

import java.io.PrintStream;

/**
 * @author F
 * 是否==0
 */
public class EqualZeroInst extends ASMInstruction {

    enum Opcode {
        seqz, snez
    }

    public Register rs1;
    public Register rd;
    public EqualZeroInst.Opcode op;

    public EqualZeroInst(Register rs1,
                         Register rd,
                         Icmp.Cond operator) {
        this.rs1 = rs1;
        this.rd = rd;
        if (operator.equals(Icmp.Cond.eq)) {
            op = Opcode.seqz;
        } else {
            op = Opcode.snez;
        }
    }

    public EqualZeroInst(Register rs1,
                         Register rd,
                         Opcode operator) {
        this.rs1 = rs1;
        this.rd = rd;
        op = operator;
    }

    @Override
    public void print(PrintStream out) {
        out.println("\t" + op + "\t" + rd + ", " + rs1);
    }

    @Override
    public void accept(ASMVisitor visitor) {
        visitor.visit(this);
    }
}
