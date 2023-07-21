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

    public ExprNode expr;

    public ExprStmtNode(Position pos, ExprNode expr) {
        super(pos);
        this.expr = expr;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
