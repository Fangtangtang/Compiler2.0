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
                             Type exprType,
                             ExprNode lhs,
                             ExprNode rhs) {
        super(pos);
        this.exprType = exprType;
        this.lhs = lhs;
        this.rhs = rhs;
    }


    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
