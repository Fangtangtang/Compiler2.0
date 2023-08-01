package ast;

import utility.Position;
import utility.type.Type;

/**
 * @author F
 * 各种expr的基类
 * 表达式有type，用于判断类型是否冲突
 * - 对表达式type无意义的：null
 * - 注意NullType表示null（引用类型的不指向任何对象的值）
 */
public abstract class ExprNode extends ASTNode {
    public Type exprType = null;
    public boolean isAssignable = false;

    public ExprNode(Position pos) {
        super(pos);
    }
}