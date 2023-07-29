package ast.other;

import ast.ASTNode;
import ast.ASTVisitor;
import ast.ExprNode;
import utility.Position;

import java.util.ArrayList;

/**
 * @author F
 * varDefUnitNodes
 *      for循环初始化列表
 *      funcDef参数表
 *      variableType? variableDeclaration (Comma variableDeclaration)* ;
 * parameterList：
 *      函数调用的参数结点
 *      for循环非新建的变量
 */
public class InitNode extends ASTNode {
    public ArrayList<VarDefUnitNode> varDefUnitNodes = new ArrayList<>();
    public ArrayList<ExprNode> parameterList=new ArrayList<>();
    public InitNode(Position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
