package frontend;

import ast.ASTNode;
import ast.ExprNode;
import ast.RootNode;
import ast.StmtNode;
import ast.other.*;
import ast.stmt.*;
import ast.expr.*;
import org.antlr.v4.runtime.ParserRuleContext;
import parser.MxParser;
import parser.MxParser.*;
import parser.MxVisitor;
import utility.Position;

/**
 * @author F
 * ASTBuilder
 * 继承MxBaseVisitor,遍历parse tree部分结点
 * 构造AST
 */
public class ASTBuilder extends MxVisitor<ASTNode> {

    /**
     * visitProgram
     * ----------------------------------------------------------------------
     * 访问parse tree的根节点
     * program:(funcDefStatement | declarationStatement)*
     * 一一添加StmtNode
     *
     * @param ctx the parse tree
     * @return root
     */
    @Override
    public ASTNode visitProgram(MxParser.ProgramContext ctx) {
        RootNode root = new RootNode(new Position(ctx));
        ctx.declarationStatement().forEach(
                stmt -> root.declarations.add((VarDefStmtNode) visit(stmt))
        );
        ctx.funcDefStatement().forEach(
                stmt -> root.declarations.add((FuncDefStmtNode) visit(stmt))
        );
        return root;
    }

    @Override
    public ASTNode visitBlockStmt(MxParser.BlockStmtContext ctx) {
        return visitChildren(ctx.suite());
    }

    @Override
    public ASTNode visitIfStmt(MxParser.IfStmtContext ctx) {
        return visitChildren(ctx.selectionStatement());
    }

    @Override
    public ASTNode visitWhileStmt(MxParser.WhileStmtContext ctx) {
        return visitChildren(ctx.whileStatement());
    }

    @Override
    public ASTNode visitForStmt(MxParser.ForStmtContext ctx) {
        return visitChildren(ctx.forStatement());
    }

    @Override
    public ASTNode visitReturnStmt(MxParser.ReturnStmtContext ctx) {
        return visitChildren(ctx.returnStatement());
    }

    @Override
    public ASTNode visitBreakStmt(MxParser.BreakStmtContext ctx) {
        return visitChildren(ctx.breakStatement());
    }

    @Override
    public ASTNode visitContinueStmt(MxParser.ContinueStmtContext ctx) {
        return visitChildren(ctx.continueStatement());
    }

    @Override
    public ASTNode visitVarDefStmt(MxParser.VarDefStmtContext ctx) {
        return visitChildren(ctx.declarationStatement());
    }

    @Override
    public ASTNode visitExprStmt(MxParser.ExprStmtContext ctx) {
        return visitChildren(ctx.expressionStatement());
    }

    @Override
    public ASTNode visitFuncDefStmt(MxParser.FuncDefStmtContext ctx) {
        return visitChildren(ctx.funcDefStatement());
    }

    @Override
    public ASTNode visitConstructorStmt(MxParser.ConstructorStmtContext ctx) {
        return visitChildren(ctx.constructFuncDefStatement());
    }

    /**
     * visitSuite
     * ----------------------------------------------------------------------
     * 访问suite
     * 构建结点BlockStmtNode，一一添加statement
     *
     * @param ctx the parse tree
     * @return blockStmtNode
     */
    @Override
    public ASTNode visitSuite(MxParser.SuiteContext ctx) {
        BlockStmtNode blockStmtNode = new BlockStmtNode(new Position(ctx));
        ctx.statement().forEach(
                stmt -> blockStmtNode.statements.add((StmtNode) visit(stmt))
        );
        return blockStmtNode;
    }

    /**
     * visitDeclarationStatement
     * ----------------------------------------------------------------------
     * 变量声明语句
     * 子结点：变量声明单元
     *
     * @param ctx the parse tree
     * @return varDefStmtNode
     */
    @Override
    public ASTNode visitDeclarationStatement(MxParser.DeclarationStatementContext ctx) {
        VarDefStmtNode varDefStmtNode = new VarDefStmtNode(new Position(ctx));
        ctx.variableDeclaration().forEach(
                varDef -> varDefStmtNode.varDefUnitNodes.add((VarDefUnitNode) visit(varDef))
        );
        return varDefStmtNode;
    }

