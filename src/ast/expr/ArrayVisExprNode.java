package ast.expr;

import ast.ASTVisitor;
import ast.ExprNode;
import utility.Position;
import utility.type.Type;

import java.util.ArrayList;

/**
 * @author F
 * 数组下标访问表达式
 */
public class ArrayVisExprNode extends ExprNode {
    public ExprNode arrayName;
    public ArrayList<ExprNode> indexList;

    public ArrayVisExprNode(Position pos,
                            Type exprType,
                            ExprNode arrayName) {
        super(pos);
        this.exprType = exprType;
        this.arrayName = arrayName;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}