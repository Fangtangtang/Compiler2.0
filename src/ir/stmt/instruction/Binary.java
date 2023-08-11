package ir.stmt.instruction;

import ast.expr.BinaryExprNode;
import ir.IRVisitor;
import ir.entity.*;
import ir.entity.var.*;
import utility.error.InternalException;

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
 * TODO:小心数位 xor
 */
public class Binary extends Instruction {
    public enum Operator {
        add, sub, mul, sdiv, srem,
        shl, ashr,
        and, xor, or
    }

    public LocalTmpVar result;
    public Entity op1, op2;
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
    public void print() {
        System.out.println(result.toString()
                + " = " + operator.name()
                + " " + result.toString() + ' '
                + op1.toString() + ", " + op2.toString());
    }

    @Override
    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }
}
