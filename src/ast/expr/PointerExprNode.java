package ast.expr;

import ast.ASTVisitor;
import ast.ExprNode;
import utility.Position;
import utility.type.*;

/**
 * @author F
 * This指针
 * this 指针作为左值视为语法错误
 */
public class PointerExprNode extends ExprNode {

    public PointerExprNode(Position pos,
                           Type exprType) {
        super(pos);
        this.exprType = exprType;
    }

    @Override
    public <T> T accept(ASTVisitor<? extends T> visitor) {
        return visitor.visit(this);
    }
}
