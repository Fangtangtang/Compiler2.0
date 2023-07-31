package ast.expr;

import ast.ASTVisitor;
import ast.ExprNode;
import utility.Position;
import utility.type.Type;

/**
 * @author F
 * 成员访问，表达式类型同被访问的成员
 */
public class MemberVisExprNode extends ExprNode {
    public ExprNode lhs;
    public ExprNode rhs;

    public MemberVisExprNode(Position pos,
                             ExprNode lhs,
                             ExprNode rhs) {
        super(pos);
        this.lhs = lhs;
        this.rhs = rhs;
        this.exprType = rhs.exprType;
        this.isAssignable=true;
    }


    @Override
    public <T> T accept(ASTVisitor<? extends T> visitor) {
        return visitor.visit(this);
    }
}
