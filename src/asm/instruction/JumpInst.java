package asm.instruction;

import asm.ASMVisitor;
import asm.operand.Register;

import java.io.PrintStream;
import java.util.ArrayList;

/**
 * @author F
 * 无条件跳转指令
 */
public class JumpInst extends ASMInstruction {
    public String desName;

    public JumpInst(String desName){
        this.desName=desName;
    }
    @Override
    public void print(PrintStream out) {
        out.println("\tj\t" + desName);
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
        return null;
    }

    @Override
    public void setUse(ArrayList<Register> use) {

    }

    @Override
    public void setDef(Register def) {

    }
}
