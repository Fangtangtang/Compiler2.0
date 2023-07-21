package ast.expr;

import ast.ASTVisitor;
import ast.ExprNode;
import utility.Position;

/**
 * @author F
 * identifier变量名
 * TODO this包含在内？
 */
public class VarNameExprNode extends ExprNode {
    public String name;

    public VarNameExprNode(Position pos,
                           String name) {
        super(pos);
        this.name = name;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
