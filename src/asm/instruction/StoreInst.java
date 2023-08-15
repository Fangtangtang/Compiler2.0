package asm.instruction;

import asm.ASMVisitor;
import asm.operand.Imm;
import asm.operand.Register;

import java.io.PrintStream;

/**
 * @author F
 * 存入内存
 */
public class StoreInst extends ASMInstruction {
    public Register re1, rs2;
    public Imm imm;

    @Override
    public void print(PrintStream out) {

    }

    @Override
    public void accept(ASMVisitor visitor) {

    }
}
