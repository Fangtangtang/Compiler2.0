package ir.stmt.instruction;

import ir.IRVisitor;
import ir.entity.Storage;
import ir.entity.var.LocalTmpVar;

import java.io.PrintStream;

/**
 * @author F
 * 通过跳转来源给变量赋值
 * 暂存前驱基本块，用于后继判断来源
 * <result> = phi <ty> [ <val0>, <label0>], ...
 * +------------------------------------------------
 * |
 * |    a==1?a:1;
 * |    %cond = phi i32 [ %1, %cond.true ], [ 1, %cond.false ]
 * |
 * +------------------------------------------------
 */
public class Phi extends Instruction {
    public LocalTmpVar result;

    public Storage ans1, ans2;
    public String label1, label2;

    public Phi(LocalTmpVar result,
               Storage ans1,
               Storage ans2,
               String label1,
               String label2) {
        this.result = result;
        this.ans1 = ans1;
        this.ans2 = ans2;
        this.label1 = label1;
        this.label2 = label2;
    }

    @Override
    public void print(PrintStream out) {
        out.println(
                "\t" + result.toString() + " = phi " + ans1.type
                        + " [ " + ans1.toString() + " %" + label1 + " ],"
                        + " [ " + ans2.toString() + " %" + label2 + " ]"
        );
    }

    @Override
    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }
}
