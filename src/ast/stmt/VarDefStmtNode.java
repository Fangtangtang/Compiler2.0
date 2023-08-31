package ast.stmt;

import ast.ASTVisitor;
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
    public <T> T accept(ASTVisitor<? extends T> visitor) {
        return visitor.visit(this);
    }
}