    /**
     * visitVariableDeclaration
     * -----------------------------------------------------------------------
     * 变量声明单元
     * variableDeclaration:Identifier (Assign initExpression=expression)?;
     * 从父结点获得type
     *
     * @param ctx the parse tree
     * @return varDefUnitNode
     */
    @Override
    public ASTNode visitVariableDeclaration(MxParser.VariableDeclarationContext ctx) {
        DeclarationStatementContext parentCtx = (DeclarationStatementContext) ctx.getParent();
        ExprNode expr = null;
        if (ctx.expression() != null) {
            expr = (ExprNode) visit(ctx.expression());
        }
        return new VarDefUnitNode(
                new Position(ctx),
                new TypeNode(new Position(parentCtx.variableType())),
                ctx.Identifier().toString(),
                expr
        );
    }

    /**
     * visitSelectionStatement
     * ------------------------------------------------------------------------
     * 条件选择语句
     * If LeftRoundBracket conditionExpression=expression RightRoundBracket
     * trueStatement=statement
     * (Else falseStatement=statement)*
     *
     * @param ctx the parse tree
     * @return ifStmtNode
     */
    @Override
    public ASTNode visitSelectionStatement(SelectionStatementContext ctx) {
        StmtNode falseStmt = null;
        if (ctx.falseStatement != null) {
            falseStmt = (StmtNode) visit(ctx.falseStatement);
        }
        return new IfStmtNode(
                new Position(ctx),
                (ExprNode) visit(ctx.conditionExpression),
                (StmtNode) visit(ctx.trueStatement),
                falseStmt
        );
    }

    /**
     * visitWhileStatement
     * -----------------------------------------------------------------------
     * While LeftRoundBracket
     * conditionExpression=expression
     * RightRoundBracket statement
     *
     * @param ctx the parse tree
     * @return whileStmtNode
     */
    @Override
    public ASTNode visitWhileStatement(WhileStatementContext ctx) {
        return new WhileStmtNode(
                new Position(ctx),
                (ExprNode) visit(ctx.expression()),
                (StmtNode) visit(ctx.statement())
        );
    }

    /**
     * visitForStatement
     * -----------------------------------------------------------------------
     * For LeftRoundBracket
     * initializationStatement? Semicolon
     * (forConditionExpression=expression)? Semicolon
     * (stepExpression=expression)?
     * RightRoundBracket
     * statement
     * ;
     *
     * @param ctx the parse tree
     * @return forStmtNode
     */
    @Override
    public ASTNode visitForStatement(ForStatementContext ctx) {
        StmtNode initStatement = null;
        if (ctx.initList() != null) {
            initStatement = (StmtNode) visit(ctx.initList());
        }
        ExprNode conditionExpr = null;
        if (ctx.forConditionExpression != null) {
            conditionExpr = (ExprNode) visit(ctx.forConditionExpression);
        }
        ExprNode stepExpr = null;
        if (ctx.stepExpression != null) {
            stepExpr = (ExprNode) visit(ctx.stepExpression);
        }
        return new ForStmtNode(
                new Position(ctx),
                initStatement,
                conditionExpr,
                stepExpr,
                (StmtNode) visit(ctx.statement())
        );
    }

    /**
     * visitInitList
     * --------------------------------------------------------------
     * for循环初始化
     *
     * @param ctx the parse tree
     * @return initNode
     */
    @Override
    public ASTNode visitInitList(InitListContext ctx) {
        InitNode initNode = new InitNode(new Position(ctx));
        ctx.variableDeclaration().forEach(
                varDef -> initNode.varDefUnitNodes.add((VarDefUnitNode) visit(varDef))
        );
        return initNode;
    }

    /**
     * visitReturnStatement
     * ---------------------------------------------------------------
     * Return expression? Semicolon;
     *
     * @param ctx the parse tree
     * @return returnStmtNode
     */
    @Override
    public ASTNode visitReturnStatement(ReturnStatementContext ctx) {
        ExprNode expr = null;
        if (ctx.expression() != null) {
            expr = (ExprNode) visit(ctx.expression());
        }
        return new ReturnStmtNode(
                new Position(ctx),
                expr
        );
    }

    /**
     * visitBreakStatement
     * --------------------------------------------------------------
     *
     * @param ctx the parse tree
     * @return breakStmtNode
     */
    @Override
    public ASTNode visitBreakStatement(BreakStatementContext ctx) {
        return new BreakStmtNode(new Position(ctx));
    }

    /**
     * visitContinueStatement
     * ---------------------------------------------------------------
     *
     * @param ctx the parse tree
     * @return continueStmtNode
     */
    @Override
    public ASTNode visitContinueStatement(ContinueStatementContext ctx) {
        return new ContinueStmtNode(new Position(ctx));
    }

