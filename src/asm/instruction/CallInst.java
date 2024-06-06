package asm.instruction;

import asm.ASMVisitor;
import asm.operand.Register;

import java.io.PrintStream;
import java.util.ArrayList;

/**
 * @author F
 * 函数调用
 * call <label>
 */
public class CallInst extends ASMInstruction {

    public String funcName;
    public boolean hasReturn = false;
    public int paraSize = 0;

    public CallInst(String funcName, boolean hasReturn, int paraSize) {
        this.funcName = funcName;
        this.hasReturn = hasReturn;
        this.paraSize = paraSize;
    }

    @Override
    public void print(PrintStream out) {
        out.println("\tcall\t" + funcName);
    }

    @Override
    public void printRegColoring(PrintStream out) {
        out.println("\tcall\t" + funcName);
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
