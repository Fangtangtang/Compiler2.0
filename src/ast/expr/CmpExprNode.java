package ast.expr;

import ast.ASTVisitor;
import utility.Position;
import ast.type.BoolType;


/**
 * @author F
 * 二元比较
 * 运算结果不可赋值
 */
public class CmpExprNode extends ExprNode {
    public enum CmpOperator {
        Less, LessEqual, Greater, GreaterEqual,
        Equal, NotEqual
    }

    public ExprNode lhs, rhs;
    public CmpOperator operator;

    public CmpExprNode(Position pos,
                       ExprNode lhs,
                       ExprNode rhs,
                       CmpOperator operator) {
        super(pos);
        this.exprType = new BoolType();
        this.lhs = lhs;
        this.rhs = rhs;
        this.operator = operator;
    }

    @Override
    public <T> T accept(ASTVisitor<? extends T> visitor) {
        return visitor.visit(this);
    }
}
