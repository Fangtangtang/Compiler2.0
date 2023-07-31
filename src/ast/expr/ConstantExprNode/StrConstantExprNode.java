package ast.expr.ConstantExprNode;

import ast.ASTVisitor;
import ast.ExprNode;
import utility.Position;
import utility.type.StringType;

/**
 * @author F
 * 字符串常量
 * 不可赋值
 */
public class StrConstantExprNode extends ExprNode {
    public String value;

    public StrConstantExprNode(Position pos,
                               String value) {
        super(pos);
        this.exprType = new StringType(true);
        this.value = value;
    }

    @Override
    public <T> T accept(ASTVisitor<? extends T> visitor) {
        return visitor.visit(this);
    }
}
