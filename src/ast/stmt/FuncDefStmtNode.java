package ast.stmt;

import ast.ASTVisitor;
import ast.StmtNode;
import ast.other.ParameterNode;
import ast.other.TypeNode;
import utility.Position;

import java.util.*;

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
    public ParameterNode parameterList;
    public BlockStmtNode functionBody;

    public FuncDefStmtNode(Position pos,
                           TypeNode returnType,
                           String name,
                           ParameterNode parameterList,
                           BlockStmtNode functionBody) {
        super(pos);
        this.returnType = returnType;
        this.name = name;
        this.parameterList = parameterList;
        this.functionBody = functionBody;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
