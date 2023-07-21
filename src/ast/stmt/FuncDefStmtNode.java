package ast.stmt;

import ast.ASTVisitor;
import ast.StmtNode;
import utility.Position;
import java.util.*;

/**
 * @author F
 * -------------------------------------
 * 函数
 * funcDefStatement:
 *     returnType Identifier
 *     LeftRoundBracket funcParameterList* RightRoundBracket
 *     functionBody=suite
 *     ;
 */
public class FuncDefStmtNode extends StmtNode {


    public FuncDefStmtNode(Position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
