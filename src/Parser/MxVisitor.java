// Generated from F:/repo/Compiler-2023/src/parser\Mx.g4 by ANTLR 4.12.0
package parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link MxParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface MxVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link MxParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(MxParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#declarations}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclarations(MxParser.DeclarationsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code blockStmt}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockStmt(MxParser.BlockStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ifStmt}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStmt(MxParser.IfStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code whileStmt}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileStmt(MxParser.WhileStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code forStmt}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForStmt(MxParser.ForStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code returnStmt}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnStmt(MxParser.ReturnStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code breakStmt}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreakStmt(MxParser.BreakStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code continueStmt}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinueStmt(MxParser.ContinueStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code varDefStmt}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDefStmt(MxParser.VarDefStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprStmt}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprStmt(MxParser.ExprStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code funcDefStmt}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncDefStmt(MxParser.FuncDefStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code constructorStmt}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstructorStmt(MxParser.ConstructorStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code emptyStmt}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyStmt(MxParser.EmptyStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#suite}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSuite(MxParser.SuiteContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#declarationStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclarationStatement(MxParser.DeclarationStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#variableDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDeclaration(MxParser.VariableDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#selectionStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectionStatement(MxParser.SelectionStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#whileStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileStatement(MxParser.WhileStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#forStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForStatement(MxParser.ForStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#returnStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnStatement(MxParser.ReturnStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#breakStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreakStatement(MxParser.BreakStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#continueStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinueStatement(MxParser.ContinueStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#expressionStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionStatement(MxParser.ExpressionStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#parameterList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterList(MxParser.ParameterListContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#funcDefStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncDefStatement(MxParser.FuncDefStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#funcParameterList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncParameterList(MxParser.FuncParameterListContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#parameterDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterDeclaration(MxParser.ParameterDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#constructFuncDefStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstructFuncDefStatement(MxParser.ConstructFuncDefStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code newExpr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNewExpr(MxParser.NewExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code pointer}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPointer(MxParser.PointerContext ctx);
	/**
	 * Visit a parse tree produced by the {@code constant}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstant(MxParser.ConstantContext ctx);
	/**
	 * Visit a parse tree produced by the {@code variableName}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableName(MxParser.VariableNameContext ctx);
	/**
	 * Visit a parse tree produced by the {@code memberVisExpr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberVisExpr(MxParser.MemberVisExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code suffixExpr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSuffixExpr(MxParser.SuffixExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code binaryExpr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryExpr(MxParser.BinaryExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arrayVisExpr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayVisExpr(MxParser.ArrayVisExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code cmpExpr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmpExpr(MxParser.CmpExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code prefixExpr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrefixExpr(MxParser.PrefixExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ternaryExpr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTernaryExpr(MxParser.TernaryExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code logicExpr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicExpr(MxParser.LogicExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code functionCallExpr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCallExpr(MxParser.FunctionCallExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parenthesisExpr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenthesisExpr(MxParser.ParenthesisExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code assignExpr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignExpr(MxParser.AssignExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arrayConstruction}
	 * labeled alternative in {@link MxParser#construction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayConstruction(MxParser.ArrayConstructionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code varConstruction}
	 * labeled alternative in {@link MxParser#construction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarConstruction(MxParser.VarConstructionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code varSimpleConstruction}
	 * labeled alternative in {@link MxParser#construction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarSimpleConstruction(MxParser.VarSimpleConstructionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#arrayUnit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayUnit(MxParser.ArrayUnitContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#returnType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnType(MxParser.ReturnTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#variableType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableType(MxParser.VariableTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#unitVariableType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnitVariableType(MxParser.UnitVariableTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#buildInVariableType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBuildInVariableType(MxParser.BuildInVariableTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#arrayIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayIdentifier(MxParser.ArrayIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#classDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassDeclaration(MxParser.ClassDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(MxParser.LiteralContext ctx);
}