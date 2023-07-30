package ast.stmt;

import ast.ASTVisitor;
import ast.ExprNode;
import ast.StmtNode;
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
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
