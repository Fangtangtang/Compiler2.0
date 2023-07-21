package ast.other;

import ast.ASTNode;
import ast.ASTVisitor;
import ast.ExprNode;
import utility.Position;

/**
 * @author F
 * 函数的参数结点
 * 将原本的参数表拆开
 */
public class ParameterNode extends ASTNode {

    public TypeNode varType;
    public String name;
    public ExprNode defaultValue;

    public ParameterNode(Position pos,
                         TypeNode varType,
                         String name,
                         ExprNode defaultValue) {
        super(pos);
        this.varType = varType;
        this.name = name;
        this.defaultValue = defaultValue;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
