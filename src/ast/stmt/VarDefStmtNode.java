package ast.stmt;

import ast.ASTVisitor;
import ast.StmtNode;
import ast.other.VarDefUnitNode;
import utility.Position;

import java.util.ArrayList;


/**
 * @author F
 */
public class VarDefStmtNode extends StmtNode {

    public ArrayList<VarDefUnitNode> varDefUnitNodes = new ArrayList<>();

    public VarDefStmtNode(Position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
