package frontend;

import ast.ASTNode;
import ast.ExprNode;
import ast.RootNode;
import ast.StmtNode;
import ast.expr.ConstantExprNode.*;
import ast.other.*;
import ast.stmt.*;
import ast.expr.*;
import org.antlr.v4.runtime.CodePointBuffer;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import parser.MxParser;
import parser.MxParser.*;
import parser.MxVisitor;
import utility.Position;
import utility.type.*;

import java.util.Objects;

/**
 * @author F
 * ASTBuilder
 * 继承MxBaseVisitor,遍历parse tree部分结点
 * 构造AST
 */
public class ASTBuilder extends AbstractParseTreeVisitor<ASTNode> implements MxVisitor<ASTNode> {

    /**
     * visitProgram
     * ----------------------------------------------------------------------
     * 访问parse tree的根节点
     * program:(funcDefStatement | declarationStatement|classDeclaration)*
     * 一一添加StmtNode,ClassDefNode
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
        ctx.classDeclaration().forEach(
                classDef -> root.classDefs.add((ClassDefNode) visit(classDef))
        );
        return root;
    }

    @Override
    public ASTNode visitBlockStmt(MxParser.BlockStmtContext ctx) {
        return visit(ctx.suite());
    }

    @Override
    public ASTNode visitIfStmt(MxParser.IfStmtContext ctx) {
        return visit(ctx.selectionStatement());
    }

    @Override
    public ASTNode visitWhileStmt(MxParser.WhileStmtContext ctx) {
        return visit(ctx.whileStatement());
    }

    @Override
    public ASTNode visitForStmt(MxParser.ForStmtContext ctx) {
        return visit(ctx.forStatement());
    }

    @Override
    public ASTNode visitReturnStmt(MxParser.ReturnStmtContext ctx) {
        return visit(ctx.returnStatement());
    }

    @Override
    public ASTNode visitBreakStmt(MxParser.BreakStmtContext ctx) {
        return visit(ctx.breakStatement());
    }

    @Override
    public ASTNode visitContinueStmt(MxParser.ContinueStmtContext ctx) {
        return visit(ctx.continueStatement());
    }

    @Override
    public ASTNode visitVarDefStmt(MxParser.VarDefStmtContext ctx) {
        return visit(ctx.declarationStatement());
    }

    @Override
    public ASTNode visitExprStmt(MxParser.ExprStmtContext ctx) {
        return visit(ctx.expressionStatement());
    }

    @Override
    public ASTNode visitFuncDefStmt(MxParser.FuncDefStmtContext ctx) {
        return visit(ctx.funcDefStatement());
    }

    @Override
    public ASTNode visitConstructorStmt(MxParser.ConstructorStmtContext ctx) {
        return visit(ctx.constructFuncDefStatement());
    }

    @Override
    public ASTNode visitEmptyStmt(EmptyStmtContext ctx) {
        return null;
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
        TypeNode typeNode = (TypeNode) visit(parentCtx.variableType());
        ExprNode expr = null;
        if (ctx.expression() != null) {
            expr = (ExprNode) visit(ctx.expression());
        }
        return new VarDefUnitNode(
                new Position(ctx),
                typeNode,
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
     * initList? Semicolon
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
                (StmtNode) visit(ctx.initializationStatement),
                conditionExpr,
                stepExpr,
                (StmtNode) visit(ctx.statement(1))
        );
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
        ExprStmtNode exprStmtNode = new ExprStmtNode(new Position(ctx));
        ctx.expression().forEach(expr -> exprStmtNode.exprList.add((ExprNode) visit(expr)));
        return exprStmtNode;
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
        InitNode parameter = null;
        if (ctx.funcParameterList() != null) {
            parameter = (InitNode) visit(ctx.funcParameterList());
        }
        return new FuncDefStmtNode(new Position(ctx),
                (TypeNode) visit(ctx.returnType()),
                ctx.Identifier().toString(),
                parameter,
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
        ExprNode initExpr = null;
        if (ctx.expression() != null) {
            initExpr = (ExprNode) visit(ctx.expression());
        }
        return new VarDefUnitNode(new Position(ctx),
                (TypeNode) visit(ctx.variableType()),
                ctx.Identifier().toString(),
                initExpr
        );
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
        NewExprNode newExprNode;
        if (context instanceof ArrayConstructionContext) {
            newExprNode = new NewExprNode(
                    new Position(ctx),
                    ((ArrayConstructionContext) context).LeftSquareBracket().size(),
                    (TypeNode) visit(context));
            ((ArrayConstructionContext) context).expression().forEach(
                    expr -> newExprNode.dimensions.add((ExprNode) visit(expr))
            );
        } else {
            newExprNode = new NewExprNode(
                    new Position(ctx),
                    0,
                    (TypeNode) visit(context));
        }
        return newExprNode;
    }

    /**
     * visitPointer
     * ------------------------------------------------------------------------
     * This
     *
     * @param ctx the parse tree
     * @return pointerExprNode
     */
    @Override
    public ASTNode visitPointer(PointerContext ctx) {
        return new PointerExprNode(
                new Position(ctx),
                new ClassType()
        );
    }

