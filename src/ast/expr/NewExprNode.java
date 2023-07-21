package ast.expr;

import ast.ASTVisitor;
import ast.ExprNode;
import utility.Position;
import utility.type.Type;

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

    public NewExprNode(Position pos,
                       Type varType) {
        super(pos);
        this.exprType = varType;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
