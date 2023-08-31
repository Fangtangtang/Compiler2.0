package ast.stmt;

import ast.ASTVisitor;
import ast.expr.ExprNode;
import utility.Position;

import java.util.*;

/**
 * @author F
 * ---------------------------------------
 * 表达式语句直接由一个表达式加 ; 组成
 * expressionStatement:
 * expression Semicolon;
 */
public class ExprStmtNode extends StmtNode {

    public ArrayList<ExprNode> exprList=new ArrayList<>() ;

    public ExprStmtNode(Position pos) {
        super(pos);
    }

    @Override
    public <T> T accept(ASTVisitor<? extends T> visitor) {
        return visitor.visit(this);
    }
}
