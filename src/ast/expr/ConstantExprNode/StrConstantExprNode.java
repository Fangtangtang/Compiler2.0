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
        this.exprType = new StringType();
        this.value = value;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
