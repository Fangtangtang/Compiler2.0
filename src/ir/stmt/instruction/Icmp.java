package ir.stmt.instruction;

import ast.expr.CmpExprNode;
import ir.IRVisitor;
import ir.entity.*;
import ir.entity.constant.*;
import ir.entity.var.*;
import utility.error.InternalException;

import java.io.PrintStream;
import java.util.ArrayList;

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
    public SSAEntity ssaResult;
    public Entity op1, op2;
    public SSAEntity ssaOp1, ssaOp2;
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
        String s1, s2;
        if (op1 instanceof ConstInt constant) {
            s1 = constant.printValue();
        } else if (op1 instanceof Null) {
            s1 = "null";
        } else {
            s1 = op1.toString();
        }
        if (op2 instanceof ConstInt constant) {
            s2 = constant.printValue();
        } else if (op2 instanceof Null) {
            s2 = "null";
        } else {
            s2 = op2.toString();
        }
        out.println("\t" + result.toString()
                + " = icmp " + cond.name()
                + " " + op1.type + " "
                + s1 + ", " + s2
        );
    }

    @Override
    public void printSSA(PrintStream out) {
        String s1, s2;
        if (op1 instanceof ConstInt constant) {
            s1 = constant.printValue();
        } else if (op1 instanceof Null) {
            s1 = "null";
        } else {
            s1 = ssaOp1.toString();
        }
        if (op2 instanceof ConstInt constant) {
            s2 = constant.printValue();
        } else if (op2 instanceof Null) {
            s2 = "null";
        } else {
            s2 = ssaOp2.toString();
        }
        out.println("\t" + ssaResult.toString()
                + " = icmp " + cond.name()
                + " " + op1.type + " "
                + s1 + ", " + s2
        );
    }

    @Override
    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }

    @Override
    public ArrayList<Entity> getUse() {
        ArrayList<Entity> ret = new ArrayList<>();
        ret.add(op1);
        ret.add(op2);
        return ret;
    }

    @Override
    public Entity getDef() {
        return result;
    }

    @Override
    public void setUse(ArrayList<SSAEntity> list) {
        ssaOp1 = list.get(0);
        ssaOp2 = list.get(1);
    }

    @Override
    public void setDef(SSAEntity entity) {
        ssaResult = entity;
    }

    @Override
    public ArrayList<SSAEntity> getSSAUse() {
        ArrayList<SSAEntity> ret = new ArrayList<>();
        ret.add(ssaOp1);
        ret.add(ssaOp2);
        return ret;
    }

    @Override
    public SSAEntity getSSADef() {
        return ssaResult;
    }
}
