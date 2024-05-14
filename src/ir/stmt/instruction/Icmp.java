package ir.stmt.instruction;

import ast.expr.CmpExprNode;
import ir.IRVisitor;
import ir.entity.*;
import ir.entity.constant.*;
import ir.entity.var.*;
import ir.stmt.Stmt;
import utility.Pair;
import utility.error.InternalException;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

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

    public Icmp(Cond op,
                LocalTmpVar result,
                Entity op1,
                Entity op2) {
        this.result = result;
        this.op1 = op1;
        this.op2 = op2;
        this.cond = op;
    }

    @Override
    public void print(PrintStream out) {
        String s1, s2, op = op1.type.toString();
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
        if (Objects.equals(op2.type.toString(), "i8") && Objects.equals(op, "i1")) {
            op = "i8";
        }
        out.println("\t" + result.toString()
                + " = icmp " + cond.name()
                + " " + op + " "
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
    public boolean hasDef() {
        return true;
    }

    @Override
    public Entity getDef() {
        return result;
    }

    @Override
    public void promoteGlobalVar() {
        if (op1 instanceof GlobalVar globalVar && globalVar.convertedLocalVar != null) {
            op1 = globalVar.convertedLocalVar;
        }
        if (op2 instanceof GlobalVar globalVar && globalVar.convertedLocalVar != null) {
            op2 = globalVar.convertedLocalVar;
        }
    }

    @Override
    public void propagateLocalTmpVar() {
        if (op1 instanceof Ptr ptr) {
            op1 = ptr.valueInBasicBlock == null ? op1 : ptr.valueInBasicBlock;
        } else if (op1 instanceof LocalTmpVar tmpVar) {
            op1 = tmpVar.valueInBasicBlock == null ? op1 : tmpVar.valueInBasicBlock;
        }
        if (op2 instanceof Ptr ptr) {
            op2 = ptr.valueInBasicBlock == null ? op2 : ptr.valueInBasicBlock;
        } else if (op2 instanceof LocalTmpVar tmpVar) {
            op2 = tmpVar.valueInBasicBlock == null ? op2 : tmpVar.valueInBasicBlock;
        }
    }

    @Override
    public Pair<Stmt, LocalTmpVar> creatCopy(String suffix) {
        LocalTmpVar newResult = new LocalTmpVar(result.type, result.identity + suffix);
        Stmt stmt = new Icmp(cond, newResult, op1, op2);
        return new Pair<>(stmt, newResult);
    }

    @Override
    public void replaceUse(HashMap<LocalTmpVar, Constant> constantMap) {
        op1 = replace(op1, constantMap);
        op2 = replace(op2, constantMap);
    }

    @Override
    public void replaceUse(HashMap<LocalTmpVar, Storage> copyMap, HashMap<LocalVar, LocalVar> curAllocaMap) {
        op1 = replace(op1, copyMap, curAllocaMap);
        op2 = replace(op2, copyMap, curAllocaMap);
    }

    @Override
    public Constant getConstResult() {
        return calConstResult(op1, op2, cond);
    }

    //两个操作数都为常数，直接传播
    public static ConstBool calConstResult(Entity operand1, Entity operand2, Cond condition) {
        ConstBool ret = null;
        if (operand1 instanceof ConstInt const1 && operand2 instanceof ConstInt const2) {
            boolean result;
            int num1 = Integer.parseInt(const1.value);
            int num2 = Integer.parseInt(const2.value);
            switch (condition) {
                case eq -> result = num1 == num2;
                case ne -> result = num1 != num2;
                case slt -> result = num1 < num2;
                case sgt -> result = num1 > num2;
                case sle -> result = num1 <= num2;
                case sge -> result = num1 >= num2;
                default -> throw new InternalException("unexpected cond in Icmp instruction");
            }
            ret = new ConstBool(result);
        } else if (operand1 instanceof ConstBool const1 && operand2 instanceof ConstBool const2) {
            boolean result;
            switch (condition) {
                case eq -> result = const1.value == const2.value;
                case ne -> result = const1.value != const2.value;
                default -> throw new InternalException("unexpected cond in Icmp instruction");
            }
            ret = new ConstBool(result);
        }
        return ret;
    }
}
