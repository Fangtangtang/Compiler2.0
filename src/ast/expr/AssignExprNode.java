package ast.expr;

import ast.ASTVisitor;

import ast.ExprNode;
import utility.Position;

/**
 * @author F
 * 赋值表达式，
 * 表达式类型无意义
 */
public class AssignExprNode extends ExprNode {
    public ExprNode lhs, rhs;

    public AssignExprNode(Position pos,
                          ExprNode lhs,
                          ExprNode rhs) {
        super(pos);
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
