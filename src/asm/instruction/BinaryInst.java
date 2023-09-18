package asm.instruction;

import asm.ASMVisitor;
import asm.operand.Operand;
import asm.operand.Register;
import ir.stmt.instruction.Binary;
import utility.error.InternalException;

import java.io.PrintStream;
import java.util.ArrayList;

/**
 * @author F
 * 二元运算指令,rs1,rs2版本
 * - 乘除法没有立即数版本
 */
public class BinaryInst extends ASMInstruction {
    public enum Opcode {
        add, sub, mul, div, rem,
        sll, xor, srl, or, and
    }

    public Operand rs1, rs2;
    public Register rd;

    public Opcode op;

    //根据IR的Binary构建
    public BinaryInst(Operand rs1, Operand rs2,
                      Register rd,
                      Binary.Operator operator) {
        this.rs1 = rs1;
        this.rs2 = rs2;
        this.rd = rd;
        switch (operator) {
            case add -> this.op = Opcode.add;
            case sub -> this.op = Opcode.sub;
            case mul -> this.op = Opcode.mul;
            case sdiv -> this.op = Opcode.div;
            case srem -> this.op = Opcode.rem;
            case shl -> this.op = Opcode.sll;
            case ashr -> this.op = Opcode.srl;
            case and -> this.op = Opcode.and;
            case xor -> this.op = Opcode.xor;
            case or -> this.op = Opcode.or;
            default -> throw new InternalException("unexpected binary operator in ir");
        }
    }

    public BinaryInst(Operand rs1, Operand rs2,
                      Register rd,
                      Opcode op) {
        this.rs1 = rs1;
        this.rs2 = rs2;
        this.rd = rd;
        this.op = op;
    }

    @Override
    public void print(PrintStream out) {
        out.println("\t" + op.name() + "\t" + rd + ", " + rs1 + ", " + rs2);
    }

    @Override
    public void printRegColoring(PrintStream out) {
        out.println("\t" + op.name() + "\t" + rd.toRegColoringString()
                + ", " + rs1.toRegColoringString() + ", " + rs2.toRegColoringString());
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
        if (rs2 instanceof Register register) {
            ret.add(register);
        }
        return ret;
    }

    @Override
    public Register getDef() {
        return rd;
    }

    @Override
    public void setUse(ArrayList<Register> use) {
        rs1 = use.get(0);
        rs2 = use.get(1);
    }

    @Override
    public void setDef(Register def) {
        rd = def;
    }
}
