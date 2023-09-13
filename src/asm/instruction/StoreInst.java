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
    public Operand rs1;
    public Register rs2;
    public Imm imm;
    int size;

    public StoreInst(Operand rs1, Register rs2,
                     Imm imm) {
        this.rs1 = rs1;
        this.rs2 = rs2;
        this.imm = imm;
        this.size = rs1.size;

    }

    @Override
    public void print(PrintStream out) {
        if (size == 4) {
            out.println("\tsw\t" + rs1 + ", " + imm + "(" + rs2 + ")");
        } else {
            out.println("\tsb\t" + rs1 + ", " + imm + "(" + rs2 + ")");
        }
    }

    @Override
    public void accept(ASMVisitor visitor) {
        visitor.visit(this);
    }
}
