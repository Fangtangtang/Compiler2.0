package ast.expr;

import ast.ASTVisitor;
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
    }

    @Override
    public <T> T accept(ASTVisitor<? extends T> visitor) {
        return visitor.visit(this);
    }
}
