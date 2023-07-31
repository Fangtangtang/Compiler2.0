package ast.other;

import ast.ASTNode;
import ast.ASTVisitor;
import ast.ExprNode;
import utility.Position;

import java.util.ArrayList;

/**
 * @author F
 * 函数调用参数结点
 * 不带类型
 */
public class ParameterNode extends ASTNode {
    public ArrayList<ExprNode> parameterList = new ArrayList<>();

    public ParameterNode(Position pos) {
        super(pos);
    }

    @Override
    public <T> T accept(ASTVisitor<? extends T> visitor) {
        return visitor.visit(this);
    }
}
