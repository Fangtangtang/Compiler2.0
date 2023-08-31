package ast.stmt;

import ast.ASTVisitor;
import utility.Position;

/**
 * @author F
 * -------------------------------------
 * continueStatement:
 * Continue Semicolon;
 */
public class ContinueStmtNode extends StmtNode {

    public ContinueStmtNode(Position pos) {
        super(pos);
    }

    @Override
    public <T> T accept(ASTVisitor<? extends T> visitor) {
        return visitor.visit(this);
    }
}
