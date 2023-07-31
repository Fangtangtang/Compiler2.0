package ast.expr.ConstantExprNode;

import ast.ASTVisitor;
import ast.ExprNode;
import utility.Position;
import utility.type.BoolType;

/**
 * @author F
 * 逻辑常量
 * 不可赋值
 */
public class BoolConstantExprNode extends ExprNode {
    public boolean value;

    public BoolConstantExprNode(Position pos,
                                boolean value) {
        super(pos);
        this.exprType = new BoolType();
        this.value = value;
    }

    @Override
    public <T> T accept(ASTVisitor<? extends T> visitor) {
        return visitor.visit(this);
    }
}