    /**
     * visitConstant
     * -----------------------------------------------------------------------
     * literal 常量表达式
     * True | False
     * | IntegerLiteral
     * | StringLiteral
     * | Null
     * ;
     *
     * @param ctx the parse tree
     * @return constantExprNode
     */
    @Override
    public ASTNode visitConstant(ConstantContext ctx) {
        if (ctx.literal().True() != null) {
            return new BoolConstantExprNode(new Position(ctx), true);
        } else if (ctx.literal().False() != null) {
            return new BoolConstantExprNode(new Position(ctx), false);
        } else if (ctx.literal().IntegerLiteral() != null) {
            return new IntConstantExprNode(new Position(ctx),
                    Integer.parseInt(ctx.literal().IntegerLiteral().toString()));
        } else if (ctx.literal().StringLiteral() != null) {
            return new StrConstantExprNode(new Position(ctx),
                    ctx.literal().StringLiteral().toString());
        } else {
            return new NullConstantExprNode(new Position(ctx));
        }
    }

    /**
     * visitVariableName
     * -------------------------------------------------------------------
     * Identifier
     *
     * @param ctx the parse tree
     * @return varNameExprNode
     */
    @Override
    public ASTNode visitVariableName(VariableNameContext ctx) {
        return new VarNameExprNode(new Position(ctx), ctx.Identifier().toString());
    }

    /**
     * visitMemberVisExpr
     * -----------------------------------------------------------------------
     * expression Dot expression
     * 类成员访问
     *
     * @param ctx the parse tree
     * @return memberVisExprNode
     */
    @Override
    public ASTNode visitMemberVisExpr(MemberVisExprContext ctx) {
        return new MemberVisExprNode(new Position(ctx),
                (ExprNode) visit(ctx.expression(0)),
                (ExprNode) visit(ctx.expression(1)));
    }

    /**
     * visitSuffixExpr
     * ------------------------------------------------------------------------
     * 后缀表达式
     * expression operator = (PlusPlus | MinusMinus)
     *
     * @param ctx the parse tree
     * @return suffixExprNode
     */
    @Override
    public ASTNode visitSuffixExpr(SuffixExprContext ctx) {
        SuffixExprNode.SuffixOperator operator;
        if (ctx.PlusPlus() != null) {
            operator = SuffixExprNode.SuffixOperator.PlusPlus;
        } else {
            operator = SuffixExprNode.SuffixOperator.MinusMinus;
        }
        return new SuffixExprNode(new Position(ctx),
                (ExprNode) visit(ctx.expression()),
                operator
        );
    }

