package ast;

import ast.expr.ConstantExprNode.*;
import ast.stmt.*;
import ast.expr.*;
import ast.other.*;

/**
 * @author F
 * ASTVisitor接口
 * （类似parse tree的Visitor接口）
 * 被ASTNode接受，访问每一个结点，遍历AST
 */
public interface ASTVisitor<T> {

    //root
    T visit(RootNode node);

    //stmt
    T visit(BlockStmtNode node);

    T visit(BreakStmtNode node);

    T visit(ConstructorDefStmtNode node);

    T visit(ContinueStmtNode node);

    T visit(ExprStmtNode node);

    T visit(ForStmtNode node);

    T visit(FuncDefStmtNode node);

    T visit(IfStmtNode node);

    T visit(ReturnStmtNode node);

    T visit(VarDefStmtNode node);

    T visit(WhileStmtNode node);

    //expr
    T visit(ArrayVisExprNode node);

    T visit(AssignExprNode node);

    T visit(BinaryExprNode node);

    T visit(CmpExprNode node);

    T visit(FuncCallExprNode node);

    T visit(LogicExprNode node);

    T visit(LogicPrefixExprNode node);

    T visit(MemberVisExprNode node);

    T visit(NewExprNode node);

    T visit(ParenthesisExprNode node);

    T visit(PrefixExprNode node);

    T visit(PointerExprNode node);


    T visit(SuffixExprNode node);

    T visit(TernaryExprNode node);

    T visit(VarNameExprNode node);

    T visit(BoolConstantExprNode node);

    T visit(IntConstantExprNode node);

    T visit(NullConstantExprNode node);

    T visit(StrConstantExprNode node);

    //other
    T visit(ClassDefNode node);

    T visit(InitNode node);

    T visit(ParameterNode node);

    T visit(TypeNode node);

    T visit(VarDefUnitNode node);

}
