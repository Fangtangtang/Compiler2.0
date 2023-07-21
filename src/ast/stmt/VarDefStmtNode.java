package ast.stmt;

import ast.ASTVisitor;
import ast.ExprNode;
import ast.StmtNode;
import ast.other.TypeNode;
import utility.Position;

/**
 * @author F
 * 将原本可能多个变量定义合写的拆解开
 * 变量类型、名称、初始化表达式
 */
public class VarDefStmtNode extends StmtNode {
    public String name;
    public TypeNode varType;
    public ExprNode initialization;

    public VarDefStmtNode(Position pos,
                          String name,
                          TypeNode varType,
                          ExprNode initialization) {
        super(pos);
        this.name = name;
        this.varType = varType;
        this.initialization = initialization;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
