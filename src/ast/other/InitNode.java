package ast.other;

import ast.ASTNode;
import ast.ASTVisitor;
import utility.Position;

import java.util.ArrayList;

/**
 * @author F
 * for循环初始化列表
 * funcDef参数表
 * variableType? variableDeclaration (Comma variableDeclaration)* ;
 */
public class InitNode extends ASTNode {
    public ArrayList<VarDefUnitNode> varDefUnitNodes = new ArrayList<>();

    public InitNode(Position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
