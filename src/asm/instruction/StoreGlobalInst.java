package asm.instruction;

import asm.ASMVisitor;
import asm.operand.PhysicalRegister;

import java.io.PrintStream;

/**
 * @author F
 * 伪指令 向全局变量存值
 * ---------------------------
 * lui	a1, %hi(a)
 * sw	a0, %lo(a)(a1)
 * --------------------------
 */
public class StoreGlobalInst extends ASMInstruction {
    public PhysicalRegister rs1;//a0
    public PhysicalRegister tmp;//a1
    public String rdName;
    int size;

    public StoreGlobalInst(PhysicalRegister rs1,
                           PhysicalRegister tmp,
                           String rdName) {
        this.rs1 = rs1;
        this.tmp = tmp;
        this.rdName = rdName;
        this.size = rs1.valueSize;
    }

    @Override
    public void print(PrintStream out) {
        out.println("\tlui\t" + tmp + ", %hi(" + rdName + ")");
        if (size == 4) {
            out.println("\tlw\t" + rs1 + ", %lo(" + rdName + ")(" + tmp + ")");
        } else {
            out.println("\tlb\t" + rs1 + ", %lo(" + rdName + ")(" + tmp + ")");
        }
    }

    @Override
    public void accept(ASMVisitor visitor) {
        visitor.visit(this);
    }
}
