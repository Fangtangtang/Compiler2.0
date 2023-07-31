package ast.expr;

import ast.ASTVisitor;
import ast.ExprNode;
import utility.Position;
import utility.type.BoolType;
import utility.type.Type;

/**
 * @author F
 * 逻辑运算前缀表达式
 * 运算结果不可赋值
 */
public class LogicPrefixExprNode extends ExprNode {
    public enum LogicPrefixOperator {
        LogicNot
    }

    public ExprNode expression;
    public LogicPrefixOperator operator;

    public LogicPrefixExprNode(Position pos,
                               ExprNode expression,
                               LogicPrefixOperator operator) {
        super(pos);
        this.exprType = new BoolType();
        this.expression = expression;
        this.operator = operator;
    }

    @Override
    public <T> T accept(ASTVisitor<? extends T> visitor) {
        return visitor.visit(this);
    }
}
