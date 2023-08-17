package ir.stmt.instruction;

import ir.IRVisitor;
import ir.entity.constant.ConstString;
import ir.entity.var.GlobalVar;

import java.io.PrintStream;

/**
 * @author F
 * 字符串字面量
 * 放在全局
 */
public class GlobalStr extends Instruction {
    public GlobalVar result;

    public GlobalStr(GlobalVar result) {
        this.result = result;
    }

    //@.str = constant [6 x i8] c"123er\00",
    @Override
    public void print(PrintStream out) {
        ConstString str = (ConstString) result.storage;
        if (str.value.length() > 0) {
            out.println(result.toString() + " = constant " +
                    "[" + (str.value.length() + 1) + " x i8] c" +
                    "\"" + str.value + "\\00\""
            );
        }else {
            out.println(result.toString() + " = constant " +
                    "[1 x i8] zeroinitializer"
            );
        }
    }

    @Override
    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }
}