    /**
     * visitBinaryExpr
     * -----------------------------------------------------------------------
     * expression operator=(Multiply | Divide | Mod) expression.
     * expression operator=(Plus | Minus) expression.
     * expression operator=(LeftShift | RightShift) expression.
     * expression operator=And expression.
     * expression operator=Xor expression.
     * expression operator=Or expression.
     *
     * @param ctx the parse tree
     * @return binaryExprNode
     */
    @Override
    public ASTNode visitBinaryExpr(BinaryExprContext ctx) {
        BinaryExprNode.BinaryOperator operator;
        if (ctx.Multiply() != null) {
            operator = BinaryExprNode.BinaryOperator.Multiply;
        } else if (ctx.Divide() != null) {
            operator = BinaryExprNode.BinaryOperator.Divide;
        } else if (ctx.Mod() != null) {
            operator = BinaryExprNode.BinaryOperator.Mod;
        } else if (ctx.Plus() != null) {
            operator = BinaryExprNode.BinaryOperator.Plus;
        } else if (ctx.Minus() != null) {
            operator = BinaryExprNode.BinaryOperator.Minus;
        } else if (ctx.LeftShift() != null) {
            operator = BinaryExprNode.BinaryOperator.LeftShift;
        } else if (ctx.RightShift() != null) {
            operator = BinaryExprNode.BinaryOperator.RightShift;
        } else if (ctx.And() != null) {
            operator = BinaryExprNode.BinaryOperator.And;
        } else if (ctx.Xor() != null) {
            operator = BinaryExprNode.BinaryOperator.Xor;
        } else {
            operator = BinaryExprNode.BinaryOperator.Or;
        }
        return new BinaryExprNode(new Position(ctx),
                (ExprNode) visit(ctx.expression(0)),
                (ExprNode) visit(ctx.expression(1)),
                operator
        );
    }

    /**
     * visitArrayVisExpr
     * --------------------------------------------------------------------------
     * 数组下标访问
     * expression (LeftSquareBracket expression RightSquareBracket)+
     *
     * @param ctx the parse tree
     * @return arrayVisExprNode
     */
    @Override
    public ASTNode visitArrayVisExpr(ArrayVisExprContext ctx) {
        ArrayVisExprNode arrayVisExprNode = new ArrayVisExprNode(new Position(ctx),
                (ExprNode) visit(ctx.expression(0)));
        for (int i = 1; i < ctx.expression().size(); ++i) {
            arrayVisExprNode.indexList.add((ExprNode) visit(ctx.expression(i)));
        }
        return arrayVisExprNode;
    }

    /**
     * visitCmpExpr
     * ------------------------------------------------------------------------------
     * 二元比较表达式
     * expression operator=(Less | LessEqual | Greater | GreaterEqual) expression
     * expression operator=(Equal | NotEqual) expression
     *
     * @param ctx the parse tree
     * @return cmpExprNode
     */
    @Override
    public ASTNode visitCmpExpr(CmpExprContext ctx) {
        CmpExprNode.CmpOperator operator;
        if (ctx.Less() != null) {
            operator = CmpExprNode.CmpOperator.Less;
        } else if (ctx.LessEqual() != null) {
            operator = CmpExprNode.CmpOperator.LessEqual;
        } else if (ctx.Greater() != null) {
            operator = CmpExprNode.CmpOperator.Greater;
        } else if (ctx.GreaterEqual() != null) {
            operator = CmpExprNode.CmpOperator.GreaterEqual;
        } else if (ctx.Equal() != null) {
            operator = CmpExprNode.CmpOperator.Equal;
        } else {
            operator = CmpExprNode.CmpOperator.NotEqual;
        }
        return new CmpExprNode(new Position(ctx),
                (ExprNode) visit(ctx.expression(0)),
                (ExprNode) visit(ctx.expression(1)),
                operator
        );
    }

    /**
     * visitPrefixExpr
     * ------------------------------------------------------------------------------
     * 前缀表达式
     * operator = (PlusPlus | MinusMinus) expression
     * operator = (LogicNot | Not | Minus) expression
     *
     * @param ctx the parse tree
     * @return logicPrefixExprNode\prefixExprNode
     */
    @Override
    public ASTNode visitPrefixExpr(PrefixExprContext ctx) {
        if (ctx.LogicNot() != null) {
            return new LogicPrefixExprNode(new Position(ctx),
                    (ExprNode) visit(ctx.expression()),
                    LogicPrefixExprNode.LogicPrefixOperator.LogicNot
            );
        } else {
            PrefixExprNode.PrefixOperator operator;
            if (ctx.PlusPlus() != null) {
                operator = PrefixExprNode.PrefixOperator.PlusPlus;
            } else if (ctx.MinusMinus() != null) {
                operator = PrefixExprNode.PrefixOperator.MinusMinus;
            } else if (ctx.Not() != null) {
                operator = PrefixExprNode.PrefixOperator.Not;
            } else {
                operator = PrefixExprNode.PrefixOperator.Minus;
            }
            return new PrefixExprNode(new Position(ctx),
                    (ExprNode) visit(ctx.expression()),
                    operator
            );
        }
    }

