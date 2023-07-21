package ast.stmt;

import ast.ASTVisitor;
import ast.StmtNode;
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
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
