package asm.instruction;

import asm.ASMVisitor;
import asm.operand.Register;

import java.io.PrintStream;

/**
 * @author F
 * 条件跳转
 * 全为bnez（true）
 */
public class BranchInst extends ASMInstruction {

    public Register rs1;
    public String desName;

    public BranchInst(Register rs1, String desName) {
        this.rs1 = rs1;
        this.desName = desName;
    }

    @Override
    public void print(PrintStream out) {
        out.println("\tbnez\t" + rs1 + ", " + desName);
    }

    @Override
    public void accept(ASMVisitor visitor) {
        visitor.visit(this);
    }
}
