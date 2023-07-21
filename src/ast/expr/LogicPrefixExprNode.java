package ast.expr;

import ast.ASTVisitor;
import ast.ExprNode;
import utility.Position;
import utility.type.Type;

/**
 * @author F
 * 逻辑运算前缀表达式
 */
public class LogicPrefixExprNode extends ExprNode {
    enum LogicPrefixOperator {
        LogicNot
    }

    public ExprNode expression;
    public LogicPrefixOperator operator;

    public LogicPrefixExprNode(Position pos,
                               Type exprType,
                               ExprNode expression,
                               LogicPrefixOperator operator) {
        super(pos);
        this.exprType = exprType;
        this.expression = expression;
        this.operator = operator;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
