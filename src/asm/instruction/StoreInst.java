package asm.instruction;

import asm.ASMVisitor;
import asm.operand.*;

import java.io.PrintStream;

/**
 * @author F
 * 存入内存
 * rs1待存
 * imm(rs2)存入位置
 * TODO:判断sb\sw
 */
public class StoreInst extends ASMInstruction {
    public Register rs1, rs2;
    public Imm imm;

    public StoreInst(Register rs1, Register rs2,
                     Imm imm) {
        this.rs1 = rs1;
        this.rs2 = rs2;
        this.imm = imm;
    }

    @Override
    public void print(PrintStream out) {
        out.println("\tsw\t" + rs1 + ", " + imm + "(" + rs2 + ")");
    }

    @Override
    public void accept(ASMVisitor visitor) {
        visitor.visit(this);
    }
}