    /**
     * visitTernaryExpr
     * ----------------------------------------------------------------------------
     * expression Question expression Colon expression
     *
     * @param ctx the parse tree
     * @return ternaryExprNode
     */
    @Override
    public ASTNode visitTernaryExpr(TernaryExprContext ctx) {
        return new TernaryExprNode(new Position(ctx),
                (ExprNode) visit(ctx.expression(0)),
                (ExprNode) visit(ctx.expression(1)),
                (ExprNode) visit(ctx.expression(2))
        );
    }

    /**
     * visitLogicExpr
     * -----------------------------------------------------------------------------
     * 二元逻辑表达式
     * expression operator=AndAnd expression
     * expression operator=OrOr expression
     *
     * @param ctx the parse tree
     * @return logicExprNode
     */
    @Override
    public ASTNode visitLogicExpr(LogicExprContext ctx) {
        LogicExprNode.LogicOperator operator;
        if (ctx.AndAnd() != null) {
            operator = LogicExprNode.LogicOperator.AndAnd;
        } else {
            operator = LogicExprNode.LogicOperator.OrOr;
        }
        return new LogicExprNode(new Position(ctx),
                (ExprNode) visit(ctx.expression(0)),
                (ExprNode) visit(ctx.expression(1)),
                operator
        );
    }

    /**
     * visitFunctionCallExpr
     * ------------------------------------------------------------------------------
     * 函数调用
     * expression LeftRoundBracket parameterList? RightRoundBracket
     *
     * @param ctx the parse tree
     * @return funcCallExprNode
     */
    @Override
    public ASTNode visitFunctionCallExpr(FunctionCallExprContext ctx) {
        return new FuncCallExprNode(new Position(ctx),
                (ExprNode) visit(ctx.expression()),
                (ParameterNode) visit(ctx.parameterList())
        );
    }

    /**
     * visitParenthesisExpr
     * -------------------------------------------------------------------------------
     *
     * @param ctx the parse tree
     * @return exprNode
     */
    @Override
    public ASTNode visitParenthesisExpr(ParenthesisExprContext ctx) {
        return visit(ctx.expression());
    }

    /**
     * visitAssignExpr
     * -------------------------------------------------------------------------------
     * expression Assign expression
     *
     * @param ctx the parse tree
     * @return assignExprNode
     */
    @Override
    public ASTNode visitAssignExpr(AssignExprContext ctx) {
        return new AssignExprNode(new Position(ctx),
                (ExprNode) visit(ctx.expression(0)),
                (ExprNode) visit(ctx.expression(1))
        );
    }

    /**
     * visitArrayConstruction
     * -----------------------------------------------------------------------------
     * 构造数组
     * unitVariableType
     * (LeftSquareBracket expression RightSquareBracket)+
     * (LeftSquareBracket RightSquareBracket)*                 #arrayConstruction
     *
     * @param ctx the parse tree
     * @return typeNode
     */
    @Override
    public ASTNode visitArrayConstruction(ArrayConstructionContext ctx) {
        TypeNode tmp = (TypeNode) visit(ctx.unitVariableType());
        return new TypeNode(new Position(ctx),
                new ArrayType(tmp.type, ctx.LeftSquareBracket().size()));
    }

    /**
     * visitVarConstruction
     * --------------------------------------------------------------------------
     * unitVariableType LeftRoundBracket RightRoundBracket
     *
     * @param ctx the parse tree
     * @return typeNode
     */
    @Override
    public ASTNode visitVarConstruction(VarConstructionContext ctx) {
        return visit(ctx.unitVariableType());
    }

    /**
     * visitVarSimpleConstruction
     * --------------------------------------------------------------------------
     * unitVariableType
     *
     * @param ctx the parse tree
     * @return typeNode
     */
    @Override
    public ASTNode visitVarSimpleConstruction(VarSimpleConstructionContext ctx) {
        return visit(ctx.unitVariableType());
    }

