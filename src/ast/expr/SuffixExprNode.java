package ast.expr;

import ast.ASTVisitor;
import ast.ExprNode;
import utility.Position;
import utility.type.Type;

import java.rmi.server.ExportException;

/**
 * @author F
 * 后缀表达式
 * 不可赋值
 */
public class SuffixExprNode extends ExprNode {

    public enum SuffixOperator {
        PlusPlus, MinusMinus
    }

    public ExprNode expression;
    public SuffixOperator operator;

    public SuffixExprNode(Position pos,
                          ExprNode expression,
                          SuffixOperator operator) {
        super(pos);
        this.expression = expression;
        this.operator = operator;
        this.exprType = expression.exprType;
    }

    @Override
    public <T> T accept(ASTVisitor<? extends T> visitor) {
        return visitor.visit(this);
    }
}
