package ast;

import ast.expr.*;
import ast.expr.ConstantExprNode.BoolConstantExprNode;
import ast.expr.ConstantExprNode.IntConstantExprNode;
import ast.expr.ConstantExprNode.NullConstantExprNode;
import ast.expr.ConstantExprNode.StrConstantExprNode;
import ast.other.*;
import ast.stmt.*;

/**
 * @author F
 * 函数体为空的astVisitor
 */
public class ASTBaseVisitor<T> implements ASTVisitor<T> {
    @Override
    public T visit(RootNode node) {
        return null;
    }

    @Override
    public T visit(BlockStmtNode node) {
        return null;
    }

    @Override
    public T visit(BreakStmtNode node) {
        return null;
    }

    @Override
    public T visit(ConstructorDefStmtNode node) {
        return null;
    }

    @Override
    public T visit(ContinueStmtNode node) {
        return null;
    }

    @Override
    public T visit(ExprStmtNode node) {
        return null;
    }

    @Override
    public T visit(ForStmtNode node) {
        return null;
    }

    @Override
    public T visit(FuncDefStmtNode node) {
        return null;
    }

    @Override
    public T visit(IfStmtNode node) {
        return null;
    }

    @Override
    public T visit(ReturnStmtNode node) {
        return null;
    }

    @Override
    public T visit(VarDefStmtNode node) {
        return null;
    }

    @Override
    public T visit(WhileStmtNode node) {
        return null;
    }

    @Override
    public T visit(ArrayVisExprNode node) {
        return null;
    }

    @Override
    public T visit(AssignExprNode node) {
        return null;
    }

    @Override
    public T visit(BinaryExprNode node) {
        return null;
    }

    @Override
    public T visit(CmpExprNode node) {
        return null;
    }

    @Override
    public T visit(FuncCallExprNode node) {
        return null;
    }

    @Override
    public T visit(LogicExprNode node) {
        return null;
    }

    @Override
    public T visit(LogicPrefixExprNode node) {
        return null;
    }

    @Override
    public T visit(MemberVisExprNode node) {
        return null;
    }

    @Override
    public T visit(NewExprNode node) {
        return null;
    }

    @Override
    public T visit(ParenthesisExprNode node) {
        return null;
    }

    @Override
    public T visit(PrefixExprNode node) {
        return null;
    }

    @Override
    public T visit(PointerExprNode node) {
        return null;
    }

    @Override
    public T visit(SuffixExprNode node) {
        return null;
    }

    @Override
    public T visit(TernaryExprNode node) {
        return null;
    }

    @Override
    public T visit(VarNameExprNode node) {
        return null;
    }

    @Override
    public T visit(BoolConstantExprNode node) {
        return null;
    }

    @Override
    public T visit(IntConstantExprNode node) {
        return null;
    }

    @Override
    public T visit(NullConstantExprNode node) {
        return null;
    }

    @Override
    public T visit(StrConstantExprNode node) {
        return null;
    }

    @Override
    public T visit(ClassDefNode node) {
        return null;
    }

    @Override
    public T visit(InitNode node) {
        return null;
    }

    @Override
    public T visit(ParameterNode node) {
        return null;
    }

    @Override
    public T visit(TypeNode node) {
        return null;
    }

    @Override
    public T visit(VarDefUnitNode node) {
        return null;
    }
}
