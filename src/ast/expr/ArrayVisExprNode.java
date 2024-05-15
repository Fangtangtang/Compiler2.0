package ast.expr;

import ast.ASTVisitor;
import utility.Position;

import java.util.ArrayList;

/**
 * @author F
 * 数组下标访问表达式
 * 访问到的数组的实际维度应该由原数组的维度减去指出的维度得到
 */
public class ArrayVisExprNode extends ExprNode {
    public ExprNode arrayName;
    public ArrayList<ExprNode> indexList = new ArrayList<>();

    public ArrayVisExprNode(Position pos,
                            ExprNode arrayName) {
        super(pos);
        this.isAssignable = true;
        this.arrayName = arrayName;
    }

    @Override
    public <T> T accept(ASTVisitor<? extends T> visitor) {
        return visitor.visit(this);
    }
}
