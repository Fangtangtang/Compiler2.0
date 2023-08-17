package asm.instruction;

import asm.ASMVisitor;
import asm.operand.*;
import ir.stmt.instruction.Binary;
import utility.error.InternalException;

import java.io.PrintStream;

/**
 * @author F
 * 立即数二元运算操作
 * TODO：如果遍历到IR上二元运算可以直接出结果，直接计算掉
 * TODO：减去常数转为加上相反数
 */
public class ImmBinaryInst extends ASMInstruction {
    enum Opcode {
        addi, slli, xori, srli, ori, andi
    }

    public Imm imm;
    public Register rs1, rd;
    public Opcode op;

    public ImmBinaryInst(Register rs1,
                         Imm imm,
                         Register rd,
                         Binary.Operator operator) {
        this.rs1 = rs1;
        this.imm = imm;
        this.rd = rd;
        switch (operator) {
            case add -> this.op = ImmBinaryInst.Opcode.addi;
            case shl -> this.op = ImmBinaryInst.Opcode.slli;
            case ashr -> this.op = ImmBinaryInst.Opcode.srli;
            case and -> this.op = ImmBinaryInst.Opcode.andi;
            case xor -> this.op = ImmBinaryInst.Opcode.xori;
            case or -> this.op = ImmBinaryInst.Opcode.ori;
            default -> throw new InternalException("unexpected binary operator in ir");
        }
    }

    @Override
    public void print(PrintStream out) {
        out.println("\t" + op.name() + "\t" + rd + ", " + rs1 + ", " + imm);
    }

    @Override
    public void accept(ASMVisitor visitor) {
        visitor.visit(this);
    }
}
