package asm.instruction;

import asm.ASMVisitor;
import asm.operand.*;

import java.io.PrintStream;

/**
 * @author F
 * 立即数二元运算操作
 * TODO：如果遍历到IR上二元运算可以直接出结果，直接计算掉
 * TODO：减去常数转为加上相反数
 */
public class ImmBinaryInst extends ASMInstruction {
    enum Opcode {
        addi, slli, xori, srli, srai, ori, andi
    }

    public Imm imm;
    public Register rs1, rd;
    public Opcode op;

    @Override
    public void print(PrintStream out) {

    }

    @Override
    public void accept(ASMVisitor visitor) {

    }
}
