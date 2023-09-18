package asm.instruction;

import asm.ASMVisitor;
import asm.operand.PhysicalRegister;
import asm.operand.Register;

import java.io.PrintStream;
import java.util.ArrayList;

/**
 * @author F
 * 伪指令
 * 获取全局变量（字符串常量）在mem中的地址
 * 地址存到物理寄存器
 */
public class GlobalAddrInst extends ASMInstruction {
    public Register rd;
    public String name;

    public GlobalAddrInst(Register rd, String name) {
        this.rd = rd;
        this.name = name;
    }

    @Override
    public void print(PrintStream out) {
        out.println("\tlui\t" + rd + ", %hi(" + name + ")");
        out.println("\taddi\t" + rd + ", " + rd + ", %lo(" + name + ")");
    }

    @Override
    public void printRegColoring(PrintStream out) {
        out.println("\tlui\t" + rd.toRegColoringString() + ", %hi(" + name + ")");
        out.println("\taddi\t" + rd.toRegColoringString()
                + ", " + rd.toRegColoringString() + ", %lo(" + name + ")");
    }

    @Override
    public void accept(ASMVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public ArrayList<Register> getUse() {
        return null;
    }

    @Override
    public Register getDef() {
        return rd;
    }

    @Override
    public void setUse(ArrayList<Register> use) {

    }

    @Override
    public void setDef(Register def) {
        rd = def;
    }
}
