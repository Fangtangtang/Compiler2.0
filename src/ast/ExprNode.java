package ast;

import utility.Position;
import utility.type.Type;

/**
 * @author F
 * 各种expr的基类
 * 表达式有type，用于判断类型是否冲突
 */
public abstract class ExprNode extends ASTNode {
    public Type exprType;

    public boolean isAssignable = false;

    public ExprNode(Position pos) {
        super(pos);
    }
}