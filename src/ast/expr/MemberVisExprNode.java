package ast.expr;

import ast.ASTVisitor;
import utility.Position;

/**
 * @author F
 * 成员访问，表达式类型同被访问的成员
 * 访问除 string 外的基本类型 int, bool 的成员变量返回一个实值(不可赋值)
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
    }


    @Override
    public <T> T accept(ASTVisitor<? extends T> visitor) {
        return visitor.visit(this);
    }
}
