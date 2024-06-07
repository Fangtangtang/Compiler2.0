package asm.instruction;

import asm.ASMVisitor;
import asm.operand.PhysicalRegister;
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
    public boolean hasReturn;

    // `sp` used but not in the paraList
    public ArrayList<Register> paraList;
    PhysicalRegister retValue;

    public CallInst(String funcName, boolean hasReturn,
                    ArrayList<Register> paraList,
                    PhysicalRegister retValue) {
        this.funcName = funcName;
        this.hasReturn = hasReturn;
        this.paraList = paraList;
        this.aliveByNature = true;
        this.retValue = retValue;
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
        return paraList;
    }

    @Override
    public Register getDef() {
        return retValue;
    }

    @Override
    public void setUse(ArrayList<Register> use) {
        // todo??
    }

    @Override
    public void setDef(Register def) {
        // todo??
    }
}
