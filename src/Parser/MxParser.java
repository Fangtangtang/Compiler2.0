// Generated from F:/repo/Compiler-2023/src/parser\Mx.g4 by ANTLR 4.12.0
package parser;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class MxParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.12.0", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		Plus=1, Minus=2, Multiply=3, Divide=4, Mod=5, Greater=6, Less=7, GreaterEqual=8, 
		LessEqual=9, NotEqual=10, Equal=11, AndAnd=12, OrOr=13, LogicNot=14, RightShift=15, 
		LeftShift=16, And=17, Or=18, Xor=19, Not=20, Assign=21, PlusPlus=22, MinusMinus=23, 
		Dot=24, LeftSquareBracket=25, RightSquareBracket=26, LeftRoundBracket=27, 
		RightRoundBracket=28, Question=29, Colon=30, Semicolon=31, Comma=32, LeftCurlyBrace=33, 
		RightCurlyBrace=34, Void=35, Bool=36, Int=37, String=38, New=39, Class=40, 
		Null=41, True=42, False=43, This=44, If=45, Else=46, For=47, While=48, 
		Break=49, Continue=50, Return=51, IntegerLiteral=52, StringLiteral=53, 
		WhiteSpace=54, LineBreak=55, LineComment=56, BlockComment=57, Identifier=58;
	public static final int
		RULE_program = 0, RULE_statement = 1, RULE_suite = 2, RULE_declarationStatement = 3, 
		RULE_variableDeclaration = 4, RULE_selectionStatement = 5, RULE_whileStatement = 6, 
		RULE_forStatement = 7, RULE_initList = 8, RULE_returnStatement = 9, RULE_breakStatement = 10, 
		RULE_continueStatement = 11, RULE_expressionStatement = 12, RULE_parameterList = 13, 
		RULE_funcDefStatement = 14, RULE_funcParameterList = 15, RULE_parameterDeclaration = 16, 
		RULE_constructFuncDefStatement = 17, RULE_expression = 18, RULE_construction = 19, 
		RULE_returnType = 20, RULE_variableType = 21, RULE_unitVariableType = 22, 
		RULE_buildInVariableType = 23, RULE_arrayIdentifier = 24, RULE_classDeclaration = 25, 
		RULE_literal = 26;
	private static String[] makeRuleNames() {
		return new String[] {
			"program", "statement", "suite", "declarationStatement", "variableDeclaration", 
			"selectionStatement", "whileStatement", "forStatement", "initList", "returnStatement", 
			"breakStatement", "continueStatement", "expressionStatement", "parameterList", 
			"funcDefStatement", "funcParameterList", "parameterDeclaration", "constructFuncDefStatement", 
			"expression", "construction", "returnType", "variableType", "unitVariableType", 
			"buildInVariableType", "arrayIdentifier", "classDeclaration", "literal"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'+'", "'-'", "'*'", "'/'", "'%'", "'>'", "'<'", "'>='", "'<='", 
			"'!='", "'=='", "'&&'", "'||'", "'!'", "'>>'", "'<<'", "'&'", "'|'", 
			"'^'", "'~'", "'='", "'++'", "'--'", "'.'", "'['", "']'", "'('", "')'", 
			"'?'", "':'", "';'", "','", "'{'", "'}'", "'void'", "'bool'", "'int'", 
			"'string'", "'new'", "'class'", "'null'", "'true'", "'false'", "'this'", 
			"'if'", "'else'", "'for'", "'while'", "'break'", "'continue'", "'return'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "Plus", "Minus", "Multiply", "Divide", "Mod", "Greater", "Less", 
			"GreaterEqual", "LessEqual", "NotEqual", "Equal", "AndAnd", "OrOr", "LogicNot", 
			"RightShift", "LeftShift", "And", "Or", "Xor", "Not", "Assign", "PlusPlus", 
			"MinusMinus", "Dot", "LeftSquareBracket", "RightSquareBracket", "LeftRoundBracket", 
			"RightRoundBracket", "Question", "Colon", "Semicolon", "Comma", "LeftCurlyBrace", 
			"RightCurlyBrace", "Void", "Bool", "Int", "String", "New", "Class", "Null", 
			"True", "False", "This", "If", "Else", "For", "While", "Break", "Continue", 
			"Return", "IntegerLiteral", "StringLiteral", "WhiteSpace", "LineBreak", 
			"LineComment", "BlockComment", "Identifier"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Mx.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public MxParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ProgramContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(MxParser.EOF, 0); }
		public List<FuncDefStatementContext> funcDefStatement() {
			return getRuleContexts(FuncDefStatementContext.class);
		}
		public FuncDefStatementContext funcDefStatement(int i) {
			return getRuleContext(FuncDefStatementContext.class,i);
		}
		public List<DeclarationStatementContext> declarationStatement() {
			return getRuleContexts(DeclarationStatementContext.class);
		}
		public DeclarationStatementContext declarationStatement(int i) {
			return getRuleContext(DeclarationStatementContext.class,i);
		}
		public List<ClassDeclarationContext> classDeclaration() {
			return getRuleContexts(ClassDeclarationContext.class);
		}
		public ClassDeclarationContext classDeclaration(int i) {
			return getRuleContext(ClassDeclarationContext.class,i);
		}
		public ProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitProgram(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgramContext program() throws RecognitionException {
		ProgramContext _localctx = new ProgramContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_program);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(59);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 288231991059415040L) != 0)) {
				{
				setState(57);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
				case 1:
					{
					setState(54);
					funcDefStatement();
					}
					break;
				case 2:
					{
					setState(55);
					declarationStatement();
					}
					break;
				case 3:
					{
					setState(56);
					classDeclaration();
					}
					break;
				}
				}
				setState(61);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(62);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StatementContext extends ParserRuleContext {
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
	 
		public StatementContext() { }
		public void copyFrom(StatementContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ForStmtContext extends StatementContext {
		public ForStatementContext forStatement() {
			return getRuleContext(ForStatementContext.class,0);
		}
		public ForStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitForStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class VarDefStmtContext extends StatementContext {
		public DeclarationStatementContext declarationStatement() {
			return getRuleContext(DeclarationStatementContext.class,0);
		}
		public VarDefStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitVarDefStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ExprStmtContext extends StatementContext {
		public ExpressionStatementContext expressionStatement() {
			return getRuleContext(ExpressionStatementContext.class,0);
		}
		public ExprStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitExprStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class WhileStmtContext extends StatementContext {
		public WhileStatementContext whileStatement() {
			return getRuleContext(WhileStatementContext.class,0);
		}
		public WhileStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitWhileStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IfStmtContext extends StatementContext {
		public SelectionStatementContext selectionStatement() {
			return getRuleContext(SelectionStatementContext.class,0);
		}
		public IfStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitIfStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class BlockStmtContext extends StatementContext {
		public SuiteContext suite() {
			return getRuleContext(SuiteContext.class,0);
		}
		public BlockStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitBlockStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class BreakStmtContext extends StatementContext {
		public BreakStatementContext breakStatement() {
			return getRuleContext(BreakStatementContext.class,0);
		}
		public BreakStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitBreakStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ReturnStmtContext extends StatementContext {
		public ReturnStatementContext returnStatement() {
			return getRuleContext(ReturnStatementContext.class,0);
		}
		public ReturnStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitReturnStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class FuncDefStmtContext extends StatementContext {
		public FuncDefStatementContext funcDefStatement() {
			return getRuleContext(FuncDefStatementContext.class,0);
		}
		public FuncDefStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitFuncDefStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ConstructorStmtContext extends StatementContext {
		public ConstructFuncDefStatementContext constructFuncDefStatement() {
			return getRuleContext(ConstructFuncDefStatementContext.class,0);
		}
		public ConstructorStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitConstructorStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ContinueStmtContext extends StatementContext {
		public ContinueStatementContext continueStatement() {
			return getRuleContext(ContinueStatementContext.class,0);
		}
		public ContinueStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitContinueStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_statement);
		try {
			setState(75);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				_localctx = new BlockStmtContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(64);
				suite();
				}
				break;
			case 2:
				_localctx = new IfStmtContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(65);
				selectionStatement();
				}
				break;
			case 3:
				_localctx = new WhileStmtContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(66);
				whileStatement();
				}
				break;
			case 4:
				_localctx = new ForStmtContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(67);
				forStatement();
				}
				break;
			case 5:
				_localctx = new ReturnStmtContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(68);
				returnStatement();
				}
				break;
			case 6:
				_localctx = new BreakStmtContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(69);
				breakStatement();
				}
				break;
			case 7:
				_localctx = new ContinueStmtContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(70);
				continueStatement();
				}
				break;
			case 8:
				_localctx = new VarDefStmtContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(71);
				declarationStatement();
				}
				break;
			case 9:
				_localctx = new ExprStmtContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(72);
				expressionStatement();
				}
				break;
			case 10:
				_localctx = new FuncDefStmtContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(73);
				funcDefStatement();
				}
				break;
			case 11:
				_localctx = new ConstructorStmtContext(_localctx);
				enterOuterAlt(_localctx, 11);
				{
				setState(74);
				constructFuncDefStatement();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SuiteContext extends ParserRuleContext {
		public TerminalNode LeftCurlyBrace() { return getToken(MxParser.LeftCurlyBrace, 0); }
		public TerminalNode RightCurlyBrace() { return getToken(MxParser.RightCurlyBrace, 0); }
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public SuiteContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_suite; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitSuite(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SuiteContext suite() throws RecognitionException {
		SuiteContext _localctx = new SuiteContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_suite);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(77);
			match(LeftCurlyBrace);
			setState(81);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 306173280783450116L) != 0)) {
				{
				{
				setState(78);
				statement();
				}
				}
				setState(83);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(84);
			match(RightCurlyBrace);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DeclarationStatementContext extends ParserRuleContext {
		public VariableTypeContext variableType() {
			return getRuleContext(VariableTypeContext.class,0);
		}
		public TerminalNode Semicolon() { return getToken(MxParser.Semicolon, 0); }
		public List<VariableDeclarationContext> variableDeclaration() {
			return getRuleContexts(VariableDeclarationContext.class);
		}
		public VariableDeclarationContext variableDeclaration(int i) {
			return getRuleContext(VariableDeclarationContext.class,i);
		}
		public List<TerminalNode> Comma() { return getTokens(MxParser.Comma); }
		public TerminalNode Comma(int i) {
			return getToken(MxParser.Comma, i);
		}
		public DeclarationStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declarationStatement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitDeclarationStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeclarationStatementContext declarationStatement() throws RecognitionException {
		DeclarationStatementContext _localctx = new DeclarationStatementContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_declarationStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(86);
			variableType();
			setState(95);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Identifier) {
				{
				setState(87);
				variableDeclaration();
				setState(92);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==Comma) {
					{
					{
					setState(88);
					match(Comma);
					setState(89);
					variableDeclaration();
					}
					}
					setState(94);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(97);
			match(Semicolon);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VariableDeclarationContext extends ParserRuleContext {
		public ExpressionContext initExpression;
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public TerminalNode Assign() { return getToken(MxParser.Assign, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public VariableDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableDeclaration; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitVariableDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableDeclarationContext variableDeclaration() throws RecognitionException {
		VariableDeclarationContext _localctx = new VariableDeclarationContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_variableDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(99);
			match(Identifier);
			setState(102);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Assign) {
				{
				setState(100);
				match(Assign);
				setState(101);
				((VariableDeclarationContext)_localctx).initExpression = expression(0);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SelectionStatementContext extends ParserRuleContext {
		public ExpressionContext conditionExpression;
		public StatementContext trueStatement;
		public StatementContext falseStatement;
		public TerminalNode If() { return getToken(MxParser.If, 0); }
		public TerminalNode LeftRoundBracket() { return getToken(MxParser.LeftRoundBracket, 0); }
		public TerminalNode RightRoundBracket() { return getToken(MxParser.RightRoundBracket, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public List<TerminalNode> Else() { return getTokens(MxParser.Else); }
		public TerminalNode Else(int i) {
			return getToken(MxParser.Else, i);
		}
		public SelectionStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selectionStatement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitSelectionStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SelectionStatementContext selectionStatement() throws RecognitionException {
		SelectionStatementContext _localctx = new SelectionStatementContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_selectionStatement);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(104);
			match(If);
			setState(105);
			match(LeftRoundBracket);
			setState(106);
			((SelectionStatementContext)_localctx).conditionExpression = expression(0);
			setState(107);
			match(RightRoundBracket);
			setState(108);
			((SelectionStatementContext)_localctx).trueStatement = statement();
			setState(113);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(109);
					match(Else);
					setState(110);
					((SelectionStatementContext)_localctx).falseStatement = statement();
					}
					} 
				}
				setState(115);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class WhileStatementContext extends ParserRuleContext {
		public ExpressionContext conditionExpression;
		public TerminalNode While() { return getToken(MxParser.While, 0); }
		public TerminalNode LeftRoundBracket() { return getToken(MxParser.LeftRoundBracket, 0); }
		public TerminalNode RightRoundBracket() { return getToken(MxParser.RightRoundBracket, 0); }
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public WhileStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whileStatement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitWhileStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WhileStatementContext whileStatement() throws RecognitionException {
		WhileStatementContext _localctx = new WhileStatementContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_whileStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(116);
			match(While);
			setState(117);
			match(LeftRoundBracket);
			setState(118);
			((WhileStatementContext)_localctx).conditionExpression = expression(0);
			setState(119);
			match(RightRoundBracket);
			setState(120);
			statement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ForStatementContext extends ParserRuleContext {
		public ExpressionContext forConditionExpression;
		public ExpressionContext stepExpression;
		public TerminalNode For() { return getToken(MxParser.For, 0); }
		public TerminalNode LeftRoundBracket() { return getToken(MxParser.LeftRoundBracket, 0); }
		public List<TerminalNode> Semicolon() { return getTokens(MxParser.Semicolon); }
		public TerminalNode Semicolon(int i) {
			return getToken(MxParser.Semicolon, i);
		}
		public TerminalNode RightRoundBracket() { return getToken(MxParser.RightRoundBracket, 0); }
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public InitListContext initList() {
			return getRuleContext(InitListContext.class,0);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ForStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forStatement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitForStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForStatementContext forStatement() throws RecognitionException {
		ForStatementContext _localctx = new ForStatementContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_forStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(122);
			match(For);
			setState(123);
			match(LeftRoundBracket);
			setState(125);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 288230857188048896L) != 0)) {
				{
				setState(124);
				initList();
				}
			}

			setState(127);
			match(Semicolon);
			setState(129);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 301774710286336004L) != 0)) {
				{
				setState(128);
				((ForStatementContext)_localctx).forConditionExpression = expression(0);
				}
			}

			setState(131);
			match(Semicolon);
			setState(133);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 301774710286336004L) != 0)) {
				{
				setState(132);
				((ForStatementContext)_localctx).stepExpression = expression(0);
				}
			}

			setState(135);
			match(RightRoundBracket);
			setState(136);
			statement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class InitListContext extends ParserRuleContext {
		public List<VariableDeclarationContext> variableDeclaration() {
			return getRuleContexts(VariableDeclarationContext.class);
		}
		public VariableDeclarationContext variableDeclaration(int i) {
			return getRuleContext(VariableDeclarationContext.class,i);
		}
		public VariableTypeContext variableType() {
			return getRuleContext(VariableTypeContext.class,0);
		}
		public List<TerminalNode> Comma() { return getTokens(MxParser.Comma); }
		public TerminalNode Comma(int i) {
			return getToken(MxParser.Comma, i);
		}
		public InitListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_initList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitInitList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InitListContext initList() throws RecognitionException {
		InitListContext _localctx = new InitListContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_initList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(139);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				{
				setState(138);
				variableType();
				}
				break;
			}
			setState(141);
			variableDeclaration();
			setState(146);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Comma) {
				{
				{
				setState(142);
				match(Comma);
				setState(143);
				variableDeclaration();
				}
				}
				setState(148);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ReturnStatementContext extends ParserRuleContext {
		public TerminalNode Return() { return getToken(MxParser.Return, 0); }
		public TerminalNode Semicolon() { return getToken(MxParser.Semicolon, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ReturnStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_returnStatement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitReturnStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReturnStatementContext returnStatement() throws RecognitionException {
		ReturnStatementContext _localctx = new ReturnStatementContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_returnStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(149);
			match(Return);
			setState(151);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 301774710286336004L) != 0)) {
				{
				setState(150);
				expression(0);
				}
			}

			setState(153);
			match(Semicolon);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BreakStatementContext extends ParserRuleContext {
		public TerminalNode Break() { return getToken(MxParser.Break, 0); }
		public TerminalNode Semicolon() { return getToken(MxParser.Semicolon, 0); }
		public BreakStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_breakStatement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitBreakStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BreakStatementContext breakStatement() throws RecognitionException {
		BreakStatementContext _localctx = new BreakStatementContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_breakStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(155);
			match(Break);
			setState(156);
			match(Semicolon);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ContinueStatementContext extends ParserRuleContext {
		public TerminalNode Continue() { return getToken(MxParser.Continue, 0); }
		public TerminalNode Semicolon() { return getToken(MxParser.Semicolon, 0); }
		public ContinueStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_continueStatement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitContinueStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ContinueStatementContext continueStatement() throws RecognitionException {
		ContinueStatementContext _localctx = new ContinueStatementContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_continueStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(158);
			match(Continue);
			setState(159);
			match(Semicolon);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionStatementContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode Semicolon() { return getToken(MxParser.Semicolon, 0); }
		public ExpressionStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expressionStatement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitExpressionStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionStatementContext expressionStatement() throws RecognitionException {
		ExpressionStatementContext _localctx = new ExpressionStatementContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_expressionStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(161);
			expression(0);
			setState(162);
			match(Semicolon);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ParameterListContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<TerminalNode> Comma() { return getTokens(MxParser.Comma); }
		public TerminalNode Comma(int i) {
			return getToken(MxParser.Comma, i);
		}
		public ParameterListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameterList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitParameterList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParameterListContext parameterList() throws RecognitionException {
		ParameterListContext _localctx = new ParameterListContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_parameterList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(164);
			expression(0);
			setState(169);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Comma) {
				{
				{
				setState(165);
				match(Comma);
				setState(166);
				expression(0);
				}
				}
				setState(171);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FuncDefStatementContext extends ParserRuleContext {
		public SuiteContext functionBody;
		public ReturnTypeContext returnType() {
			return getRuleContext(ReturnTypeContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public TerminalNode LeftRoundBracket() { return getToken(MxParser.LeftRoundBracket, 0); }
		public TerminalNode RightRoundBracket() { return getToken(MxParser.RightRoundBracket, 0); }
		public SuiteContext suite() {
			return getRuleContext(SuiteContext.class,0);
		}
		public FuncParameterListContext funcParameterList() {
			return getRuleContext(FuncParameterListContext.class,0);
		}
		public FuncDefStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_funcDefStatement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitFuncDefStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FuncDefStatementContext funcDefStatement() throws RecognitionException {
		FuncDefStatementContext _localctx = new FuncDefStatementContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_funcDefStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(172);
			returnType();
			setState(173);
			match(Identifier);
			setState(174);
			match(LeftRoundBracket);
			setState(176);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 288230857188048896L) != 0)) {
				{
				setState(175);
				funcParameterList();
				}
			}

			setState(178);
			match(RightRoundBracket);
			setState(179);
			((FuncDefStatementContext)_localctx).functionBody = suite();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FuncParameterListContext extends ParserRuleContext {
		public List<ParameterDeclarationContext> parameterDeclaration() {
			return getRuleContexts(ParameterDeclarationContext.class);
		}
		public ParameterDeclarationContext parameterDeclaration(int i) {
			return getRuleContext(ParameterDeclarationContext.class,i);
		}
		public List<TerminalNode> Comma() { return getTokens(MxParser.Comma); }
		public TerminalNode Comma(int i) {
			return getToken(MxParser.Comma, i);
		}
		public FuncParameterListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_funcParameterList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitFuncParameterList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FuncParameterListContext funcParameterList() throws RecognitionException {
		FuncParameterListContext _localctx = new FuncParameterListContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_funcParameterList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(181);
			parameterDeclaration();
			setState(186);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Comma) {
				{
				{
				setState(182);
				match(Comma);
				setState(183);
				parameterDeclaration();
				}
				}
				setState(188);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ParameterDeclarationContext extends ParserRuleContext {
		public ExpressionContext initExpression;
		public VariableTypeContext variableType() {
			return getRuleContext(VariableTypeContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public TerminalNode Assign() { return getToken(MxParser.Assign, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ParameterDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameterDeclaration; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitParameterDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParameterDeclarationContext parameterDeclaration() throws RecognitionException {
		ParameterDeclarationContext _localctx = new ParameterDeclarationContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_parameterDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(189);
			variableType();
			setState(190);
			match(Identifier);
			setState(193);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Assign) {
				{
				setState(191);
				match(Assign);
				setState(192);
				((ParameterDeclarationContext)_localctx).initExpression = expression(0);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConstructFuncDefStatementContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public TerminalNode LeftRoundBracket() { return getToken(MxParser.LeftRoundBracket, 0); }
		public TerminalNode RightRoundBracket() { return getToken(MxParser.RightRoundBracket, 0); }
		public SuiteContext suite() {
			return getRuleContext(SuiteContext.class,0);
		}
		public ConstructFuncDefStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constructFuncDefStatement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitConstructFuncDefStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstructFuncDefStatementContext constructFuncDefStatement() throws RecognitionException {
		ConstructFuncDefStatementContext _localctx = new ConstructFuncDefStatementContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_constructFuncDefStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(195);
			match(Identifier);
			setState(196);
			match(LeftRoundBracket);
			setState(197);
			match(RightRoundBracket);
			setState(198);
			suite();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionContext extends ParserRuleContext {
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
	 
		public ExpressionContext() { }
		public void copyFrom(ExpressionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NewExprContext extends ExpressionContext {
		public TerminalNode New() { return getToken(MxParser.New, 0); }
		public ConstructionContext construction() {
			return getRuleContext(ConstructionContext.class,0);
		}
		public NewExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitNewExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PointerContext extends ExpressionContext {
		public TerminalNode This() { return getToken(MxParser.This, 0); }
		public PointerContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitPointer(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ConstantContext extends ExpressionContext {
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public ConstantContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitConstant(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class VariableNameContext extends ExpressionContext {
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public VariableNameContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitVariableName(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class MemberVisExprContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode Dot() { return getToken(MxParser.Dot, 0); }
		public MemberVisExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitMemberVisExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class SuffixExprContext extends ExpressionContext {
		public Token operator;
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode PlusPlus() { return getToken(MxParser.PlusPlus, 0); }
		public TerminalNode MinusMinus() { return getToken(MxParser.MinusMinus, 0); }
		public SuffixExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitSuffixExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class BinaryExprContext extends ExpressionContext {
		public Token operator;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode Multiply() { return getToken(MxParser.Multiply, 0); }
		public TerminalNode Divide() { return getToken(MxParser.Divide, 0); }
		public TerminalNode Mod() { return getToken(MxParser.Mod, 0); }
		public TerminalNode Plus() { return getToken(MxParser.Plus, 0); }
		public TerminalNode Minus() { return getToken(MxParser.Minus, 0); }
		public TerminalNode LeftShift() { return getToken(MxParser.LeftShift, 0); }
		public TerminalNode RightShift() { return getToken(MxParser.RightShift, 0); }
		public TerminalNode And() { return getToken(MxParser.And, 0); }
		public TerminalNode Xor() { return getToken(MxParser.Xor, 0); }
		public TerminalNode Or() { return getToken(MxParser.Or, 0); }
		public BinaryExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitBinaryExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ArrayVisExprContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<TerminalNode> LeftSquareBracket() { return getTokens(MxParser.LeftSquareBracket); }
		public TerminalNode LeftSquareBracket(int i) {
			return getToken(MxParser.LeftSquareBracket, i);
		}
		public List<TerminalNode> RightSquareBracket() { return getTokens(MxParser.RightSquareBracket); }
		public TerminalNode RightSquareBracket(int i) {
			return getToken(MxParser.RightSquareBracket, i);
		}
		public ArrayVisExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitArrayVisExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class CmpExprContext extends ExpressionContext {
		public Token operator;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode Less() { return getToken(MxParser.Less, 0); }
		public TerminalNode LessEqual() { return getToken(MxParser.LessEqual, 0); }
		public TerminalNode Greater() { return getToken(MxParser.Greater, 0); }
		public TerminalNode GreaterEqual() { return getToken(MxParser.GreaterEqual, 0); }
		public TerminalNode Equal() { return getToken(MxParser.Equal, 0); }
		public TerminalNode NotEqual() { return getToken(MxParser.NotEqual, 0); }
		public CmpExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitCmpExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PrefixExprContext extends ExpressionContext {
		public Token operator;
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode PlusPlus() { return getToken(MxParser.PlusPlus, 0); }
		public TerminalNode MinusMinus() { return getToken(MxParser.MinusMinus, 0); }
		public TerminalNode LogicNot() { return getToken(MxParser.LogicNot, 0); }
		public TerminalNode Not() { return getToken(MxParser.Not, 0); }
		public TerminalNode Minus() { return getToken(MxParser.Minus, 0); }
		public PrefixExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitPrefixExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class TernaryExprContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode Question() { return getToken(MxParser.Question, 0); }
		public TerminalNode Colon() { return getToken(MxParser.Colon, 0); }
		public TernaryExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitTernaryExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class LogicExprContext extends ExpressionContext {
		public Token operator;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode AndAnd() { return getToken(MxParser.AndAnd, 0); }
		public TerminalNode OrOr() { return getToken(MxParser.OrOr, 0); }
		public LogicExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitLogicExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class FunctionCallExprContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode LeftRoundBracket() { return getToken(MxParser.LeftRoundBracket, 0); }
		public TerminalNode RightRoundBracket() { return getToken(MxParser.RightRoundBracket, 0); }
		public ParameterListContext parameterList() {
			return getRuleContext(ParameterListContext.class,0);
		}
		public FunctionCallExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitFunctionCallExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ParenthesisExprContext extends ExpressionContext {
		public TerminalNode LeftRoundBracket() { return getToken(MxParser.LeftRoundBracket, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RightRoundBracket() { return getToken(MxParser.RightRoundBracket, 0); }
		public ParenthesisExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitParenthesisExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AssignExprContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode Assign() { return getToken(MxParser.Assign, 0); }
		public AssignExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitAssignExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		return expression(0);
	}

	private ExpressionContext expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
		ExpressionContext _prevctx = _localctx;
		int _startState = 36;
		enterRecursionRule(_localctx, 36, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(214);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LeftRoundBracket:
				{
				_localctx = new ParenthesisExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(201);
				match(LeftRoundBracket);
				setState(202);
				expression(0);
				setState(203);
				match(RightRoundBracket);
				}
				break;
			case Null:
			case True:
			case False:
			case IntegerLiteral:
			case StringLiteral:
				{
				_localctx = new ConstantContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(205);
				literal();
				}
				break;
			case Identifier:
				{
				_localctx = new VariableNameContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(206);
				match(Identifier);
				}
				break;
			case This:
				{
				_localctx = new PointerContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(207);
				match(This);
				}
				break;
			case New:
				{
				_localctx = new NewExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(208);
				match(New);
				setState(209);
				construction();
				}
				break;
			case PlusPlus:
			case MinusMinus:
				{
				_localctx = new PrefixExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(210);
				((PrefixExprContext)_localctx).operator = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==PlusPlus || _la==MinusMinus) ) {
					((PrefixExprContext)_localctx).operator = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(211);
				expression(14);
				}
				break;
			case Minus:
			case LogicNot:
			case Not:
				{
				_localctx = new PrefixExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(212);
				((PrefixExprContext)_localctx).operator = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 1064964L) != 0)) ) {
					((PrefixExprContext)_localctx).operator = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(213);
				expression(13);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(277);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,22,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(275);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
					case 1:
						{
						_localctx = new MemberVisExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(216);
						if (!(precpred(_ctx, 18))) throw new FailedPredicateException(this, "precpred(_ctx, 18)");
						setState(217);
						match(Dot);
						setState(218);
						expression(19);
						}
						break;
					case 2:
						{
						_localctx = new BinaryExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(219);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(220);
						((BinaryExprContext)_localctx).operator = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 56L) != 0)) ) {
							((BinaryExprContext)_localctx).operator = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(221);
						expression(13);
						}
						break;
					case 3:
						{
						_localctx = new BinaryExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(222);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(223);
						((BinaryExprContext)_localctx).operator = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==Plus || _la==Minus) ) {
							((BinaryExprContext)_localctx).operator = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(224);
						expression(12);
						}
						break;
					case 4:
						{
						_localctx = new BinaryExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(225);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(226);
						((BinaryExprContext)_localctx).operator = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==RightShift || _la==LeftShift) ) {
							((BinaryExprContext)_localctx).operator = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(227);
						expression(11);
						}
						break;
					case 5:
						{
						_localctx = new CmpExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(228);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(229);
						((CmpExprContext)_localctx).operator = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 960L) != 0)) ) {
							((CmpExprContext)_localctx).operator = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(230);
						expression(10);
						}
						break;
					case 6:
						{
						_localctx = new CmpExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(231);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(232);
						((CmpExprContext)_localctx).operator = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==NotEqual || _la==Equal) ) {
							((CmpExprContext)_localctx).operator = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(233);
						expression(9);
						}
						break;
					case 7:
						{
						_localctx = new BinaryExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(234);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(235);
						((BinaryExprContext)_localctx).operator = match(And);
						setState(236);
						expression(8);
						}
						break;
					case 8:
						{
						_localctx = new BinaryExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(237);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(238);
						((BinaryExprContext)_localctx).operator = match(Xor);
						setState(239);
						expression(7);
						}
						break;
					case 9:
						{
						_localctx = new BinaryExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(240);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(241);
						((BinaryExprContext)_localctx).operator = match(Or);
						setState(242);
						expression(6);
						}
						break;
					case 10:
						{
						_localctx = new LogicExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(243);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(244);
						((LogicExprContext)_localctx).operator = match(AndAnd);
						setState(245);
						expression(5);
						}
						break;
					case 11:
						{
						_localctx = new LogicExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(246);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(247);
						((LogicExprContext)_localctx).operator = match(OrOr);
						setState(248);
						expression(4);
						}
						break;
					case 12:
						{
						_localctx = new TernaryExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(249);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(250);
						match(Question);
						setState(251);
						expression(0);
						setState(252);
						match(Colon);
						setState(253);
						expression(2);
						}
						break;
					case 13:
						{
						_localctx = new AssignExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(255);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(256);
						match(Assign);
						setState(257);
						expression(1);
						}
						break;
					case 14:
						{
						_localctx = new FunctionCallExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(258);
						if (!(precpred(_ctx, 19))) throw new FailedPredicateException(this, "precpred(_ctx, 19)");
						setState(259);
						match(LeftRoundBracket);
						setState(261);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 301774710286336004L) != 0)) {
							{
							setState(260);
							parameterList();
							}
						}

						setState(263);
						match(RightRoundBracket);
						}
						break;
					case 15:
						{
						_localctx = new ArrayVisExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(264);
						if (!(precpred(_ctx, 17))) throw new FailedPredicateException(this, "precpred(_ctx, 17)");
						setState(269); 
						_errHandler.sync(this);
						_alt = 1;
						do {
							switch (_alt) {
							case 1:
								{
								{
								setState(265);
								match(LeftSquareBracket);
								setState(266);
								expression(0);
								setState(267);
								match(RightSquareBracket);
								}
								}
								break;
							default:
								throw new NoViableAltException(this);
							}
							setState(271); 
							_errHandler.sync(this);
							_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
						} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
						}
						break;
					case 16:
						{
						_localctx = new SuffixExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(273);
						if (!(precpred(_ctx, 16))) throw new FailedPredicateException(this, "precpred(_ctx, 16)");
						setState(274);
						((SuffixExprContext)_localctx).operator = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==PlusPlus || _la==MinusMinus) ) {
							((SuffixExprContext)_localctx).operator = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						}
						break;
					}
					} 
				}
				setState(279);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,22,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConstructionContext extends ParserRuleContext {
		public ConstructionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_construction; }
	 
		public ConstructionContext() { }
		public void copyFrom(ConstructionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class VarConstructionContext extends ConstructionContext {
		public UnitVariableTypeContext unitVariableType() {
			return getRuleContext(UnitVariableTypeContext.class,0);
		}
		public TerminalNode LeftRoundBracket() { return getToken(MxParser.LeftRoundBracket, 0); }
		public TerminalNode RightRoundBracket() { return getToken(MxParser.RightRoundBracket, 0); }
		public VarConstructionContext(ConstructionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitVarConstruction(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class VarSimpleConstructionContext extends ConstructionContext {
		public UnitVariableTypeContext unitVariableType() {
			return getRuleContext(UnitVariableTypeContext.class,0);
		}
		public VarSimpleConstructionContext(ConstructionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitVarSimpleConstruction(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ArrayConstructionContext extends ConstructionContext {
		public UnitVariableTypeContext unitVariableType() {
			return getRuleContext(UnitVariableTypeContext.class,0);
		}
		public List<TerminalNode> LeftSquareBracket() { return getTokens(MxParser.LeftSquareBracket); }
		public TerminalNode LeftSquareBracket(int i) {
			return getToken(MxParser.LeftSquareBracket, i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<TerminalNode> RightSquareBracket() { return getTokens(MxParser.RightSquareBracket); }
		public TerminalNode RightSquareBracket(int i) {
			return getToken(MxParser.RightSquareBracket, i);
		}
		public ArrayConstructionContext(ConstructionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitArrayConstruction(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstructionContext construction() throws RecognitionException {
		ConstructionContext _localctx = new ConstructionContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_construction);
		try {
			int _alt;
			setState(301);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,25,_ctx) ) {
			case 1:
				_localctx = new ArrayConstructionContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(280);
				unitVariableType();
				setState(285); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(281);
						match(LeftSquareBracket);
						setState(282);
						expression(0);
						setState(283);
						match(RightSquareBracket);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(287); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,23,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				setState(293);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,24,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(289);
						match(LeftSquareBracket);
						setState(290);
						match(RightSquareBracket);
						}
						} 
					}
					setState(295);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,24,_ctx);
				}
				}
				break;
			case 2:
				_localctx = new VarConstructionContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(296);
				unitVariableType();
				setState(297);
				match(LeftRoundBracket);
				setState(298);
				match(RightRoundBracket);
				}
				break;
			case 3:
				_localctx = new VarSimpleConstructionContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(300);
				unitVariableType();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ReturnTypeContext extends ParserRuleContext {
		public TerminalNode Void() { return getToken(MxParser.Void, 0); }
		public BuildInVariableTypeContext buildInVariableType() {
			return getRuleContext(BuildInVariableTypeContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public ArrayIdentifierContext arrayIdentifier() {
			return getRuleContext(ArrayIdentifierContext.class,0);
		}
		public ReturnTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_returnType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitReturnType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReturnTypeContext returnType() throws RecognitionException {
		ReturnTypeContext _localctx = new ReturnTypeContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_returnType);
		try {
			setState(307);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,26,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(303);
				match(Void);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(304);
				buildInVariableType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(305);
				match(Identifier);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(306);
				arrayIdentifier();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VariableTypeContext extends ParserRuleContext {
		public BuildInVariableTypeContext buildInVariableType() {
			return getRuleContext(BuildInVariableTypeContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public ArrayIdentifierContext arrayIdentifier() {
			return getRuleContext(ArrayIdentifierContext.class,0);
		}
		public VariableTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitVariableType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableTypeContext variableType() throws RecognitionException {
		VariableTypeContext _localctx = new VariableTypeContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_variableType);
		try {
			setState(312);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(309);
				buildInVariableType();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(310);
				match(Identifier);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(311);
				arrayIdentifier();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class UnitVariableTypeContext extends ParserRuleContext {
		public BuildInVariableTypeContext buildInVariableType() {
			return getRuleContext(BuildInVariableTypeContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public UnitVariableTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unitVariableType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitUnitVariableType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnitVariableTypeContext unitVariableType() throws RecognitionException {
		UnitVariableTypeContext _localctx = new UnitVariableTypeContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_unitVariableType);
		try {
			setState(316);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Bool:
			case Int:
			case String:
				enterOuterAlt(_localctx, 1);
				{
				setState(314);
				buildInVariableType();
				}
				break;
			case Identifier:
				enterOuterAlt(_localctx, 2);
				{
				setState(315);
				match(Identifier);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BuildInVariableTypeContext extends ParserRuleContext {
		public TerminalNode Bool() { return getToken(MxParser.Bool, 0); }
		public TerminalNode Int() { return getToken(MxParser.Int, 0); }
		public TerminalNode String() { return getToken(MxParser.String, 0); }
		public BuildInVariableTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_buildInVariableType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitBuildInVariableType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BuildInVariableTypeContext buildInVariableType() throws RecognitionException {
		BuildInVariableTypeContext _localctx = new BuildInVariableTypeContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_buildInVariableType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(318);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 481036337152L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArrayIdentifierContext extends ParserRuleContext {
		public BuildInVariableTypeContext buildInVariableType() {
			return getRuleContext(BuildInVariableTypeContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public List<TerminalNode> LeftSquareBracket() { return getTokens(MxParser.LeftSquareBracket); }
		public TerminalNode LeftSquareBracket(int i) {
			return getToken(MxParser.LeftSquareBracket, i);
		}
		public List<TerminalNode> RightSquareBracket() { return getTokens(MxParser.RightSquareBracket); }
		public TerminalNode RightSquareBracket(int i) {
			return getToken(MxParser.RightSquareBracket, i);
		}
		public ArrayIdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayIdentifier; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitArrayIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayIdentifierContext arrayIdentifier() throws RecognitionException {
		ArrayIdentifierContext _localctx = new ArrayIdentifierContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_arrayIdentifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(322);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Bool:
			case Int:
			case String:
				{
				setState(320);
				buildInVariableType();
				}
				break;
			case Identifier:
				{
				setState(321);
				match(Identifier);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(326); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(324);
				match(LeftSquareBracket);
				setState(325);
				match(RightSquareBracket);
				}
				}
				setState(328); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==LeftSquareBracket );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ClassDeclarationContext extends ParserRuleContext {
		public TerminalNode Class() { return getToken(MxParser.Class, 0); }
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public TerminalNode LeftCurlyBrace() { return getToken(MxParser.LeftCurlyBrace, 0); }
		public TerminalNode RightCurlyBrace() { return getToken(MxParser.RightCurlyBrace, 0); }
		public TerminalNode Semicolon() { return getToken(MxParser.Semicolon, 0); }
		public List<FuncDefStatementContext> funcDefStatement() {
			return getRuleContexts(FuncDefStatementContext.class);
		}
		public FuncDefStatementContext funcDefStatement(int i) {
			return getRuleContext(FuncDefStatementContext.class,i);
		}
		public List<DeclarationStatementContext> declarationStatement() {
			return getRuleContexts(DeclarationStatementContext.class);
		}
		public DeclarationStatementContext declarationStatement(int i) {
			return getRuleContext(DeclarationStatementContext.class,i);
		}
		public ConstructFuncDefStatementContext constructFuncDefStatement() {
			return getRuleContext(ConstructFuncDefStatementContext.class,0);
		}
		public ClassDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classDeclaration; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitClassDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassDeclarationContext classDeclaration() throws RecognitionException {
		ClassDeclarationContext _localctx = new ClassDeclarationContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_classDeclaration);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(330);
			match(Class);
			setState(331);
			match(Identifier);
			setState(332);
			match(LeftCurlyBrace);
			setState(337);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,32,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(335);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,31,_ctx) ) {
					case 1:
						{
						setState(333);
						funcDefStatement();
						}
						break;
					case 2:
						{
						setState(334);
						declarationStatement();
						}
						break;
					}
					} 
				}
				setState(339);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,32,_ctx);
			}
			setState(341);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,33,_ctx) ) {
			case 1:
				{
				setState(340);
				constructFuncDefStatement();
				}
				break;
			}
			setState(347);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 288230891547787264L) != 0)) {
				{
				setState(345);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,34,_ctx) ) {
				case 1:
					{
					setState(343);
					funcDefStatement();
					}
					break;
				case 2:
					{
					setState(344);
					declarationStatement();
					}
					break;
				}
				}
				setState(349);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(350);
			match(RightCurlyBrace);
			setState(351);
			match(Semicolon);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LiteralContext extends ParserRuleContext {
		public TerminalNode True() { return getToken(MxParser.True, 0); }
		public TerminalNode False() { return getToken(MxParser.False, 0); }
		public TerminalNode IntegerLiteral() { return getToken(MxParser.IntegerLiteral, 0); }
		public TerminalNode StringLiteral() { return getToken(MxParser.StringLiteral, 0); }
		public TerminalNode Null() { return getToken(MxParser.Null, 0); }
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_literal);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(353);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 13526192044900352L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 18:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 18);
		case 1:
			return precpred(_ctx, 12);
		case 2:
			return precpred(_ctx, 11);
		case 3:
			return precpred(_ctx, 10);
		case 4:
			return precpred(_ctx, 9);
		case 5:
			return precpred(_ctx, 8);
		case 6:
			return precpred(_ctx, 7);
		case 7:
			return precpred(_ctx, 6);
		case 8:
			return precpred(_ctx, 5);
		case 9:
			return precpred(_ctx, 4);
		case 10:
			return precpred(_ctx, 3);
		case 11:
			return precpred(_ctx, 2);
		case 12:
			return precpred(_ctx, 1);
		case 13:
			return precpred(_ctx, 19);
		case 14:
			return precpred(_ctx, 17);
		case 15:
			return precpred(_ctx, 16);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001:\u0164\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"+
		"\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007\u0018"+
		"\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0005\u0000:\b\u0000\n\u0000\f\u0000=\t\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0003\u0001L\b\u0001\u0001\u0002\u0001\u0002\u0005\u0002P\b\u0002\n\u0002"+
		"\f\u0002S\t\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0005\u0003[\b\u0003\n\u0003\f\u0003^\t\u0003\u0003"+
		"\u0003`\b\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0003\u0004g\b\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0005\u0005p\b\u0005\n\u0005"+
		"\f\u0005s\t\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001"+
		"\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0003\u0007~\b"+
		"\u0007\u0001\u0007\u0001\u0007\u0003\u0007\u0082\b\u0007\u0001\u0007\u0001"+
		"\u0007\u0003\u0007\u0086\b\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001"+
		"\b\u0003\b\u008c\b\b\u0001\b\u0001\b\u0001\b\u0005\b\u0091\b\b\n\b\f\b"+
		"\u0094\t\b\u0001\t\u0001\t\u0003\t\u0098\b\t\u0001\t\u0001\t\u0001\n\u0001"+
		"\n\u0001\n\u0001\u000b\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001\f"+
		"\u0001\r\u0001\r\u0001\r\u0005\r\u00a8\b\r\n\r\f\r\u00ab\t\r\u0001\u000e"+
		"\u0001\u000e\u0001\u000e\u0001\u000e\u0003\u000e\u00b1\b\u000e\u0001\u000e"+
		"\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001\u000f\u0005\u000f"+
		"\u00b9\b\u000f\n\u000f\f\u000f\u00bc\t\u000f\u0001\u0010\u0001\u0010\u0001"+
		"\u0010\u0001\u0010\u0003\u0010\u00c2\b\u0010\u0001\u0011\u0001\u0011\u0001"+
		"\u0011\u0001\u0011\u0001\u0011\u0001\u0012\u0001\u0012\u0001\u0012\u0001"+
		"\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001"+
		"\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0003\u0012\u00d7"+
		"\b\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001"+
		"\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001"+
		"\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001"+
		"\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001"+
		"\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001"+
		"\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001"+
		"\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001"+
		"\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0003\u0012\u0106\b\u0012\u0001"+
		"\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0004"+
		"\u0012\u010e\b\u0012\u000b\u0012\f\u0012\u010f\u0001\u0012\u0001\u0012"+
		"\u0005\u0012\u0114\b\u0012\n\u0012\f\u0012\u0117\t\u0012\u0001\u0013\u0001"+
		"\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0004\u0013\u011e\b\u0013\u000b"+
		"\u0013\f\u0013\u011f\u0001\u0013\u0001\u0013\u0005\u0013\u0124\b\u0013"+
		"\n\u0013\f\u0013\u0127\t\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001"+
		"\u0013\u0001\u0013\u0003\u0013\u012e\b\u0013\u0001\u0014\u0001\u0014\u0001"+
		"\u0014\u0001\u0014\u0003\u0014\u0134\b\u0014\u0001\u0015\u0001\u0015\u0001"+
		"\u0015\u0003\u0015\u0139\b\u0015\u0001\u0016\u0001\u0016\u0003\u0016\u013d"+
		"\b\u0016\u0001\u0017\u0001\u0017\u0001\u0018\u0001\u0018\u0003\u0018\u0143"+
		"\b\u0018\u0001\u0018\u0001\u0018\u0004\u0018\u0147\b\u0018\u000b\u0018"+
		"\f\u0018\u0148\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019"+
		"\u0005\u0019\u0150\b\u0019\n\u0019\f\u0019\u0153\t\u0019\u0001\u0019\u0003"+
		"\u0019\u0156\b\u0019\u0001\u0019\u0001\u0019\u0005\u0019\u015a\b\u0019"+
		"\n\u0019\f\u0019\u015d\t\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001"+
		"\u001a\u0001\u001a\u0001\u001a\u0000\u0001$\u001b\u0000\u0002\u0004\u0006"+
		"\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e \"$&(*,."+
		"024\u0000\t\u0001\u0000\u0016\u0017\u0003\u0000\u0002\u0002\u000e\u000e"+
		"\u0014\u0014\u0001\u0000\u0003\u0005\u0001\u0000\u0001\u0002\u0001\u0000"+
		"\u000f\u0010\u0001\u0000\u0006\t\u0001\u0000\n\u000b\u0001\u0000$&\u0002"+
		"\u0000)+45\u018d\u0000;\u0001\u0000\u0000\u0000\u0002K\u0001\u0000\u0000"+
		"\u0000\u0004M\u0001\u0000\u0000\u0000\u0006V\u0001\u0000\u0000\u0000\b"+
		"c\u0001\u0000\u0000\u0000\nh\u0001\u0000\u0000\u0000\ft\u0001\u0000\u0000"+
		"\u0000\u000ez\u0001\u0000\u0000\u0000\u0010\u008b\u0001\u0000\u0000\u0000"+
		"\u0012\u0095\u0001\u0000\u0000\u0000\u0014\u009b\u0001\u0000\u0000\u0000"+
		"\u0016\u009e\u0001\u0000\u0000\u0000\u0018\u00a1\u0001\u0000\u0000\u0000"+
		"\u001a\u00a4\u0001\u0000\u0000\u0000\u001c\u00ac\u0001\u0000\u0000\u0000"+
		"\u001e\u00b5\u0001\u0000\u0000\u0000 \u00bd\u0001\u0000\u0000\u0000\""+
		"\u00c3\u0001\u0000\u0000\u0000$\u00d6\u0001\u0000\u0000\u0000&\u012d\u0001"+
		"\u0000\u0000\u0000(\u0133\u0001\u0000\u0000\u0000*\u0138\u0001\u0000\u0000"+
		"\u0000,\u013c\u0001\u0000\u0000\u0000.\u013e\u0001\u0000\u0000\u00000"+
		"\u0142\u0001\u0000\u0000\u00002\u014a\u0001\u0000\u0000\u00004\u0161\u0001"+
		"\u0000\u0000\u00006:\u0003\u001c\u000e\u00007:\u0003\u0006\u0003\u0000"+
		"8:\u00032\u0019\u000096\u0001\u0000\u0000\u000097\u0001\u0000\u0000\u0000"+
		"98\u0001\u0000\u0000\u0000:=\u0001\u0000\u0000\u0000;9\u0001\u0000\u0000"+
		"\u0000;<\u0001\u0000\u0000\u0000<>\u0001\u0000\u0000\u0000=;\u0001\u0000"+
		"\u0000\u0000>?\u0005\u0000\u0000\u0001?\u0001\u0001\u0000\u0000\u0000"+
		"@L\u0003\u0004\u0002\u0000AL\u0003\n\u0005\u0000BL\u0003\f\u0006\u0000"+
		"CL\u0003\u000e\u0007\u0000DL\u0003\u0012\t\u0000EL\u0003\u0014\n\u0000"+
		"FL\u0003\u0016\u000b\u0000GL\u0003\u0006\u0003\u0000HL\u0003\u0018\f\u0000"+
		"IL\u0003\u001c\u000e\u0000JL\u0003\"\u0011\u0000K@\u0001\u0000\u0000\u0000"+
		"KA\u0001\u0000\u0000\u0000KB\u0001\u0000\u0000\u0000KC\u0001\u0000\u0000"+
		"\u0000KD\u0001\u0000\u0000\u0000KE\u0001\u0000\u0000\u0000KF\u0001\u0000"+
		"\u0000\u0000KG\u0001\u0000\u0000\u0000KH\u0001\u0000\u0000\u0000KI\u0001"+
		"\u0000\u0000\u0000KJ\u0001\u0000\u0000\u0000L\u0003\u0001\u0000\u0000"+
		"\u0000MQ\u0005!\u0000\u0000NP\u0003\u0002\u0001\u0000ON\u0001\u0000\u0000"+
		"\u0000PS\u0001\u0000\u0000\u0000QO\u0001\u0000\u0000\u0000QR\u0001\u0000"+
		"\u0000\u0000RT\u0001\u0000\u0000\u0000SQ\u0001\u0000\u0000\u0000TU\u0005"+
		"\"\u0000\u0000U\u0005\u0001\u0000\u0000\u0000V_\u0003*\u0015\u0000W\\"+
		"\u0003\b\u0004\u0000XY\u0005 \u0000\u0000Y[\u0003\b\u0004\u0000ZX\u0001"+
		"\u0000\u0000\u0000[^\u0001\u0000\u0000\u0000\\Z\u0001\u0000\u0000\u0000"+
		"\\]\u0001\u0000\u0000\u0000]`\u0001\u0000\u0000\u0000^\\\u0001\u0000\u0000"+
		"\u0000_W\u0001\u0000\u0000\u0000_`\u0001\u0000\u0000\u0000`a\u0001\u0000"+
		"\u0000\u0000ab\u0005\u001f\u0000\u0000b\u0007\u0001\u0000\u0000\u0000"+
		"cf\u0005:\u0000\u0000de\u0005\u0015\u0000\u0000eg\u0003$\u0012\u0000f"+
		"d\u0001\u0000\u0000\u0000fg\u0001\u0000\u0000\u0000g\t\u0001\u0000\u0000"+
		"\u0000hi\u0005-\u0000\u0000ij\u0005\u001b\u0000\u0000jk\u0003$\u0012\u0000"+
		"kl\u0005\u001c\u0000\u0000lq\u0003\u0002\u0001\u0000mn\u0005.\u0000\u0000"+
		"np\u0003\u0002\u0001\u0000om\u0001\u0000\u0000\u0000ps\u0001\u0000\u0000"+
		"\u0000qo\u0001\u0000\u0000\u0000qr\u0001\u0000\u0000\u0000r\u000b\u0001"+
		"\u0000\u0000\u0000sq\u0001\u0000\u0000\u0000tu\u00050\u0000\u0000uv\u0005"+
		"\u001b\u0000\u0000vw\u0003$\u0012\u0000wx\u0005\u001c\u0000\u0000xy\u0003"+
		"\u0002\u0001\u0000y\r\u0001\u0000\u0000\u0000z{\u0005/\u0000\u0000{}\u0005"+
		"\u001b\u0000\u0000|~\u0003\u0010\b\u0000}|\u0001\u0000\u0000\u0000}~\u0001"+
		"\u0000\u0000\u0000~\u007f\u0001\u0000\u0000\u0000\u007f\u0081\u0005\u001f"+
		"\u0000\u0000\u0080\u0082\u0003$\u0012\u0000\u0081\u0080\u0001\u0000\u0000"+
		"\u0000\u0081\u0082\u0001\u0000\u0000\u0000\u0082\u0083\u0001\u0000\u0000"+
		"\u0000\u0083\u0085\u0005\u001f\u0000\u0000\u0084\u0086\u0003$\u0012\u0000"+
		"\u0085\u0084\u0001\u0000\u0000\u0000\u0085\u0086\u0001\u0000\u0000\u0000"+
		"\u0086\u0087\u0001\u0000\u0000\u0000\u0087\u0088\u0005\u001c\u0000\u0000"+
		"\u0088\u0089\u0003\u0002\u0001\u0000\u0089\u000f\u0001\u0000\u0000\u0000"+
		"\u008a\u008c\u0003*\u0015\u0000\u008b\u008a\u0001\u0000\u0000\u0000\u008b"+
		"\u008c\u0001\u0000\u0000\u0000\u008c\u008d\u0001\u0000\u0000\u0000\u008d"+
		"\u0092\u0003\b\u0004\u0000\u008e\u008f\u0005 \u0000\u0000\u008f\u0091"+
		"\u0003\b\u0004\u0000\u0090\u008e\u0001\u0000\u0000\u0000\u0091\u0094\u0001"+
		"\u0000\u0000\u0000\u0092\u0090\u0001\u0000\u0000\u0000\u0092\u0093\u0001"+
		"\u0000\u0000\u0000\u0093\u0011\u0001\u0000\u0000\u0000\u0094\u0092\u0001"+
		"\u0000\u0000\u0000\u0095\u0097\u00053\u0000\u0000\u0096\u0098\u0003$\u0012"+
		"\u0000\u0097\u0096\u0001\u0000\u0000\u0000\u0097\u0098\u0001\u0000\u0000"+
		"\u0000\u0098\u0099\u0001\u0000\u0000\u0000\u0099\u009a\u0005\u001f\u0000"+
		"\u0000\u009a\u0013\u0001\u0000\u0000\u0000\u009b\u009c\u00051\u0000\u0000"+
		"\u009c\u009d\u0005\u001f\u0000\u0000\u009d\u0015\u0001\u0000\u0000\u0000"+
		"\u009e\u009f\u00052\u0000\u0000\u009f\u00a0\u0005\u001f\u0000\u0000\u00a0"+
		"\u0017\u0001\u0000\u0000\u0000\u00a1\u00a2\u0003$\u0012\u0000\u00a2\u00a3"+
		"\u0005\u001f\u0000\u0000\u00a3\u0019\u0001\u0000\u0000\u0000\u00a4\u00a9"+
		"\u0003$\u0012\u0000\u00a5\u00a6\u0005 \u0000\u0000\u00a6\u00a8\u0003$"+
		"\u0012\u0000\u00a7\u00a5\u0001\u0000\u0000\u0000\u00a8\u00ab\u0001\u0000"+
		"\u0000\u0000\u00a9\u00a7\u0001\u0000\u0000\u0000\u00a9\u00aa\u0001\u0000"+
		"\u0000\u0000\u00aa\u001b\u0001\u0000\u0000\u0000\u00ab\u00a9\u0001\u0000"+
		"\u0000\u0000\u00ac\u00ad\u0003(\u0014\u0000\u00ad\u00ae\u0005:\u0000\u0000"+
		"\u00ae\u00b0\u0005\u001b\u0000\u0000\u00af\u00b1\u0003\u001e\u000f\u0000"+
		"\u00b0\u00af\u0001\u0000\u0000\u0000\u00b0\u00b1\u0001\u0000\u0000\u0000"+
		"\u00b1\u00b2\u0001\u0000\u0000\u0000\u00b2\u00b3\u0005\u001c\u0000\u0000"+
		"\u00b3\u00b4\u0003\u0004\u0002\u0000\u00b4\u001d\u0001\u0000\u0000\u0000"+
		"\u00b5\u00ba\u0003 \u0010\u0000\u00b6\u00b7\u0005 \u0000\u0000\u00b7\u00b9"+
		"\u0003 \u0010\u0000\u00b8\u00b6\u0001\u0000\u0000\u0000\u00b9\u00bc\u0001"+
		"\u0000\u0000\u0000\u00ba\u00b8\u0001\u0000\u0000\u0000\u00ba\u00bb\u0001"+
		"\u0000\u0000\u0000\u00bb\u001f\u0001\u0000\u0000\u0000\u00bc\u00ba\u0001"+
		"\u0000\u0000\u0000\u00bd\u00be\u0003*\u0015\u0000\u00be\u00c1\u0005:\u0000"+
		"\u0000\u00bf\u00c0\u0005\u0015\u0000\u0000\u00c0\u00c2\u0003$\u0012\u0000"+
		"\u00c1\u00bf\u0001\u0000\u0000\u0000\u00c1\u00c2\u0001\u0000\u0000\u0000"+
		"\u00c2!\u0001\u0000\u0000\u0000\u00c3\u00c4\u0005:\u0000\u0000\u00c4\u00c5"+
		"\u0005\u001b\u0000\u0000\u00c5\u00c6\u0005\u001c\u0000\u0000\u00c6\u00c7"+
		"\u0003\u0004\u0002\u0000\u00c7#\u0001\u0000\u0000\u0000\u00c8\u00c9\u0006"+
		"\u0012\uffff\uffff\u0000\u00c9\u00ca\u0005\u001b\u0000\u0000\u00ca\u00cb"+
		"\u0003$\u0012\u0000\u00cb\u00cc\u0005\u001c\u0000\u0000\u00cc\u00d7\u0001"+
		"\u0000\u0000\u0000\u00cd\u00d7\u00034\u001a\u0000\u00ce\u00d7\u0005:\u0000"+
		"\u0000\u00cf\u00d7\u0005,\u0000\u0000\u00d0\u00d1\u0005\'\u0000\u0000"+
		"\u00d1\u00d7\u0003&\u0013\u0000\u00d2\u00d3\u0007\u0000\u0000\u0000\u00d3"+
		"\u00d7\u0003$\u0012\u000e\u00d4\u00d5\u0007\u0001\u0000\u0000\u00d5\u00d7"+
		"\u0003$\u0012\r\u00d6\u00c8\u0001\u0000\u0000\u0000\u00d6\u00cd\u0001"+
		"\u0000\u0000\u0000\u00d6\u00ce\u0001\u0000\u0000\u0000\u00d6\u00cf\u0001"+
		"\u0000\u0000\u0000\u00d6\u00d0\u0001\u0000\u0000\u0000\u00d6\u00d2\u0001"+
		"\u0000\u0000\u0000\u00d6\u00d4\u0001\u0000\u0000\u0000\u00d7\u0115\u0001"+
		"\u0000\u0000\u0000\u00d8\u00d9\n\u0012\u0000\u0000\u00d9\u00da\u0005\u0018"+
		"\u0000\u0000\u00da\u0114\u0003$\u0012\u0013\u00db\u00dc\n\f\u0000\u0000"+
		"\u00dc\u00dd\u0007\u0002\u0000\u0000\u00dd\u0114\u0003$\u0012\r\u00de"+
		"\u00df\n\u000b\u0000\u0000\u00df\u00e0\u0007\u0003\u0000\u0000\u00e0\u0114"+
		"\u0003$\u0012\f\u00e1\u00e2\n\n\u0000\u0000\u00e2\u00e3\u0007\u0004\u0000"+
		"\u0000\u00e3\u0114\u0003$\u0012\u000b\u00e4\u00e5\n\t\u0000\u0000\u00e5"+
		"\u00e6\u0007\u0005\u0000\u0000\u00e6\u0114\u0003$\u0012\n\u00e7\u00e8"+
		"\n\b\u0000\u0000\u00e8\u00e9\u0007\u0006\u0000\u0000\u00e9\u0114\u0003"+
		"$\u0012\t\u00ea\u00eb\n\u0007\u0000\u0000\u00eb\u00ec\u0005\u0011\u0000"+
		"\u0000\u00ec\u0114\u0003$\u0012\b\u00ed\u00ee\n\u0006\u0000\u0000\u00ee"+
		"\u00ef\u0005\u0013\u0000\u0000\u00ef\u0114\u0003$\u0012\u0007\u00f0\u00f1"+
		"\n\u0005\u0000\u0000\u00f1\u00f2\u0005\u0012\u0000\u0000\u00f2\u0114\u0003"+
		"$\u0012\u0006\u00f3\u00f4\n\u0004\u0000\u0000\u00f4\u00f5\u0005\f\u0000"+
		"\u0000\u00f5\u0114\u0003$\u0012\u0005\u00f6\u00f7\n\u0003\u0000\u0000"+
		"\u00f7\u00f8\u0005\r\u0000\u0000\u00f8\u0114\u0003$\u0012\u0004\u00f9"+
		"\u00fa\n\u0002\u0000\u0000\u00fa\u00fb\u0005\u001d\u0000\u0000\u00fb\u00fc"+
		"\u0003$\u0012\u0000\u00fc\u00fd\u0005\u001e\u0000\u0000\u00fd\u00fe\u0003"+
		"$\u0012\u0002\u00fe\u0114\u0001\u0000\u0000\u0000\u00ff\u0100\n\u0001"+
		"\u0000\u0000\u0100\u0101\u0005\u0015\u0000\u0000\u0101\u0114\u0003$\u0012"+
		"\u0001\u0102\u0103\n\u0013\u0000\u0000\u0103\u0105\u0005\u001b\u0000\u0000"+
		"\u0104\u0106\u0003\u001a\r\u0000\u0105\u0104\u0001\u0000\u0000\u0000\u0105"+
		"\u0106\u0001\u0000\u0000\u0000\u0106\u0107\u0001\u0000\u0000\u0000\u0107"+
		"\u0114\u0005\u001c\u0000\u0000\u0108\u010d\n\u0011\u0000\u0000\u0109\u010a"+
		"\u0005\u0019\u0000\u0000\u010a\u010b\u0003$\u0012\u0000\u010b\u010c\u0005"+
		"\u001a\u0000\u0000\u010c\u010e\u0001\u0000\u0000\u0000\u010d\u0109\u0001"+
		"\u0000\u0000\u0000\u010e\u010f\u0001\u0000\u0000\u0000\u010f\u010d\u0001"+
		"\u0000\u0000\u0000\u010f\u0110\u0001\u0000\u0000\u0000\u0110\u0114\u0001"+
		"\u0000\u0000\u0000\u0111\u0112\n\u0010\u0000\u0000\u0112\u0114\u0007\u0000"+
		"\u0000\u0000\u0113\u00d8\u0001\u0000\u0000\u0000\u0113\u00db\u0001\u0000"+
		"\u0000\u0000\u0113\u00de\u0001\u0000\u0000\u0000\u0113\u00e1\u0001\u0000"+
		"\u0000\u0000\u0113\u00e4\u0001\u0000\u0000\u0000\u0113\u00e7\u0001\u0000"+
		"\u0000\u0000\u0113\u00ea\u0001\u0000\u0000\u0000\u0113\u00ed\u0001\u0000"+
		"\u0000\u0000\u0113\u00f0\u0001\u0000\u0000\u0000\u0113\u00f3\u0001\u0000"+
		"\u0000\u0000\u0113\u00f6\u0001\u0000\u0000\u0000\u0113\u00f9\u0001\u0000"+
		"\u0000\u0000\u0113\u00ff\u0001\u0000\u0000\u0000\u0113\u0102\u0001\u0000"+
		"\u0000\u0000\u0113\u0108\u0001\u0000\u0000\u0000\u0113\u0111\u0001\u0000"+
		"\u0000\u0000\u0114\u0117\u0001\u0000\u0000\u0000\u0115\u0113\u0001\u0000"+
		"\u0000\u0000\u0115\u0116\u0001\u0000\u0000\u0000\u0116%\u0001\u0000\u0000"+
		"\u0000\u0117\u0115\u0001\u0000\u0000\u0000\u0118\u011d\u0003,\u0016\u0000"+
		"\u0119\u011a\u0005\u0019\u0000\u0000\u011a\u011b\u0003$\u0012\u0000\u011b"+
		"\u011c\u0005\u001a\u0000\u0000\u011c\u011e\u0001\u0000\u0000\u0000\u011d"+
		"\u0119\u0001\u0000\u0000\u0000\u011e\u011f\u0001\u0000\u0000\u0000\u011f"+
		"\u011d\u0001\u0000\u0000\u0000\u011f\u0120\u0001\u0000\u0000\u0000\u0120"+
		"\u0125\u0001\u0000\u0000\u0000\u0121\u0122\u0005\u0019\u0000\u0000\u0122"+
		"\u0124\u0005\u001a\u0000\u0000\u0123\u0121\u0001\u0000\u0000\u0000\u0124"+
		"\u0127\u0001\u0000\u0000\u0000\u0125\u0123\u0001\u0000\u0000\u0000\u0125"+
		"\u0126\u0001\u0000\u0000\u0000\u0126\u012e\u0001\u0000\u0000\u0000\u0127"+
		"\u0125\u0001\u0000\u0000\u0000\u0128\u0129\u0003,\u0016\u0000\u0129\u012a"+
		"\u0005\u001b\u0000\u0000\u012a\u012b\u0005\u001c\u0000\u0000\u012b\u012e"+
		"\u0001\u0000\u0000\u0000\u012c\u012e\u0003,\u0016\u0000\u012d\u0118\u0001"+
		"\u0000\u0000\u0000\u012d\u0128\u0001\u0000\u0000\u0000\u012d\u012c\u0001"+
		"\u0000\u0000\u0000\u012e\'\u0001\u0000\u0000\u0000\u012f\u0134\u0005#"+
		"\u0000\u0000\u0130\u0134\u0003.\u0017\u0000\u0131\u0134\u0005:\u0000\u0000"+
		"\u0132\u0134\u00030\u0018\u0000\u0133\u012f\u0001\u0000\u0000\u0000\u0133"+
		"\u0130\u0001\u0000\u0000\u0000\u0133\u0131\u0001\u0000\u0000\u0000\u0133"+
		"\u0132\u0001\u0000\u0000\u0000\u0134)\u0001\u0000\u0000\u0000\u0135\u0139"+
		"\u0003.\u0017\u0000\u0136\u0139\u0005:\u0000\u0000\u0137\u0139\u00030"+
		"\u0018\u0000\u0138\u0135\u0001\u0000\u0000\u0000\u0138\u0136\u0001\u0000"+
		"\u0000\u0000\u0138\u0137\u0001\u0000\u0000\u0000\u0139+\u0001\u0000\u0000"+
		"\u0000\u013a\u013d\u0003.\u0017\u0000\u013b\u013d\u0005:\u0000\u0000\u013c"+
		"\u013a\u0001\u0000\u0000\u0000\u013c\u013b\u0001\u0000\u0000\u0000\u013d"+
		"-\u0001\u0000\u0000\u0000\u013e\u013f\u0007\u0007\u0000\u0000\u013f/\u0001"+
		"\u0000\u0000\u0000\u0140\u0143\u0003.\u0017\u0000\u0141\u0143\u0005:\u0000"+
		"\u0000\u0142\u0140\u0001\u0000\u0000\u0000\u0142\u0141\u0001\u0000\u0000"+
		"\u0000\u0143\u0146\u0001\u0000\u0000\u0000\u0144\u0145\u0005\u0019\u0000"+
		"\u0000\u0145\u0147\u0005\u001a\u0000\u0000\u0146\u0144\u0001\u0000\u0000"+
		"\u0000\u0147\u0148\u0001\u0000\u0000\u0000\u0148\u0146\u0001\u0000\u0000"+
		"\u0000\u0148\u0149\u0001\u0000\u0000\u0000\u01491\u0001\u0000\u0000\u0000"+
		"\u014a\u014b\u0005(\u0000\u0000\u014b\u014c\u0005:\u0000\u0000\u014c\u0151"+
		"\u0005!\u0000\u0000\u014d\u0150\u0003\u001c\u000e\u0000\u014e\u0150\u0003"+
		"\u0006\u0003\u0000\u014f\u014d\u0001\u0000\u0000\u0000\u014f\u014e\u0001"+
		"\u0000\u0000\u0000\u0150\u0153\u0001\u0000\u0000\u0000\u0151\u014f\u0001"+
		"\u0000\u0000\u0000\u0151\u0152\u0001\u0000\u0000\u0000\u0152\u0155\u0001"+
		"\u0000\u0000\u0000\u0153\u0151\u0001\u0000\u0000\u0000\u0154\u0156\u0003"+
		"\"\u0011\u0000\u0155\u0154\u0001\u0000\u0000\u0000\u0155\u0156\u0001\u0000"+
		"\u0000\u0000\u0156\u015b\u0001\u0000\u0000\u0000\u0157\u015a\u0003\u001c"+
		"\u000e\u0000\u0158\u015a\u0003\u0006\u0003\u0000\u0159\u0157\u0001\u0000"+
		"\u0000\u0000\u0159\u0158\u0001\u0000\u0000\u0000\u015a\u015d\u0001\u0000"+
		"\u0000\u0000\u015b\u0159\u0001\u0000\u0000\u0000\u015b\u015c\u0001\u0000"+
		"\u0000\u0000\u015c\u015e\u0001\u0000\u0000\u0000\u015d\u015b\u0001\u0000"+
		"\u0000\u0000\u015e\u015f\u0005\"\u0000\u0000\u015f\u0160\u0005\u001f\u0000"+
		"\u0000\u01603\u0001\u0000\u0000\u0000\u0161\u0162\u0007\b\u0000\u0000"+
		"\u01625\u0001\u0000\u0000\u0000$9;KQ\\_fq}\u0081\u0085\u008b\u0092\u0097"+
		"\u00a9\u00b0\u00ba\u00c1\u00d6\u0105\u010f\u0113\u0115\u011f\u0125\u012d"+
		"\u0133\u0138\u013c\u0142\u0148\u014f\u0151\u0155\u0159\u015b";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}