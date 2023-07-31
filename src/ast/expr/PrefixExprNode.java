package ast.expr;

import ast.ASTVisitor;
import ast.ExprNode;
import utility.Position;
import utility.type.Type;

/**
 * @author F
 * 非逻辑运算的前缀表达式
 */
public class PrefixExprNode extends ExprNode {

    public enum PrefixOperator {
        PlusPlus, MinusMinus, Not, Minus
    }

    public ExprNode expression;
    public PrefixOperator operator;

    public PrefixExprNode(Position pos,
                          ExprNode expression,
                          PrefixOperator operator) {
        super(pos);
        this.exprType = expression.exprType;
        this.expression = expression;
        this.operator = operator;
        if (operator == PrefixOperator.PlusPlus || operator == PrefixOperator.MinusMinus) {
            this.isAssignable = true;
        }
    }

    @Override
    public <T> T accept(ASTVisitor<? extends T> visitor) {
        return visitor.visit(this);
    }
}
