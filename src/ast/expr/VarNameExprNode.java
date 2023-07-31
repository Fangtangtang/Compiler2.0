package ast.expr;

import ast.ASTVisitor;
import ast.ExprNode;
import utility.Position;

/**
 * @author F
 * identifier变量名
 */
public class VarNameExprNode extends ExprNode {
    public String name;

    public VarNameExprNode(Position pos,
                           String name) {
        super(pos);
        this.name = name;
        this.isAssignable = true;
    }

    @Override
    public <T> T accept(ASTVisitor<? extends T> visitor) {
        return visitor.visit(this);
    }
}
