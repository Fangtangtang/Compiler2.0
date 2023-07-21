package ast;

import ast.stmt.*;

/**
 * @author F
 * ASTVisitor接口
 * 被ASTNode接受，访问每一个结点，遍历AST
 */
public interface ASTVisitor {

    //root
    void visit(RootNode node);

    //stmt
    void visit(BlockStmtNode node);
    void visit(BreakStmtNode node);
    void visit(ContinueStmtNode node);
    void visit(ExprStmtNode node);
    void visit(ForStmtNode node);
    void visit(FuncDefStmtNode node);
    void visit(IfStmtNode node);
    void visit(ReturnStmtNode node);
    void visit(VarDefStmtNode node);
    void visit(WhileStmtNode node);

    //expr

}
