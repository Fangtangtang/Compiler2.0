package ast.stmt;

import ast.ASTVisitor;
import ast.ExprNode;
import ast.StmtNode;
import utility.Position;
import utility.scope.Scope;

/**
 * @author F
 * ----------------------------------
 * selectionStatement:
 * If LeftRoundBracket conditionExpression=expression RightRoundBracket
 * trueStatement=statement
 * (Else falseStatement=statement)*
 * ;
 */
public class IfStmtNode extends StmtNode {
    public ExprNode condition;
    public StmtNode trueStatement;
    public StmtNode falseStatement;
    //可能有两个scope
    public Scope falseScope = null;

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
    public <T> T accept(ASTVisitor<? extends T> visitor) {
        return visitor.visit(this);
    }
}
