package ast.other;

import ast.ASTNode;
import ast.ASTVisitor;
import utility.Position;

import java.util.ArrayList;

/**
 * @author F
 * varDefUnitNodes
 * 明确类型的变量
 * （变量定义\funcDef参数表等）
 */
public class InitNode extends ASTNode {
    public ArrayList<VarDefUnitNode> varDefUnitNodes = new ArrayList<>();
//    public ArrayList<ExprNode> parameterList=new ArrayList<>();
    public InitNode(Position pos) {
        super(pos);
    }

    @Override
    public <T> T accept(ASTVisitor<? extends T> visitor) {
        return visitor.visit(this);
    }
}
