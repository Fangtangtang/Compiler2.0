package ast.stmt;

import ast.ASTVisitor;
import ast.expr.ExprNode;
import utility.Position;

/**
 * @author F
 * -------------------------------------
 * forStatement:
 * For LeftRoundBracket
 * initializationStatement? Semicolon
 * (forConditionExpression=expression)? Semicolon
 * (stepExpression=expression)?
 * RightRoundBracket
 * statement
 * ;
 */
public class ForStmtNode extends StmtNode {

    public StmtNode initializationStmt;
    public ExprNode condition;
    public ExprNode step;
    public StmtNode statement;

    public ForStmtNode(Position pos,
                       StmtNode initializationStmt,
                       ExprNode condition,
                       ExprNode step,
                       StmtNode statement) {
        super(pos);
        this.initializationStmt = initializationStmt;
        this.condition = condition;
        this.step = step;
        this.statement = statement;
    }

    @Override
    public <T> T accept(ASTVisitor<? extends T> visitor) {
        return visitor.visit(this);
    }
}
