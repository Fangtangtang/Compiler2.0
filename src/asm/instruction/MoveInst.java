package asm.instruction;

import asm.ASMVisitor;
import asm.operand.Register;

import java.io.PrintStream;
import java.util.ArrayList;

/**
 * @author F
 * 寄存器赋值
 * mv	a0, sp
 * 用于call传参
 * 注意避免使用a_，与参数传递用到的reg冲突
 */
public class MoveInst extends ASMInstruction {
    public Register rd;
    public Register rs1;

    public MoveInst(Register rd, Register rs1) {
        this.rd = rd;
        this.rs1 = rs1;
        rd.size = rs1.size;
    }

    @Override
    public void print(PrintStream out) {
        if (rd.equals(rs1)) {
            return;
        }
        out.println("\tmv\t" + rd + ", " + rs1);
    }

    @Override
    public void printRegColoring(PrintStream out) {
        if (rd.equals(rs1)) {
            return;
        }
        out.println("\tmv\t" + rd.toRegColoringString() + ", " + rs1.toRegColoringString());
    }

    @Override
    public void accept(ASMVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public ArrayList<Register> getUse() {
        ArrayList<Register> ret = new ArrayList<>();
        ret.add(rs1);
        return ret;
    }

    @Override
    public Register getDef() {
        return rd;
    }

    @Override
    public void setUse(ArrayList<Register> use) {
        rs1 = use.get(0);
    }

    @Override
    public void setDef(Register def) {
        rd = def;
    }
}
