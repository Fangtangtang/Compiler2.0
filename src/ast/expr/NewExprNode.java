package ast.expr;

import ast.ASTVisitor;
import ast.ExprNode;
import ast.other.TypeNode;
import utility.Position;
import utility.type.Type;

import java.util.ArrayList;

/**
 * @author F
 * ---------------------------------
 * New construction
 * (Identifier | buildInVariableType)
 * (LeftSquareBracket expression RightSquareBracket)+
 * (LeftSquareBracket RightSquareBracket)*                     #arrayConstruction
 * | (Identifier | buildInVariableType) LeftRoundBracket RightRoundBracket         #varConstruction
 * | (Identifier | buildInVariableType)                                            #varSimpleConstruction
 * 不可赋值
 */
public class NewExprNode extends ExprNode {
    public TypeNode typeNode;
    public Integer dimension;
    public ArrayList<ExprNode> dimensions = new ArrayList<>();

    public NewExprNode(Position pos,
                       Integer dimension,
                       TypeNode typeNode) {
        super(pos);
        this.typeNode=typeNode;
        this.dimension=dimension;
        this.exprType = typeNode.type;
    }

    @Override
    public <T> T accept(ASTVisitor<? extends T> visitor) {
        return visitor.visit(this);
    }
}
