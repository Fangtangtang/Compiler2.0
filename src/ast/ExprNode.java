package ast;

import utility.Position;

/**
 * @author F
 * 各种expr的基类
 */
public abstract class ExprNode extends ASTNode {

    public ExprNode(Position pos) {
        super(pos);
    }
}