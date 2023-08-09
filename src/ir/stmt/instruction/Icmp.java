package ir.stmt.instruction;

import ast.expr.CmpExprNode;
import ir.IRVisitor;
import ir.entity.*;
import ir.entity.register.*;
import utility.error.InternalException;

/**
 * @author F
 * 二元大小比较
 * <result> = icmp <cond> <ty> <op1>, <op2>
 */
public class Icmp extends Instruction {
    public enum Cond {
        slt, sgt, sle, sge,
        eq, ne
    }

    public Register resultReg;
    public Entity op1, op2;
    public Cond cond;

    public Icmp(CmpExprNode.CmpOperator operator,
                Register resultReg,
                Entity op1,
                Entity op2) {
        this.resultReg = resultReg;
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
    public void print() {
        System.out.println(resultReg.toString() + " = icmp " + cond.name() + " "
                + op1.type.toString() + ' ' + op1.toString() + ", " + op2.toString());
    }

    @Override
    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }
}
