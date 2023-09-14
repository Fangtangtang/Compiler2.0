package asm.instruction;

import asm.ASMVisitor;
import asm.operand.*;

import java.io.PrintStream;

/**
 * @author F
 * 加载
 * -
 * lbu：1byte 0拓展
 * lbu	a1, -10(s0)
 * -
 * lw：4byte
 * lw	a0, -12(s0)
 */
public class LoadInst extends ASMInstruction {
    public Register rs1;
    public Register rd;
    public Imm imm;
    //true:get addr
    //false:get value
    public boolean needResultAddr, needPointerAddr;
    public boolean complete = false;//非完备的load
    int size;

    //pointer为基地址
    //rd为physical reg
    public LoadInst(Register rs1,
                    Register rd,
                    Imm imm) {
        this.rs1 = rs1;
        this.rd = rd;
        this.imm = imm;
        this.size = rd.size;
        complete = true;
    }

    //pointer为基地址
    //result地址已知，向地址load
    public LoadInst(Register rs1,
                    Register rd,
                    Imm imm,
                    boolean complete,
                    boolean needPointerAddr,
                    boolean needResultAddr) {
        this.rs1 = rs1;
        this.rd = rd;
        this.imm = imm;
        if (complete) {
            this.size = rd.size;
        }
        this.needResultAddr = needResultAddr;
        this.needPointerAddr = needPointerAddr;
        this.complete = complete;
    }


    @Override
    public void print(PrintStream out) {
        if (size == 4) {
            out.println("\tlw\t" + rd + ", " + imm + "(" + rs1 + ")");
        } else {
            out.println("\tlbu\t" + rd + ", " + imm + "(" + rs1 + ")");
        }
    }

    @Override
    public void accept(ASMVisitor visitor) {
        visitor.visit(this);
    }
}
