package ast.expr;

import ast.ASTVisitor;
import ast.ExprNode;
import ast.other.InitNode;
import ast.other.ParameterNode;
import utility.Position;
import utility.type.FunctionType;

import java.util.ArrayList;

/**
 * @author F
 * 函数调用表达式
 */
public class FuncCallExprNode extends ExprNode {
    public ExprNode func;
    public ParameterNode parameter;

    public FuncCallExprNode(Position pos,
                            ExprNode func,
                            ParameterNode parameter) {
        super(pos);
        this.exprType = new FunctionType();
        this.func = func;
        this.parameter=parameter;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
