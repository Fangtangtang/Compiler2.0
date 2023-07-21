package ast.stmt;

import ast.ASTVisitor;
import ast.StmtNode;
import utility.Position;

/**
 * @author F
 * -------------------------------------
 * continueStatement:
 *     Continue Semicolon;
 */
public class ContinueStmtNode extends StmtNode {

    public ContinueStmtNode(Position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
