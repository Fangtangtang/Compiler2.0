package ir.stmt.instruction;

import ast.expr.BinaryExprNode;
import ir.IRVisitor;
import ir.entity.*;
import ir.entity.constant.*;
import ir.entity.var.*;
import ir.stmt.Stmt;
import utility.Pair;
import utility.error.InternalException;

import java.io.PrintStream;
import java.util.ArrayList;

/**
 * @author F
 * 二元运算指令
 * <result> = <operator> <type> <operand1>, <operand2>
 * +------------------------------------------
 * |
 * |    int a=b+1;  ->  %0 = load i32, ptr %b
 * |                    %add = add i32 %0, 1
 * |                    store i32 %add, ptr %a
 * |
 * + --------------------------------------------
 */
public class Binary extends Instruction {

    public enum Operator {
        add, sub, mul, sdiv, srem,
        shl, ashr,
        and, xor, or
    }

    public LocalTmpVar result;
    public SSAEntity ssaResult;
    public Entity op1, op2;
    public SSAEntity ssaOp1, ssaOp2;
    public Operator operator;

    public Binary(BinaryExprNode.BinaryOperator operator,
                  LocalTmpVar result,
                  Entity op1,
                  Entity op2) {
        this.result = result;
        this.op1 = op1;
        this.op2 = op2;
        switch (operator) {
            case Plus -> this.operator = Operator.add;
            case Minus -> this.operator = Operator.sub;
            case Multiply -> this.operator = Operator.mul;
            case Divide -> this.operator = Operator.sdiv;
            case Mod -> this.operator = Operator.srem;
            case LeftShift -> this.operator = Operator.shl;
            case RightShift -> this.operator = Operator.ashr;
            case And -> this.operator = Operator.and;
            case Xor -> this.operator = Operator.xor;
            case Or -> this.operator = Operator.or;
            default -> throw new InternalException("unexpected operator in Binary instruction");
        }
    }

    @Override
    public void print(PrintStream out) {
        String s1, s2;
        if (op1 instanceof ConstInt constant) {
            s1 = constant.printValue();
        } else {
            s1 = op1.toString();
        }
        if (op2 instanceof ConstInt constant) {
            s2 = constant.printValue();
        } else {
            s2 = op2.toString();
        }
        out.println("\t" + result.toString()
                + " = " + operator.name()
                + " " + result.type.toString() + ' '
                + s1 + ", " + s2
        );
    }

    @Override
    public void printSSA(PrintStream out) {
        String s1, s2;
        if (op1 instanceof ConstInt constant) {
            s1 = constant.printValue();
        } else {
            s1 = ssaOp1.toString();
        }
        if (op2 instanceof ConstInt constant) {
            s2 = constant.printValue();
        } else {
            s2 = ssaOp2.toString();
        }
        out.println("\t" + ssaResult.toString()
                + " = " + operator.name()
                + " " + result.type.toString() + ' '
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
    public Pair<Stmt, LocalTmpVar> creatCopy(ArrayList<Entity> newUse, String suffix) {

    }

    @Override
    public Constant getConstResult() {
        Constant ret = null;
        if (op1 instanceof ConstInt const1 && op2 instanceof ConstInt const2) {
            int result;
            int num1 = Integer.parseInt(const1.value);
            int num2 = Integer.parseInt(const2.value);
            switch (operator) {
                case add -> result = num1 + num2;
                case sub -> result = num1 - num2;
                case mul -> result = num1 * num2;
                case sdiv -> result = num1 / num2;
                case srem -> result = num1 % num2;
                case shl -> result = num1 << num2;
                case ashr -> result = num1 >> num2;
                case and -> result = num1 & num2;
                case xor -> result = num1 ^ num2;
                case or -> result = num1 | num2;
                default -> throw new InternalException("unexpected cond in Binary instruction");
            }
            ret = new ConstInt(Integer.toString(result));
        } else if (op1 instanceof ConstBool const1 && op2 instanceof ConstBool const2) {
            boolean result;
            switch (operator) {
                case and -> result = const1.value & const2.value;
                case xor -> result = const1.value ^ const2.value;
                case or -> result = const1.value | const2.value;
                default -> throw new InternalException("unexpected cond in Binary instruction");
            }
            ret = new ConstBool(result);
        }
        return ret;
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
