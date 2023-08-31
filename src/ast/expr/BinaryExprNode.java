package ast.expr;

import ast.ASTVisitor;
import utility.Position;


/**
 * @author F
 * 二元表达式
 * 表达式类型由lhs、rhs决定
 * 若两表达式类型不相符，抛出异常
 * 运算结果不可赋值
 */
public class BinaryExprNode extends ExprNode {
    public enum BinaryOperator {
        Multiply, Divide, Mod, Plus, Minus,
        LeftShift, RightShift,
        And, Xor, Or
    }

    public ExprNode lhs, rhs;
    public BinaryOperator operator;

    public BinaryExprNode(Position pos,
                          ExprNode lhs,
                          ExprNode rhs,
                          BinaryOperator operator) {
        super(pos);
        this.lhs = lhs;
        this.rhs = rhs;
        this.operator = operator;
    }

    @Override
    public <T> T accept(ASTVisitor<? extends T> visitor) {
        return visitor.visit(this);
    }
}
