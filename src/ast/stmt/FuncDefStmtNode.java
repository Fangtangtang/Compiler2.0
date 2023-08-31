package ast.stmt;

import ast.ASTVisitor;
import ast.other.*;
import utility.Position;

/**
 * @author F
 * -------------------------------------
 * 函数
 * funcDefStatement:
 * returnType Identifier
 * LeftRoundBracket funcParameterList* RightRoundBracket
 * functionBody=suite
 * ;
 */
public class FuncDefStmtNode extends StmtNode {

    public TypeNode returnType;
    public String name;
    public InitNode parameterList;
    public BlockStmtNode functionBody;

    public FuncDefStmtNode(Position pos,
                           TypeNode returnType,
                           String name,
                           InitNode parameterList,
                           BlockStmtNode functionBody) {
        super(pos);
        this.returnType = returnType;
        this.name = name;
        this.parameterList = parameterList;
        this.functionBody = functionBody;
    }

    @Override
    public <T> T accept(ASTVisitor<? extends T> visitor) {
        return visitor.visit(this);
    }
}