    /**
     * visitExpressionStatement
     * ----------------------------------------------------------------
     * 表达式语句
     * expression Semicolon;
     *
     * @param ctx the parse tree
     * @return exprStmtNode
     */
    @Override
    public ASTNode visitExpressionStatement(ExpressionStatementContext ctx) {
        return new ExprStmtNode(
                new Position(ctx),
                (ExprNode) visit(ctx.expression())
        );
    }

    /**
     * visitParameterList
     * ------------------------------------------------------------------
     * 函数调用的参数表
     * expression (Comma expression)*
     *
     * @param ctx the parse tree
     * @return parameterNode
     */
    @Override
    public ASTNode visitParameterList(ParameterListContext ctx) {
        ParameterNode parameterNode = new ParameterNode(new Position(ctx));
        ctx.expression().forEach(
                expr -> parameterNode.parameterList.add((ExprNode) visit(expr))
        );
        return parameterNode;
    }

    /**
     * visitFuncDefStatement
     * -----------------------------------------------------------------
     * 函数声明定义
     * funcDefStatement:
     * returnType Identifier
     * LeftRoundBracket funcParameterList? RightRoundBracket
     * functionBody=suite
     * ;
     *
     * @param ctx the parse tree
     * @return funcDefStmtNode
     */
    @Override
    public ASTNode visitFuncDefStatement(FuncDefStatementContext ctx) {
        return new FuncDefStmtNode(new Position(ctx),
                (TypeNode) visit(ctx.returnType()),
                ctx.Identifier().toString(),
                (ParameterNode) visit(ctx.funcParameterList()),
                (BlockStmtNode) visit(ctx.functionBody));
    }

    /**
     * visitFuncParameterList
     * ----------------------------------------------------------------
     * 函数声明的参数表
     * variableType variableDeclaration (Comma variableType variableDeclaration)*
     *
     * @param ctx the parse tree
     * @return initNode
     */
    @Override
    public ASTNode visitFuncParameterList(FuncParameterListContext ctx) {
        InitNode initNode = new InitNode(new Position(ctx));
        ctx.parameterDeclaration().forEach(
                paraDef -> initNode.varDefUnitNodes.add((VarDefUnitNode) visit(paraDef))
        );
        return initNode;
    }

    /**
     * visitParameterDeclaration
     * ------------------------------------------------------------------
     * 单个参数
     * variableType variableDeclaration;
     *
     * @param ctx the parse tree
     * @return varDefUnitNode
     */
    @Override
    public ASTNode visitParameterDeclaration(ParameterDeclarationContext ctx) {
        return new VarDefUnitNode(new Position(ctx),
                (TypeNode) visit(ctx.variableType()),
                ctx.Identifier().toString(),
                (ExprNode) visit(ctx.expression()));
    }

    /**
     * visitConstructFuncDefStatement
     * ------------------------------------------------------------------
     * 类的构造函数
     * Identifier LeftRoundBracket RightRoundBracket suite;
     *
     * @param ctx the parse tree
     * @return constructorDefStmtNode
     */
    @Override
    public ASTNode visitConstructFuncDefStatement(ConstructFuncDefStatementContext ctx) {
        return new ConstructorDefStmtNode(new Position(ctx),
                ctx.Identifier().toString(),
                (BlockStmtNode) visit(ctx.suite()));
    }

    /**
     * visitNewExpr
     * ------------------------------------------------------------------
     * New construction
     * construction:
     * 变量类型 （+ 维度）
     * (Identifier | buildInVariableType)
     * (LeftSquareBracket expression RightSquareBracket)+
     * (LeftSquareBracket RightSquareBracket)*                     #arrayConstruction
     * | (Identifier | buildInVariableType) LeftRoundBracket RightRoundBracket         #varConstruction
     * | (Identifier | buildInVariableType)                                            #varSimpleConstruction
     * ;
     *
     * @param ctx the parse tree
     * @return newExprNode
     */
    @Override
    public ASTNode visitNewExpr(NewExprContext ctx) {
        ConstructionContext context = ctx.construction();
        NewExprNode newExprNode = new NewExprNode(new Position(ctx),
                (TypeNode) visit(context));
        if (context instanceof ArrayConstructionContext) {
            ((ArrayConstructionContext) context).expression().forEach(
                    expr -> newExprNode.dimensions.add((ExprNode) visit(expr))
            );
        }
        return newExprNode;
    }


}
