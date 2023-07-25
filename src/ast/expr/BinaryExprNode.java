package ast.expr;

import ast.ASTVisitor;
import ast.ExprNode;
import utility.Position;
import utility.error.*;
import utility.type.Type;


/**
 * @author F
 * 二元表达式
 * 表达式类型由lhs、rhs决定
 * 若两表达式类型不相符，抛出异常
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
        if (lhs.exprType != rhs.exprType) {
            throw new SemanticException(pos, "exprType exception");
        }
        this.lhs = lhs;
        this.rhs = rhs;
        this.operator = operator;
        this.exprType = lhs.exprType;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
