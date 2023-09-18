package asm.instruction;

import asm.ASMVisitor;
import asm.operand.*;
import ir.entity.Entity;
import ir.entity.Storage;

import java.io.PrintStream;
import java.util.ArrayList;

/**
 * @author F
 * 存入内存
 * rs1待存
 * imm(rs2)存入位置
 */
public class StoreInst extends ASMInstruction {
    public Operand rs1;
    public Register rs2;
    public Imm imm;
    public boolean needPointerAddr;
    public boolean complete = false;//非完备的load

    int size;

    public StoreInst(Operand rs1,
                     Register rs2,
                     Imm imm) {
        this.rs1 = rs1;
        this.rs2 = rs2;
        this.imm = imm;
        this.size = rs1.size;
        complete = true;
    }

    public StoreInst(Operand rs1,
                     Register pointer,
                     Imm imm,
                     boolean complete,
                     boolean needPointerAddr) {
        this.rs1 = rs1;
        this.size = rs1.size;
        this.rs2 = pointer;
        this.complete = complete;
        this.needPointerAddr = needPointerAddr;
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

    @Override
    public ArrayList<Register> getUse() {
        ArrayList<Register> ret = new ArrayList<>();
        if (rs1 instanceof Register register) {
            ret.add(register);
        }
        ret.add(rs2);
        return ret;
    }

    @Override
    public Register getDef() {
        return null;
    }

    @Override
    public void setUse(ArrayList<Register> use) {
        rs1 = use.get(0);
        rs2 = use.get(1);
    }

    @Override
    public void setDef(Register def) {
    }
}
