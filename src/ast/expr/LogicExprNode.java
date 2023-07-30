package ast.expr;

import ast.ASTVisitor;
import ast.ExprNode;
import utility.Position;
import utility.type.BoolType;

/**
 * @author F
 * 逻辑二元运算表达式
 * bool类型
 * 不可赋值
 */
public class LogicExprNode extends ExprNode {
    public enum LogicOperator {
        AndAnd, OrOr
    }

    public ExprNode lhs, rhs;
    public LogicOperator operator;

    public LogicExprNode(Position pos,
                         ExprNode lhs,
                         ExprNode rhs,
                         LogicOperator operator) {
        super(pos);
        this.exprType = new BoolType();
        this.lhs = lhs;
        this.rhs = rhs;
        this.operator = operator;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }


}
