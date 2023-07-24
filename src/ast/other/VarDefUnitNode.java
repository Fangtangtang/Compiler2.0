package ast.other;

import ast.ASTNode;
import ast.ASTVisitor;
import ast.ExprNode;
import utility.Position;

/**
 * @author F
 * 变量声明单元
 * 将原本可能多个变量定义合写的拆解开
 * 变量类型、名称、初始化表达式
 */
public class VarDefUnitNode extends ASTNode {
    public TypeNode typeNode;
    public String name;
    public ExprNode initExpr;

    public VarDefUnitNode(Position pos,
                          TypeNode typeNode,
                          String name,
                          ExprNode initExpr) {
        super(pos);
        this.typeNode = typeNode;
        this.name = name;
        this.initExpr = initExpr;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
