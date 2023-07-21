package ast.expr;

import ast.ASTVisitor;
import ast.ExprNode;
import utility.Position;
import utility.type.Type;

import java.rmi.server.ExportException;

/**
 * @author F
 * 后缀表达式
 */
public class SuffixExprNode extends ExprNode {

    public enum SuffixOperator {
        PlusPlus, MinusMinus
    }

    public ExprNode expression;
    public SuffixOperator operator;

    public SuffixExprNode(Position pos,
                          Type exprType,
                          ExprNode expression,
                          SuffixOperator operator) {
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
