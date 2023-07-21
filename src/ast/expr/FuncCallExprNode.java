package ast.expr;

import ast.ASTVisitor;
import ast.ExprNode;
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
    public ArrayList<ParameterNode> parameterList = new ArrayList<>();

    public FuncCallExprNode(Position pos,
                            ExprNode func) {
        super(pos);
        this.exprType = new FunctionType();
        this.func = func;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
