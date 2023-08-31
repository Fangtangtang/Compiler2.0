package ast.stmt;

import ast.ASTVisitor;
import ast.expr.ExprNode;
import utility.Position;

/**
 * @author F
 * ------------------------------------
 * whileStatement:
 *     While  LeftRoundBracket
 *             conditionExpression=expression
 *            RightRoundBracket statement
 *     ;
 */
public class WhileStmtNode extends StmtNode {
    public ExprNode condition;
    public StmtNode statement;

    public WhileStmtNode(Position pos,
                         ExprNode condition,
                         StmtNode statement) {
        super(pos);
        this.condition = condition;
        this.statement = statement;
    }

    @Override
    public <T> T accept(ASTVisitor<? extends T> visitor) {
        return visitor.visit(this);
    }

}
