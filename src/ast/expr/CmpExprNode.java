package ast.expr;

import ast.ASTVisitor;
import ast.ExprNode;
import utility.Position;
import utility.type.BoolType;


public class CmpExprNode extends ExprNode {
    public enum CmpOperator {
        Less, LessEqual, Greater, GreaterEqual,
        Equal, NotEqual
    }

    public ExprNode lhs, rhs;
    public CmpOperator operator;

    public CmpExprNode(Position pos,
                       ExprNode lhs,
                       ExprNode rhs,
                       CmpOperator operator) {
        super(pos);
        this.exprType = new BoolType();
        this.lhs = lhs;
        this.rhs = rhs;
        this.operator = operator;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
