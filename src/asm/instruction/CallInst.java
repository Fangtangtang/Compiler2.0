package asm.instruction;

import asm.ASMVisitor;

import java.io.PrintStream;

/**
 * @author F
 * 函数调用
 * call <label>
 */
public class CallInst extends ASMInstruction {

    public String funcName;

    public CallInst(String funcName) {
        this.funcName = funcName;
    }

    @Override
    public void print(PrintStream out) {
        out.println("\tcall\t" + funcName);
    }

    @Override
    public void accept(ASMVisitor visitor) {
        visitor.visit(this);
    }
}
