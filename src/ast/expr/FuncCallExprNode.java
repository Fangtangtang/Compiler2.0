package ast.expr;

import ast.ASTVisitor;
import utility.Position;

import java.util.ArrayList;

/**
 * @author F
 * 函数调用表达式
 * 返回右值，不可赋值
 * func:函数名、类函数名……
 */
public class FuncCallExprNode extends ExprNode {
    public ExprNode func;
    //    public ParameterNode parameter;
    public ArrayList<ExprNode> parameterList = new ArrayList<>();


    public FuncCallExprNode(Position pos,
                            ExprNode func) {
        super(pos);
        this.func = func;
    }

    @Override
    public <T> T accept(ASTVisitor<? extends T> visitor) {
        return visitor.visit(this);
    }
}
