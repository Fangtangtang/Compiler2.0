package asm.instruction;

import asm.ASMVisitor;
import asm.operand.PhysicalRegister;

import java.io.PrintStream;

/**
 * @author F
 * 加载全局变量的伪指令
 * ----------------------------------
 * lui	a0, %hi(a)
 * lw	a0, %lo(a)(a0)
 */
public class LoadGlobalInst extends ASMInstruction {
    public PhysicalRegister rd;
    public String rs1Name;
    int size;

    public LoadGlobalInst(PhysicalRegister rd,
                          String rs1Name) {
        this.rd = rd;
        this.rs1Name = rs1Name;
        this.size = rd.valueSize;
    }

    @Override
    public void print(PrintStream out) {
        out.println("\tlui\t" + rd + ", %hi(" + rs1Name + ")");
        if (size == 4) {
            out.println("\tlw\t" + rd + ", %lo(" + rs1Name + ")(" + rd + ")");
        } else {
            out.println("\tlb\t" + rd + ", %lo(" + rs1Name + ")(" + rd + ")");
        }
    }

    @Override
    public void accept(ASMVisitor visitor) {
        visitor.visit(this);
    }
}
