package ast.stmt;

import ast.ASTVisitor;
import ast.ExprNode;
import ast.StmtNode;
import ast.other.InitNode;
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
        this.initializationStmt=initializationStmt;
        this.condition=condition;
        this.step=step;
        this.statement=statement;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