    /**
     * visitReturnType
     * ------------------------------------------------------------------------------------
     * Void
     * | buildInVariableType
     * | Identifier
     * | arrayIdentifier
     * ;
     *
     * @param ctx the parse tree
     * @return typeNode
     */
    @Override
    public ASTNode visitReturnType(ReturnTypeContext ctx) {
        if (ctx.Void() != null) {
            return new TypeNode(new Position(ctx), new VoidType());
        } else if (ctx.buildInVariableType() != null) {
            return visit(ctx.buildInVariableType());
        } else if (ctx.Identifier() != null) {
            return new TypeNode(new Position(ctx), new ClassType(ctx.Identifier().toString()));
        } else {
            return visit(ctx.arrayIdentifier());
        }
    }

    /**
     * visitVariableType
     * --------------------------------------------------------------------------------
     * buildInVariableType
     * | Identifier
     * | arrayIdentifier
     * ;
     *
     * @param ctx the parse tree
     * @return typeNode
     */
    @Override
    public ASTNode visitVariableType(VariableTypeContext ctx) {
        if (ctx.buildInVariableType() != null) {
            return visit(ctx.buildInVariableType());
        } else if (ctx.Identifier() != null) {
            return new TypeNode(new Position(ctx), new ClassType(ctx.Identifier().toString()));
        } else {
            return visit(ctx.arrayIdentifier());
        }
    }

    /**
     * visitUnitVariableType
     * --------------------------------------------------------------------
     * buildInVariableType
     * | Identifier
     *
     * @param ctx the parse tree
     * @return typeNode
     */
    @Override
    public ASTNode visitUnitVariableType(UnitVariableTypeContext ctx) {
        if (ctx.buildInVariableType() != null) {
            return visit(ctx.buildInVariableType());
        } else {
            return new TypeNode(new Position(ctx), new ClassType(ctx.Identifier().toString()));
        }
    }

    /**
     * visitBuildInVariableType
     * ---------------------------------------------------------------------
     * Bool
     * | Int
     * | String
     * ;
     *
     * @param ctx the parse tree
     * @return typeNode
     */
    @Override
    public ASTNode visitBuildInVariableType(BuildInVariableTypeContext ctx) {
        if (ctx.Bool() != null) {
            return new TypeNode(new Position(ctx), new BoolType());
        } else if (ctx.Int() != null) {
            return new TypeNode(new Position(ctx), new IntType());
        } else {
            return new TypeNode(new Position(ctx), new StringType());
        }
    }

    /**
     * visitArrayIdentifier
     * ---------------------------------------------------------------------------
     * arrayIdentifier:
     * (
     * buildInVariableType
     * | Identifier
     * )
     * (LeftSquareBracket RightSquareBracket)+ ;
     *
     * @param ctx the parse tree
     * @return typeNode
     */
    @Override
    public ASTNode visitArrayIdentifier(ArrayIdentifierContext ctx) {
        TypeNode tmp;
        if (ctx.buildInVariableType() != null) {
            tmp = (TypeNode) visit(ctx.buildInVariableType());
        } else {
            tmp = (TypeNode) visit(ctx.Identifier());
        }
        return new TypeNode(new Position(ctx),
                new ArrayType(tmp.type, ctx.LeftSquareBracket().size()));
    }

    /**
     * visitClassDeclaration
     * --------------------------------------------------------------------------
     * Class Identifier LeftCurlyBrace
     * (funcDefStatement | declarationStatement)*
     * constructFuncDefStatement?
     * (funcDefStatement | declarationStatement)*
     * RightCurlyBrace Semicolon
     * ;
     *
     * @param ctx the parse tree
     * @return classDefNode
     */
    @Override
    public ASTNode visitClassDeclaration(ClassDeclarationContext ctx) {
        ClassDefNode classDefNode = new ClassDefNode(new Position(ctx),
                ctx.Identifier().toString());
        if (ctx.constructFuncDefStatement() != null) {
            classDefNode.members.add((StmtNode) visit(ctx.constructFuncDefStatement()));
        }
        ctx.funcDefStatement().forEach(
                stmt -> classDefNode.members.add((StmtNode) visit(stmt))
        );
        ctx.declarationStatement().forEach(
                stmt -> classDefNode.members.add((StmtNode) visit(stmt))
        );
        return classDefNode;
    }

    @Override
    public ASTNode visitLiteral(LiteralContext ctx) {
        return null;
    }
}
