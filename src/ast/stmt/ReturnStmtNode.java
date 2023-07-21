package ast.stmt;

import ast.ASTVisitor;
import ast.ExprNode;
import ast.StmtNode;
import utility.Position;

/**
 * @author F
 * ----------------------------------------
 * returnStatement:
 *     Return expression? Semicolon;
 */
public class ReturnStmtNode extends StmtNode {
    public ExprNode expression;

    public ReturnStmtNode(Position pos,
                          ExprNode expression) {
        super(pos);
        this.expression = expression;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
