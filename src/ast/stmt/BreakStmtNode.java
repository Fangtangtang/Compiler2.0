package ast.stmt;

import ast.ASTVisitor;
import utility.Position;

/**
 * @author F
 * ------------------------------
 * breakStatement:
 *     Break Semicolon;
 */
public class BreakStmtNode extends StmtNode {

    public BreakStmtNode(Position pos) {
        super(pos);
    }

    @Override
    public <T> T accept(ASTVisitor<? extends T> visitor) {
        return visitor.visit(this);
    }
}
