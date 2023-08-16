package ir.stmt.instruction;


import ir.IRVisitor;
import ir.entity.Storage;
import ir.entity.var.LocalTmpVar;

import java.io.PrintStream;

/**
 * @author F
 * trunc逆操作
 * zero extends its operand to type ty2
 * <result> = zext <ty> <value> to <ty2>
 * +-----------------------------------
 * |
 * |    %frombool = zext i1 %lnot to i8
 * |    store i8 %frombool, ptr %a, align 1
 * |
 * +----------------------------------------
 */
public class Zext extends Instruction {

    public LocalTmpVar result;
    public Storage value;

    public Zext(LocalTmpVar result,
                Storage value) {
        this.result = result;
        this.value = value;
    }

    @Override
    public void print(PrintStream out) {
        out.println(
                "\t" + result.toString() + " = zext "
                        + value.type + " " + value.toString()
                        + " to " + result.type
        );
    }

    @Override
    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }
}
