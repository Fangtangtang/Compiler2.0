package ast.expr.ConstantExprNode;

import ast.ASTVisitor;
import ast.ExprNode;
import utility.Position;
import utility.type.IntType;

/**
 * @author F
 * 整型常量
 * 不可赋值
 */
public class IntConstantExprNode extends ExprNode {
    public int value;

    public IntConstantExprNode(Position pos, int value) {
        super(pos);
        this.exprType = new IntType();
        this.value = value;
    }

    @Override
    public <T> T accept(ASTVisitor<? extends T> visitor) {
        return visitor.visit(this);
    }
}
