package asm.instruction;

import asm.ASMVisitor;
import asm.operand.Imm;
import asm.operand.Register;

import java.io.PrintStream;

/**
 * @author F
 * 和立即数比较
 */
public class ImmCmpInst extends ASMInstruction {
    enum Opcode {
        slti, sgti, slei, sgei,
        eqi, nei
    }

    public Register rs1;
    public Imm imm;
    public Register rd;
    public CmpInst.Opcode op;

    @Override
    public void print(PrintStream out) {

    }

    @Override
    public void accept(ASMVisitor visitor) {

    }
}
