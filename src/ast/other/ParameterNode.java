package ast.other;

import ast.ASTNode;
import ast.ASTVisitor;
import ast.ExprNode;
import utility.Position;

import java.util.ArrayList;

/**
 * @author F
 * 函数调用的参数结点
 */
public class ParameterNode extends ASTNode {
    public ArrayList<ExprNode> parameterList=new ArrayList<>();

    public ParameterNode(Position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
