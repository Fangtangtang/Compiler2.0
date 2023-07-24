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
 */
public class NewExprNode extends ExprNode {
    public TypeNode typeNode;

    public ArrayList<ExprNode> dimensions = new ArrayList<>();

    public NewExprNode(Position pos,
                       TypeNode typeNode) {
        super(pos);
        this.typeNode=typeNode;
        this.exprType = typeNode.type;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
