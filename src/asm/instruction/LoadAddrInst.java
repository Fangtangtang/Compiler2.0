package asm.instruction;

import asm.ASMVisitor;
import asm.operand.Register;

import java.io.PrintStream;
import java.time.Period;

/**
 * @author F
 * 加载寄存器地址
 * （取offset的基地址）
 * ------------------------------------------------
 * la t0, myArray    # 加载基地址到寄存器 t0
 */
public class LoadAddrInst extends ASMInstruction {
    public Register rs1;
    public Register rd;

    public LoadAddrInst(Register rd, Register rs1) {
        this.rs1 = rs1;
        this.rd = rd;
    }

    @Override
    public void print(PrintStream out) {
        out.println("\tla\t" + rd + ", " + rs1);
    }

    @Override
    public void accept(ASMVisitor visitor) {
        visitor.visit(this);
    }
}
