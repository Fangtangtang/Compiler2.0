package ast.expr.ConstantExprNode;

import ast.ASTVisitor;
import ast.ExprNode;
import utility.Position;
import utility.type.IntType;

/**
 * @author F
 * 整型常量
 * 不可赋值
 * 先将数值用字符串形式存下来，最后需要用数值的时候再转换成数值
 * 需要特判2147483648，-2147483648合法，但是2147483648超出int
 */
public class IntConstantExprNode extends ExprNode {
    public String value;

    public IntConstantExprNode(Position pos, String value) {
        super(pos);
        this.exprType = new IntType();
        this.value = value;
    }

    @Override
    public <T> T accept(ASTVisitor<? extends T> visitor) {
        return visitor.visit(this);
    }
}
