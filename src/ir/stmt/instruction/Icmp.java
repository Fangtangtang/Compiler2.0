package ir.stmt.instruction;

import ast.expr.CmpExprNode;
import ir.IRVisitor;
import ir.entity.*;
import ir.entity.var.*;
import utility.error.InternalException;

import java.io.PrintStream;

/**
 * @author F
 * 二元大小比较
 * <result> = icmp <cond> <ty> <op1>, <op2>
 * +--------------------------------------
 * |
 * |    bool c=a==b;    ->  %0 = load i32, ptr %a, align 4
 * |                        %1 = load i32, ptr %b, align 4
 * |                        %cmp = icmp eq i32 %0, %1
 * |                        %frombool = zext i1 %cmp to i8
 * |                        store i8 %frombool, ptr %c, align 1
 * |
 * + -----------------------------------------
 */
public class Icmp extends Instruction {
    public enum Cond {
        slt, sgt, sle, sge,
        eq, ne
    }

    public LocalTmpVar result;
    public Entity op1, op2;
    public Cond cond;

    public Icmp(CmpExprNode.CmpOperator operator,
                LocalTmpVar result,
                Entity op1,
                Entity op2) {
        this.result = result;
        this.op1 = op1;
        this.op2 = op2;
        switch (operator) {
            case Less -> this.cond = Cond.slt;
            case Greater -> this.cond = Cond.sgt;
            case LessEqual -> this.cond = Cond.sle;
            case GreaterEqual -> this.cond = Cond.sge;
            case Equal -> this.cond = Cond.eq;
            case NotEqual -> this.cond = Cond.ne;
            default -> throw new InternalException("unexpected operator in Icmp instruction");

        }
    }

    @Override
    public void print(PrintStream out) {
        out.println("\t" + result.toString()
                + " = icmp " + cond.name()
                + " " + op1.toString() + ", " + op2.toString());

    }

    @Override
    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }
}
