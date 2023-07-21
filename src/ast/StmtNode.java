package ast;

import utility.Position;

/**
 * @author F
 * 各种Stmt的基类
 */
public abstract class StmtNode extends ASTNode {

    public StmtNode(Position pos) {
        super(pos);
    }
}