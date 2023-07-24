package ast.stmt;

import ast.ASTVisitor;
import ast.StmtNode;
import utility.Position;

import java.util.*;

/**
 * @author F
 * 从StmtNode派生BlockStmtNode
 * ----------------------------------------------------------
 * suite:
 * LeftCurlyBrace statement* RightCurlyBrace;
 */
public class BlockStmtNode extends StmtNode {
    public ArrayList<StmtNode> statements = new ArrayList<>();

    public BlockStmtNode(Position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
