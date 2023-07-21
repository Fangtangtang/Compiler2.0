package ast.stmt;

import ast.ASTVisitor;
import ast.ExprNode;
import ast.StmtNode;
import utility.Position;

/**
 * @author F
 * ----------------------------------
 * selectionStatement:
 *     If LeftRoundBracket conditionExpression=expression RightRoundBracket
 *         trueStatement=statement
 *     (Else falseStatement=statement)*
 *     ;
 */
public class IfStmtNode extends StmtNode {
    public ExprNode condition;
    public StmtNode trueStatement;
    public StmtNode falseStatement;

    public IfStmtNode(Position pos,
                      ExprNode condition,
                      StmtNode trueStatement,
                      StmtNode falseStatement) {
        super(pos);
        this.condition = condition;
        this.trueStatement = trueStatement;
        this.falseStatement = falseStatement;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
