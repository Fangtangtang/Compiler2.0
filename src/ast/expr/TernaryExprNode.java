package ast.expr;

import ast.ASTVisitor;
import ast.ExprNode;
import utility.Position;

/**
 * @author F
 * 三目表达式
 */
public class TernaryExprNode extends ExprNode {
    public ExprNode condition;
    public ExprNode trueExpr;
    public ExprNode falseExpr;

    public TernaryExprNode(Position pos,
                           ExprNode condition,
                           ExprNode trueExpr,
                           ExprNode falseExpr) {
        super(pos);
        this.condition = condition;
        this.trueExpr = trueExpr;
        this.falseExpr = falseExpr;
    }


    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
