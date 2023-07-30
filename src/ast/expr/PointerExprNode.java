package ast.expr;

import ast.ASTVisitor;
import ast.ExprNode;
import utility.Position;
import utility.type.*;

/**
 * @author F
 * This指针
 */
public class PointerExprNode extends ExprNode {

    public PointerExprNode(Position pos,
                           Type exprType) {
        super(pos);
        this.exprType = exprType;
        this.isAssignable=true;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
