// Generated from F:/repo/Compiler-2023/Parser\Mx.g4 by ANTLR 4.12.0
package Parser;
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
		RULE_program = 0, RULE_declaration = 1, RULE_constructor = 2, RULE_functionDeclaration = 3, 
		RULE_funcParameterList = 4, RULE_statement = 5, RULE_suite = 6, RULE_declarationStatement = 7, 
		RULE_selectionStatement = 8, RULE_loopStatement = 9, RULE_initializationStatement = 10, 
		RULE_jumpStatement = 11, RULE_expressionStatement = 12, RULE_parameterList = 13, 
		RULE_expression = 14, RULE_construction = 15, RULE_returnType = 16, RULE_variableType = 17, 
		RULE_buildInVariableType = 18, RULE_arrayIdentifier = 19, RULE_classIdentifier = 20, 
		RULE_literal = 21;
	private static String[] makeRuleNames() {
		return new String[] {
			"program", "declaration", "constructor", "functionDeclaration", "funcParameterList", 
			"statement", "suite", "declarationStatement", "selectionStatement", "loopStatement", 
			"initializationStatement", "jumpStatement", "expressionStatement", "parameterList", 
			"expression", "construction", "returnType", "variableType", "buildInVariableType", 
			"arrayIdentifier", "classIdentifier", "literal"
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
		public List<DeclarationContext> declaration() {
			return getRuleContexts(DeclarationContext.class);
		}
		public DeclarationContext declaration(int i) {
			return getRuleContext(DeclarationContext.class,i);
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
			setState(47);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 288231991059415040L) != 0)) {
				{
				{
				setState(44);
				declaration();
				}
				}
				setState(49);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(50);
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
	public static class DeclarationContext extends ParserRuleContext {
		public FunctionDeclarationContext functionDeclaration() {
			return getRuleContext(FunctionDeclarationContext.class,0);
		}
		public DeclarationStatementContext declarationStatement() {
			return getRuleContext(DeclarationStatementContext.class,0);
		}
		public DeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declaration; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeclarationContext declaration() throws RecognitionException {
		DeclarationContext _localctx = new DeclarationContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_declaration);
		try {
			setState(54);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(52);
				functionDeclaration();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(53);
				declarationStatement();
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
	public static class ConstructorContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public TerminalNode LeftRoundBracket() { return getToken(MxParser.LeftRoundBracket, 0); }
		public TerminalNode RightRoundBracket() { return getToken(MxParser.RightRoundBracket, 0); }
		public SuiteContext suite() {
			return getRuleContext(SuiteContext.class,0);
		}
		public ConstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constructor; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstructorContext constructor() throws RecognitionException {
		ConstructorContext _localctx = new ConstructorContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_constructor);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(56);
			match(Identifier);
			setState(57);
			match(LeftRoundBracket);
			setState(58);
			match(RightRoundBracket);
			setState(59);
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
	public static class FunctionDeclarationContext extends ParserRuleContext {
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
		public List<FuncParameterListContext> funcParameterList() {
			return getRuleContexts(FuncParameterListContext.class);
		}
		public FuncParameterListContext funcParameterList(int i) {
			return getRuleContext(FuncParameterListContext.class,i);
		}
		public FunctionDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionDeclaration; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitFunctionDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionDeclarationContext functionDeclaration() throws RecognitionException {
		FunctionDeclarationContext _localctx = new FunctionDeclarationContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_functionDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(61);
			returnType();
			setState(62);
			match(Identifier);
			setState(63);
			match(LeftRoundBracket);
			setState(67);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 288231956699676672L) != 0)) {
				{
				{
				setState(64);
				funcParameterList();
				}
				}
				setState(69);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(70);
			match(RightRoundBracket);
			setState(71);
			((FunctionDeclarationContext)_localctx).functionBody = suite();
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
		public List<VariableTypeContext> variableType() {
			return getRuleContexts(VariableTypeContext.class);
		}
		public VariableTypeContext variableType(int i) {
			return getRuleContext(VariableTypeContext.class,i);
		}
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
		enterRule(_localctx, 8, RULE_funcParameterList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(73);
			variableType();
			setState(74);
			expression(0);
			setState(81);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Comma) {
				{
				{
				setState(75);
				match(Comma);
				setState(76);
				variableType();
				setState(77);
				expression(0);
				}
				}
				setState(83);
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
	public static class JumpStmtContext extends StatementContext {
		public JumpStatementContext jumpStatement() {
			return getRuleContext(JumpStatementContext.class,0);
		}
		public JumpStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitJumpStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class LoopStmtContext extends StatementContext {
		public LoopStatementContext loopStatement() {
			return getRuleContext(LoopStatementContext.class,0);
		}
		public LoopStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitLoopStmt(this);
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

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_statement);
		try {
			setState(90);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				_localctx = new BlockStmtContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(84);
				suite();
				}
				break;
			case 2:
				_localctx = new IfStmtContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(85);
				selectionStatement();
				}
				break;
			case 3:
				_localctx = new LoopStmtContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(86);
				loopStatement();
				}
				break;
			case 4:
				_localctx = new JumpStmtContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(87);
				jumpStatement();
				}
				break;
			case 5:
				_localctx = new VarDefStmtContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(88);
				declarationStatement();
				}
				break;
			case 6:
				_localctx = new ExprStmtContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(89);
				expressionStatement();
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
		enterRule(_localctx, 12, RULE_suite);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(92);
			match(LeftCurlyBrace);
			setState(96);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 306174345935339524L) != 0)) {
				{
				{
				setState(93);
				statement();
				}
				}
				setState(98);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(99);
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
		public ExpressionContext variableDeclarationExpression;
		public VariableTypeContext variableType() {
			return getRuleContext(VariableTypeContext.class,0);
		}
		public TerminalNode Semicolon() { return getToken(MxParser.Semicolon, 0); }
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
		enterRule(_localctx, 14, RULE_declarationStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(101);
			variableType();
			setState(110);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 301774710286336004L) != 0)) {
				{
				setState(102);
				((DeclarationStatementContext)_localctx).variableDeclarationExpression = expression(0);
				setState(107);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==Comma) {
					{
					{
					setState(103);
					match(Comma);
					setState(104);
					((DeclarationStatementContext)_localctx).variableDeclarationExpression = expression(0);
					}
					}
					setState(109);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(112);
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
		enterRule(_localctx, 16, RULE_selectionStatement);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(114);
			match(If);
			setState(115);
			match(LeftRoundBracket);
			setState(116);
			((SelectionStatementContext)_localctx).conditionExpression = expression(0);
			setState(117);
			match(RightRoundBracket);
			setState(118);
			((SelectionStatementContext)_localctx).trueStatement = statement();
			setState(123);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(119);
					match(Else);
					setState(120);
					((SelectionStatementContext)_localctx).falseStatement = statement();
					}
					} 
				}
				setState(125);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
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
	public static class LoopStatementContext extends ParserRuleContext {
		public LoopStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_loopStatement; }
	 
		public LoopStatementContext() { }
		public void copyFrom(LoopStatementContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ForStmtContext extends LoopStatementContext {
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
		public InitializationStatementContext initializationStatement() {
			return getRuleContext(InitializationStatementContext.class,0);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ForStmtContext(LoopStatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitForStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class WhileStmtContext extends LoopStatementContext {
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
		public WhileStmtContext(LoopStatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitWhileStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LoopStatementContext loopStatement() throws RecognitionException {
		LoopStatementContext _localctx = new LoopStatementContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_loopStatement);
		int _la;
		try {
			setState(147);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case While:
				_localctx = new WhileStmtContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(126);
				match(While);
				setState(127);
				match(LeftRoundBracket);
				setState(128);
				((WhileStmtContext)_localctx).conditionExpression = expression(0);
				setState(129);
				match(RightRoundBracket);
				setState(130);
				statement();
				}
				break;
			case For:
				_localctx = new ForStmtContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(132);
				match(For);
				setState(133);
				match(LeftRoundBracket);
				setState(135);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 301776290834300932L) != 0)) {
					{
					setState(134);
					initializationStatement();
					}
				}

				setState(137);
				match(Semicolon);
				setState(139);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 301774710286336004L) != 0)) {
					{
					setState(138);
					((ForStmtContext)_localctx).forConditionExpression = expression(0);
					}
				}

				setState(141);
				match(Semicolon);
				setState(143);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 301774710286336004L) != 0)) {
					{
					setState(142);
					((ForStmtContext)_localctx).stepExpression = expression(0);
					}
				}

				setState(145);
				match(RightRoundBracket);
				setState(146);
				statement();
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
	public static class InitializationStatementContext extends ParserRuleContext {
		public ExpressionContext variableDeclarationExpression;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public VariableTypeContext variableType() {
			return getRuleContext(VariableTypeContext.class,0);
		}
		public List<TerminalNode> Comma() { return getTokens(MxParser.Comma); }
		public TerminalNode Comma(int i) {
			return getToken(MxParser.Comma, i);
		}
		public InitializationStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_initializationStatement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitInitializationStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InitializationStatementContext initializationStatement() throws RecognitionException {
		InitializationStatementContext _localctx = new InitializationStatementContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_initializationStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(150);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
			case 1:
				{
				setState(149);
				variableType();
				}
				break;
			}
			setState(152);
			((InitializationStatementContext)_localctx).variableDeclarationExpression = expression(0);
			setState(157);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Comma) {
				{
				{
				setState(153);
				match(Comma);
				setState(154);
				((InitializationStatementContext)_localctx).variableDeclarationExpression = expression(0);
				}
				}
				setState(159);
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
	public static class JumpStatementContext extends ParserRuleContext {
		public TerminalNode Return() { return getToken(MxParser.Return, 0); }
		public TerminalNode Semicolon() { return getToken(MxParser.Semicolon, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode Break() { return getToken(MxParser.Break, 0); }
		public TerminalNode Continue() { return getToken(MxParser.Continue, 0); }
		public JumpStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jumpStatement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitJumpStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JumpStatementContext jumpStatement() throws RecognitionException {
		JumpStatementContext _localctx = new JumpStatementContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_jumpStatement);
		int _la;
		try {
			setState(169);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Return:
				enterOuterAlt(_localctx, 1);
				{
				setState(160);
				match(Return);
				setState(162);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 301774710286336004L) != 0)) {
					{
					setState(161);
					expression(0);
					}
				}

				setState(164);
				match(Semicolon);
				}
				break;
			case Break:
				enterOuterAlt(_localctx, 2);
				{
				setState(165);
				match(Break);
				setState(166);
				match(Semicolon);
				}
				break;
			case Continue:
				enterOuterAlt(_localctx, 3);
				{
				setState(167);
				match(Continue);
				setState(168);
				match(Semicolon);
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
			setState(171);
			expression(0);
			setState(172);
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
			setState(174);
			expression(0);
			setState(179);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Comma) {
				{
				{
				setState(175);
				match(Comma);
				setState(176);
				expression(0);
				}
				}
				setState(181);
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
		public TerminalNode Less() { return getToken(MxParser.Less, 0); }
		public TerminalNode LessEqual() { return getToken(MxParser.LessEqual, 0); }
		public TerminalNode Greater() { return getToken(MxParser.Greater, 0); }
		public TerminalNode GreaterEqual() { return getToken(MxParser.GreaterEqual, 0); }
		public TerminalNode Equal() { return getToken(MxParser.Equal, 0); }
		public TerminalNode NotEqual() { return getToken(MxParser.NotEqual, 0); }
		public TerminalNode And() { return getToken(MxParser.And, 0); }
		public TerminalNode Xor() { return getToken(MxParser.Xor, 0); }
		public TerminalNode Or() { return getToken(MxParser.Or, 0); }
		public TerminalNode AndAnd() { return getToken(MxParser.AndAnd, 0); }
		public TerminalNode OrOr() { return getToken(MxParser.OrOr, 0); }
		public BinaryExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitBinaryExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NestificationExprContext extends ExpressionContext {
		public TerminalNode LeftRoundBracket() { return getToken(MxParser.LeftRoundBracket, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RightRoundBracket() { return getToken(MxParser.RightRoundBracket, 0); }
		public NestificationExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitNestificationExpr(this);
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
	public static class VariableContext extends ExpressionContext {
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public VariableContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitVariable(this);
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
		int _startState = 28;
		enterRecursionRule(_localctx, 28, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(196);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LeftRoundBracket:
				{
				_localctx = new NestificationExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(183);
				match(LeftRoundBracket);
				setState(184);
				expression(0);
				setState(185);
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
				setState(187);
				literal();
				}
				break;
			case Identifier:
				{
				_localctx = new VariableContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(188);
				match(Identifier);
				}
				break;
			case This:
				{
				_localctx = new PointerContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(189);
				match(This);
				}
				break;
			case New:
				{
				_localctx = new NewExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(190);
				match(New);
				setState(191);
				construction();
				}
				break;
			case PlusPlus:
			case MinusMinus:
				{
				_localctx = new PrefixExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(192);
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
				setState(193);
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
				setState(194);
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
				setState(195);
				expression(13);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(259);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,22,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(257);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
					case 1:
						{
						_localctx = new MemberVisExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(198);
						if (!(precpred(_ctx, 18))) throw new FailedPredicateException(this, "precpred(_ctx, 18)");
						setState(199);
						match(Dot);
						setState(200);
						expression(19);
						}
						break;
					case 2:
						{
						_localctx = new BinaryExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(201);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(202);
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
						setState(203);
						expression(13);
						}
						break;
					case 3:
						{
						_localctx = new BinaryExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(204);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(205);
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
						setState(206);
						expression(12);
						}
						break;
					case 4:
						{
						_localctx = new BinaryExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(207);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(208);
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
						setState(209);
						expression(11);
						}
						break;
					case 5:
						{
						_localctx = new BinaryExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(210);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(211);
						((BinaryExprContext)_localctx).operator = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 960L) != 0)) ) {
							((BinaryExprContext)_localctx).operator = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(212);
						expression(10);
						}
						break;
					case 6:
						{
						_localctx = new BinaryExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(213);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(214);
						((BinaryExprContext)_localctx).operator = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==NotEqual || _la==Equal) ) {
							((BinaryExprContext)_localctx).operator = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(215);
						expression(9);
						}
						break;
					case 7:
						{
						_localctx = new BinaryExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(216);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(217);
						((BinaryExprContext)_localctx).operator = match(And);
						setState(218);
						expression(8);
						}
						break;
					case 8:
						{
						_localctx = new BinaryExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(219);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(220);
						((BinaryExprContext)_localctx).operator = match(Xor);
						setState(221);
						expression(7);
						}
						break;
					case 9:
						{
						_localctx = new BinaryExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(222);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(223);
						((BinaryExprContext)_localctx).operator = match(Or);
						setState(224);
						expression(6);
						}
						break;
					case 10:
						{
						_localctx = new BinaryExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(225);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(226);
						((BinaryExprContext)_localctx).operator = match(AndAnd);
						setState(227);
						expression(5);
						}
						break;
					case 11:
						{
						_localctx = new BinaryExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(228);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(229);
						((BinaryExprContext)_localctx).operator = match(OrOr);
						setState(230);
						expression(4);
						}
						break;
					case 12:
						{
						_localctx = new TernaryExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(231);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(232);
						match(Question);
						setState(233);
						expression(0);
						setState(234);
						match(Colon);
						setState(235);
						expression(2);
						}
						break;
					case 13:
						{
						_localctx = new AssignExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(237);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(238);
						match(Assign);
						setState(239);
						expression(1);
						}
						break;
					case 14:
						{
						_localctx = new FunctionCallExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(240);
						if (!(precpred(_ctx, 19))) throw new FailedPredicateException(this, "precpred(_ctx, 19)");
						setState(241);
						match(LeftRoundBracket);
						setState(243);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 301774710286336004L) != 0)) {
							{
							setState(242);
							parameterList();
							}
						}

						setState(245);
						match(RightRoundBracket);
						}
						break;
					case 15:
						{
						_localctx = new ArrayVisExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(246);
						if (!(precpred(_ctx, 17))) throw new FailedPredicateException(this, "precpred(_ctx, 17)");
						setState(251); 
						_errHandler.sync(this);
						_alt = 1;
						do {
							switch (_alt) {
							case 1:
								{
								{
								setState(247);
								match(LeftSquareBracket);
								setState(248);
								expression(0);
								setState(249);
								match(RightSquareBracket);
								}
								}
								break;
							default:
								throw new NoViableAltException(this);
							}
							setState(253); 
							_errHandler.sync(this);
							_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
						} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
						}
						break;
					case 16:
						{
						_localctx = new SuffixExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(255);
						if (!(precpred(_ctx, 16))) throw new FailedPredicateException(this, "precpred(_ctx, 16)");
						setState(256);
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
				setState(261);
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
		public TerminalNode LeftRoundBracket() { return getToken(MxParser.LeftRoundBracket, 0); }
		public TerminalNode RightRoundBracket() { return getToken(MxParser.RightRoundBracket, 0); }
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public BuildInVariableTypeContext buildInVariableType() {
			return getRuleContext(BuildInVariableTypeContext.class,0);
		}
		public VarConstructionContext(ConstructionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitVarConstruction(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class VarSimpleConstructionContext extends ConstructionContext {
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public BuildInVariableTypeContext buildInVariableType() {
			return getRuleContext(BuildInVariableTypeContext.class,0);
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
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public BuildInVariableTypeContext buildInVariableType() {
			return getRuleContext(BuildInVariableTypeContext.class,0);
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
		enterRule(_localctx, 30, RULE_construction);
		try {
			int _alt;
			setState(291);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,28,_ctx) ) {
			case 1:
				_localctx = new ArrayConstructionContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(264);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case Identifier:
					{
					setState(262);
					match(Identifier);
					}
					break;
				case Bool:
				case Int:
				case String:
					{
					setState(263);
					buildInVariableType();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(270); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(266);
						match(LeftSquareBracket);
						setState(267);
						expression(0);
						setState(268);
						match(RightSquareBracket);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(272); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,24,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				setState(278);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(274);
						match(LeftSquareBracket);
						setState(275);
						match(RightSquareBracket);
						}
						} 
					}
					setState(280);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
				}
				}
				break;
			case 2:
				_localctx = new VarConstructionContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(283);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case Identifier:
					{
					setState(281);
					match(Identifier);
					}
					break;
				case Bool:
				case Int:
				case String:
					{
					setState(282);
					buildInVariableType();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(285);
				match(LeftRoundBracket);
				setState(286);
				match(RightRoundBracket);
				}
				break;
			case 3:
				_localctx = new VarSimpleConstructionContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(289);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case Identifier:
					{
					setState(287);
					match(Identifier);
					}
					break;
				case Bool:
				case Int:
				case String:
					{
					setState(288);
					buildInVariableType();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
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
		enterRule(_localctx, 32, RULE_returnType);
		try {
			setState(297);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,29,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(293);
				match(Void);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(294);
				buildInVariableType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(295);
				match(Identifier);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(296);
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
		public ClassIdentifierContext classIdentifier() {
			return getRuleContext(ClassIdentifierContext.class,0);
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
		enterRule(_localctx, 34, RULE_variableType);
		try {
			setState(303);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,30,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(299);
				buildInVariableType();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(300);
				match(Identifier);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(301);
				arrayIdentifier();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(302);
				classIdentifier();
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
		enterRule(_localctx, 36, RULE_buildInVariableType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(305);
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
		public ClassIdentifierContext classIdentifier() {
			return getRuleContext(ClassIdentifierContext.class,0);
		}
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
		enterRule(_localctx, 38, RULE_arrayIdentifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(310);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Bool:
			case Int:
			case String:
				{
				setState(307);
				buildInVariableType();
				}
				break;
			case Identifier:
				{
				setState(308);
				match(Identifier);
				}
				break;
			case Class:
				{
				setState(309);
				classIdentifier();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(314); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(312);
				match(LeftSquareBracket);
				setState(313);
				match(RightSquareBracket);
				}
				}
				setState(316); 
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
	public static class ClassIdentifierContext extends ParserRuleContext {
		public TerminalNode Class() { return getToken(MxParser.Class, 0); }
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public TerminalNode LeftCurlyBrace() { return getToken(MxParser.LeftCurlyBrace, 0); }
		public TerminalNode RightCurlyBrace() { return getToken(MxParser.RightCurlyBrace, 0); }
		public List<DeclarationContext> declaration() {
			return getRuleContexts(DeclarationContext.class);
		}
		public DeclarationContext declaration(int i) {
			return getRuleContext(DeclarationContext.class,i);
		}
		public ConstructorContext constructor() {
			return getRuleContext(ConstructorContext.class,0);
		}
		public ClassIdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classIdentifier; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitClassIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassIdentifierContext classIdentifier() throws RecognitionException {
		ClassIdentifierContext _localctx = new ClassIdentifierContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_classIdentifier);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(318);
			match(Class);
			setState(319);
			match(Identifier);
			setState(320);
			match(LeftCurlyBrace);
			setState(324);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,33,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(321);
					declaration();
					}
					} 
				}
				setState(326);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,33,_ctx);
			}
			setState(328);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,34,_ctx) ) {
			case 1:
				{
				setState(327);
				constructor();
				}
				break;
			}
			setState(333);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 288231991059415040L) != 0)) {
				{
				{
				setState(330);
				declaration();
				}
				}
				setState(335);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(336);
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
		enterRule(_localctx, 42, RULE_literal);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(338);
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
		case 14:
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
		"\u0004\u0001:\u0155\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"+
		"\u0001\u0000\u0005\u0000.\b\u0000\n\u0000\f\u00001\t\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0001\u0001\u0001\u0003\u00017\b\u0001\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0001\u0003\u0005\u0003B\b\u0003\n\u0003\f\u0003E\t\u0003"+
		"\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0005\u0004P\b\u0004\n\u0004\f\u0004"+
		"S\t\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0003\u0005[\b\u0005\u0001\u0006\u0001\u0006\u0005\u0006"+
		"_\b\u0006\n\u0006\f\u0006b\t\u0006\u0001\u0006\u0001\u0006\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0005\u0007j\b\u0007\n\u0007\f\u0007"+
		"m\t\u0007\u0003\u0007o\b\u0007\u0001\u0007\u0001\u0007\u0001\b\u0001\b"+
		"\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0005\bz\b\b\n\b\f\b}\t\b\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0003"+
		"\t\u0088\b\t\u0001\t\u0001\t\u0003\t\u008c\b\t\u0001\t\u0001\t\u0003\t"+
		"\u0090\b\t\u0001\t\u0001\t\u0003\t\u0094\b\t\u0001\n\u0003\n\u0097\b\n"+
		"\u0001\n\u0001\n\u0001\n\u0005\n\u009c\b\n\n\n\f\n\u009f\t\n\u0001\u000b"+
		"\u0001\u000b\u0003\u000b\u00a3\b\u000b\u0001\u000b\u0001\u000b\u0001\u000b"+
		"\u0001\u000b\u0001\u000b\u0003\u000b\u00aa\b\u000b\u0001\f\u0001\f\u0001"+
		"\f\u0001\r\u0001\r\u0001\r\u0005\r\u00b2\b\r\n\r\f\r\u00b5\t\r\u0001\u000e"+
		"\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e"+
		"\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e"+
		"\u0001\u000e\u0003\u000e\u00c5\b\u000e\u0001\u000e\u0001\u000e\u0001\u000e"+
		"\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e"+
		"\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e"+
		"\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e"+
		"\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e"+
		"\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e"+
		"\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e"+
		"\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e"+
		"\u0003\u000e\u00f4\b\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e"+
		"\u0001\u000e\u0001\u000e\u0004\u000e\u00fc\b\u000e\u000b\u000e\f\u000e"+
		"\u00fd\u0001\u000e\u0001\u000e\u0005\u000e\u0102\b\u000e\n\u000e\f\u000e"+
		"\u0105\t\u000e\u0001\u000f\u0001\u000f\u0003\u000f\u0109\b\u000f\u0001"+
		"\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0004\u000f\u010f\b\u000f\u000b"+
		"\u000f\f\u000f\u0110\u0001\u000f\u0001\u000f\u0005\u000f\u0115\b\u000f"+
		"\n\u000f\f\u000f\u0118\t\u000f\u0001\u000f\u0001\u000f\u0003\u000f\u011c"+
		"\b\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0003\u000f\u0122"+
		"\b\u000f\u0003\u000f\u0124\b\u000f\u0001\u0010\u0001\u0010\u0001\u0010"+
		"\u0001\u0010\u0003\u0010\u012a\b\u0010\u0001\u0011\u0001\u0011\u0001\u0011"+
		"\u0001\u0011\u0003\u0011\u0130\b\u0011\u0001\u0012\u0001\u0012\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0003\u0013\u0137\b\u0013\u0001\u0013\u0001\u0013"+
		"\u0004\u0013\u013b\b\u0013\u000b\u0013\f\u0013\u013c\u0001\u0014\u0001"+
		"\u0014\u0001\u0014\u0001\u0014\u0005\u0014\u0143\b\u0014\n\u0014\f\u0014"+
		"\u0146\t\u0014\u0001\u0014\u0003\u0014\u0149\b\u0014\u0001\u0014\u0005"+
		"\u0014\u014c\b\u0014\n\u0014\f\u0014\u014f\t\u0014\u0001\u0014\u0001\u0014"+
		"\u0001\u0015\u0001\u0015\u0001\u0015\u0000\u0001\u001c\u0016\u0000\u0002"+
		"\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e"+
		" \"$&(*\u0000\t\u0001\u0000\u0016\u0017\u0003\u0000\u0002\u0002\u000e"+
		"\u000e\u0014\u0014\u0001\u0000\u0003\u0005\u0001\u0000\u0001\u0002\u0001"+
		"\u0000\u000f\u0010\u0001\u0000\u0006\t\u0001\u0000\n\u000b\u0001\u0000"+
		"$&\u0002\u0000)+45\u0180\u0000/\u0001\u0000\u0000\u0000\u00026\u0001\u0000"+
		"\u0000\u0000\u00048\u0001\u0000\u0000\u0000\u0006=\u0001\u0000\u0000\u0000"+
		"\bI\u0001\u0000\u0000\u0000\nZ\u0001\u0000\u0000\u0000\f\\\u0001\u0000"+
		"\u0000\u0000\u000ee\u0001\u0000\u0000\u0000\u0010r\u0001\u0000\u0000\u0000"+
		"\u0012\u0093\u0001\u0000\u0000\u0000\u0014\u0096\u0001\u0000\u0000\u0000"+
		"\u0016\u00a9\u0001\u0000\u0000\u0000\u0018\u00ab\u0001\u0000\u0000\u0000"+
		"\u001a\u00ae\u0001\u0000\u0000\u0000\u001c\u00c4\u0001\u0000\u0000\u0000"+
		"\u001e\u0123\u0001\u0000\u0000\u0000 \u0129\u0001\u0000\u0000\u0000\""+
		"\u012f\u0001\u0000\u0000\u0000$\u0131\u0001\u0000\u0000\u0000&\u0136\u0001"+
		"\u0000\u0000\u0000(\u013e\u0001\u0000\u0000\u0000*\u0152\u0001\u0000\u0000"+
		"\u0000,.\u0003\u0002\u0001\u0000-,\u0001\u0000\u0000\u0000.1\u0001\u0000"+
		"\u0000\u0000/-\u0001\u0000\u0000\u0000/0\u0001\u0000\u0000\u000002\u0001"+
		"\u0000\u0000\u00001/\u0001\u0000\u0000\u000023\u0005\u0000\u0000\u0001"+
		"3\u0001\u0001\u0000\u0000\u000047\u0003\u0006\u0003\u000057\u0003\u000e"+
		"\u0007\u000064\u0001\u0000\u0000\u000065\u0001\u0000\u0000\u00007\u0003"+
		"\u0001\u0000\u0000\u000089\u0005:\u0000\u00009:\u0005\u001b\u0000\u0000"+
		":;\u0005\u001c\u0000\u0000;<\u0003\f\u0006\u0000<\u0005\u0001\u0000\u0000"+
		"\u0000=>\u0003 \u0010\u0000>?\u0005:\u0000\u0000?C\u0005\u001b\u0000\u0000"+
		"@B\u0003\b\u0004\u0000A@\u0001\u0000\u0000\u0000BE\u0001\u0000\u0000\u0000"+
		"CA\u0001\u0000\u0000\u0000CD\u0001\u0000\u0000\u0000DF\u0001\u0000\u0000"+
		"\u0000EC\u0001\u0000\u0000\u0000FG\u0005\u001c\u0000\u0000GH\u0003\f\u0006"+
		"\u0000H\u0007\u0001\u0000\u0000\u0000IJ\u0003\"\u0011\u0000JQ\u0003\u001c"+
		"\u000e\u0000KL\u0005 \u0000\u0000LM\u0003\"\u0011\u0000MN\u0003\u001c"+
		"\u000e\u0000NP\u0001\u0000\u0000\u0000OK\u0001\u0000\u0000\u0000PS\u0001"+
		"\u0000\u0000\u0000QO\u0001\u0000\u0000\u0000QR\u0001\u0000\u0000\u0000"+
		"R\t\u0001\u0000\u0000\u0000SQ\u0001\u0000\u0000\u0000T[\u0003\f\u0006"+
		"\u0000U[\u0003\u0010\b\u0000V[\u0003\u0012\t\u0000W[\u0003\u0016\u000b"+
		"\u0000X[\u0003\u000e\u0007\u0000Y[\u0003\u0018\f\u0000ZT\u0001\u0000\u0000"+
		"\u0000ZU\u0001\u0000\u0000\u0000ZV\u0001\u0000\u0000\u0000ZW\u0001\u0000"+
		"\u0000\u0000ZX\u0001\u0000\u0000\u0000ZY\u0001\u0000\u0000\u0000[\u000b"+
		"\u0001\u0000\u0000\u0000\\`\u0005!\u0000\u0000]_\u0003\n\u0005\u0000^"+
		"]\u0001\u0000\u0000\u0000_b\u0001\u0000\u0000\u0000`^\u0001\u0000\u0000"+
		"\u0000`a\u0001\u0000\u0000\u0000ac\u0001\u0000\u0000\u0000b`\u0001\u0000"+
		"\u0000\u0000cd\u0005\"\u0000\u0000d\r\u0001\u0000\u0000\u0000en\u0003"+
		"\"\u0011\u0000fk\u0003\u001c\u000e\u0000gh\u0005 \u0000\u0000hj\u0003"+
		"\u001c\u000e\u0000ig\u0001\u0000\u0000\u0000jm\u0001\u0000\u0000\u0000"+
		"ki\u0001\u0000\u0000\u0000kl\u0001\u0000\u0000\u0000lo\u0001\u0000\u0000"+
		"\u0000mk\u0001\u0000\u0000\u0000nf\u0001\u0000\u0000\u0000no\u0001\u0000"+
		"\u0000\u0000op\u0001\u0000\u0000\u0000pq\u0005\u001f\u0000\u0000q\u000f"+
		"\u0001\u0000\u0000\u0000rs\u0005-\u0000\u0000st\u0005\u001b\u0000\u0000"+
		"tu\u0003\u001c\u000e\u0000uv\u0005\u001c\u0000\u0000v{\u0003\n\u0005\u0000"+
		"wx\u0005.\u0000\u0000xz\u0003\n\u0005\u0000yw\u0001\u0000\u0000\u0000"+
		"z}\u0001\u0000\u0000\u0000{y\u0001\u0000\u0000\u0000{|\u0001\u0000\u0000"+
		"\u0000|\u0011\u0001\u0000\u0000\u0000}{\u0001\u0000\u0000\u0000~\u007f"+
		"\u00050\u0000\u0000\u007f\u0080\u0005\u001b\u0000\u0000\u0080\u0081\u0003"+
		"\u001c\u000e\u0000\u0081\u0082\u0005\u001c\u0000\u0000\u0082\u0083\u0003"+
		"\n\u0005\u0000\u0083\u0094\u0001\u0000\u0000\u0000\u0084\u0085\u0005/"+
		"\u0000\u0000\u0085\u0087\u0005\u001b\u0000\u0000\u0086\u0088\u0003\u0014"+
		"\n\u0000\u0087\u0086\u0001\u0000\u0000\u0000\u0087\u0088\u0001\u0000\u0000"+
		"\u0000\u0088\u0089\u0001\u0000\u0000\u0000\u0089\u008b\u0005\u001f\u0000"+
		"\u0000\u008a\u008c\u0003\u001c\u000e\u0000\u008b\u008a\u0001\u0000\u0000"+
		"\u0000\u008b\u008c\u0001\u0000\u0000\u0000\u008c\u008d\u0001\u0000\u0000"+
		"\u0000\u008d\u008f\u0005\u001f\u0000\u0000\u008e\u0090\u0003\u001c\u000e"+
		"\u0000\u008f\u008e\u0001\u0000\u0000\u0000\u008f\u0090\u0001\u0000\u0000"+
		"\u0000\u0090\u0091\u0001\u0000\u0000\u0000\u0091\u0092\u0005\u001c\u0000"+
		"\u0000\u0092\u0094\u0003\n\u0005\u0000\u0093~\u0001\u0000\u0000\u0000"+
		"\u0093\u0084\u0001\u0000\u0000\u0000\u0094\u0013\u0001\u0000\u0000\u0000"+
		"\u0095\u0097\u0003\"\u0011\u0000\u0096\u0095\u0001\u0000\u0000\u0000\u0096"+
		"\u0097\u0001\u0000\u0000\u0000\u0097\u0098\u0001\u0000\u0000\u0000\u0098"+
		"\u009d\u0003\u001c\u000e\u0000\u0099\u009a\u0005 \u0000\u0000\u009a\u009c"+
		"\u0003\u001c\u000e\u0000\u009b\u0099\u0001\u0000\u0000\u0000\u009c\u009f"+
		"\u0001\u0000\u0000\u0000\u009d\u009b\u0001\u0000\u0000\u0000\u009d\u009e"+
		"\u0001\u0000\u0000\u0000\u009e\u0015\u0001\u0000\u0000\u0000\u009f\u009d"+
		"\u0001\u0000\u0000\u0000\u00a0\u00a2\u00053\u0000\u0000\u00a1\u00a3\u0003"+
		"\u001c\u000e\u0000\u00a2\u00a1\u0001\u0000\u0000\u0000\u00a2\u00a3\u0001"+
		"\u0000\u0000\u0000\u00a3\u00a4\u0001\u0000\u0000\u0000\u00a4\u00aa\u0005"+
		"\u001f\u0000\u0000\u00a5\u00a6\u00051\u0000\u0000\u00a6\u00aa\u0005\u001f"+
		"\u0000\u0000\u00a7\u00a8\u00052\u0000\u0000\u00a8\u00aa\u0005\u001f\u0000"+
		"\u0000\u00a9\u00a0\u0001\u0000\u0000\u0000\u00a9\u00a5\u0001\u0000\u0000"+
		"\u0000\u00a9\u00a7\u0001\u0000\u0000\u0000\u00aa\u0017\u0001\u0000\u0000"+
		"\u0000\u00ab\u00ac\u0003\u001c\u000e\u0000\u00ac\u00ad\u0005\u001f\u0000"+
		"\u0000\u00ad\u0019\u0001\u0000\u0000\u0000\u00ae\u00b3\u0003\u001c\u000e"+
		"\u0000\u00af\u00b0\u0005 \u0000\u0000\u00b0\u00b2\u0003\u001c\u000e\u0000"+
		"\u00b1\u00af\u0001\u0000\u0000\u0000\u00b2\u00b5\u0001\u0000\u0000\u0000"+
		"\u00b3\u00b1\u0001\u0000\u0000\u0000\u00b3\u00b4\u0001\u0000\u0000\u0000"+
		"\u00b4\u001b\u0001\u0000\u0000\u0000\u00b5\u00b3\u0001\u0000\u0000\u0000"+
		"\u00b6\u00b7\u0006\u000e\uffff\uffff\u0000\u00b7\u00b8\u0005\u001b\u0000"+
		"\u0000\u00b8\u00b9\u0003\u001c\u000e\u0000\u00b9\u00ba\u0005\u001c\u0000"+
		"\u0000\u00ba\u00c5\u0001\u0000\u0000\u0000\u00bb\u00c5\u0003*\u0015\u0000"+
		"\u00bc\u00c5\u0005:\u0000\u0000\u00bd\u00c5\u0005,\u0000\u0000\u00be\u00bf"+
		"\u0005\'\u0000\u0000\u00bf\u00c5\u0003\u001e\u000f\u0000\u00c0\u00c1\u0007"+
		"\u0000\u0000\u0000\u00c1\u00c5\u0003\u001c\u000e\u000e\u00c2\u00c3\u0007"+
		"\u0001\u0000\u0000\u00c3\u00c5\u0003\u001c\u000e\r\u00c4\u00b6\u0001\u0000"+
		"\u0000\u0000\u00c4\u00bb\u0001\u0000\u0000\u0000\u00c4\u00bc\u0001\u0000"+
		"\u0000\u0000\u00c4\u00bd\u0001\u0000\u0000\u0000\u00c4\u00be\u0001\u0000"+
		"\u0000\u0000\u00c4\u00c0\u0001\u0000\u0000\u0000\u00c4\u00c2\u0001\u0000"+
		"\u0000\u0000\u00c5\u0103\u0001\u0000\u0000\u0000\u00c6\u00c7\n\u0012\u0000"+
		"\u0000\u00c7\u00c8\u0005\u0018\u0000\u0000\u00c8\u0102\u0003\u001c\u000e"+
		"\u0013\u00c9\u00ca\n\f\u0000\u0000\u00ca\u00cb\u0007\u0002\u0000\u0000"+
		"\u00cb\u0102\u0003\u001c\u000e\r\u00cc\u00cd\n\u000b\u0000\u0000\u00cd"+
		"\u00ce\u0007\u0003\u0000\u0000\u00ce\u0102\u0003\u001c\u000e\f\u00cf\u00d0"+
		"\n\n\u0000\u0000\u00d0\u00d1\u0007\u0004\u0000\u0000\u00d1\u0102\u0003"+
		"\u001c\u000e\u000b\u00d2\u00d3\n\t\u0000\u0000\u00d3\u00d4\u0007\u0005"+
		"\u0000\u0000\u00d4\u0102\u0003\u001c\u000e\n\u00d5\u00d6\n\b\u0000\u0000"+
		"\u00d6\u00d7\u0007\u0006\u0000\u0000\u00d7\u0102\u0003\u001c\u000e\t\u00d8"+
		"\u00d9\n\u0007\u0000\u0000\u00d9\u00da\u0005\u0011\u0000\u0000\u00da\u0102"+
		"\u0003\u001c\u000e\b\u00db\u00dc\n\u0006\u0000\u0000\u00dc\u00dd\u0005"+
		"\u0013\u0000\u0000\u00dd\u0102\u0003\u001c\u000e\u0007\u00de\u00df\n\u0005"+
		"\u0000\u0000\u00df\u00e0\u0005\u0012\u0000\u0000\u00e0\u0102\u0003\u001c"+
		"\u000e\u0006\u00e1\u00e2\n\u0004\u0000\u0000\u00e2\u00e3\u0005\f\u0000"+
		"\u0000\u00e3\u0102\u0003\u001c\u000e\u0005\u00e4\u00e5\n\u0003\u0000\u0000"+
		"\u00e5\u00e6\u0005\r\u0000\u0000\u00e6\u0102\u0003\u001c\u000e\u0004\u00e7"+
		"\u00e8\n\u0002\u0000\u0000\u00e8\u00e9\u0005\u001d\u0000\u0000\u00e9\u00ea"+
		"\u0003\u001c\u000e\u0000\u00ea\u00eb\u0005\u001e\u0000\u0000\u00eb\u00ec"+
		"\u0003\u001c\u000e\u0002\u00ec\u0102\u0001\u0000\u0000\u0000\u00ed\u00ee"+
		"\n\u0001\u0000\u0000\u00ee\u00ef\u0005\u0015\u0000\u0000\u00ef\u0102\u0003"+
		"\u001c\u000e\u0001\u00f0\u00f1\n\u0013\u0000\u0000\u00f1\u00f3\u0005\u001b"+
		"\u0000\u0000\u00f2\u00f4\u0003\u001a\r\u0000\u00f3\u00f2\u0001\u0000\u0000"+
		"\u0000\u00f3\u00f4\u0001\u0000\u0000\u0000\u00f4\u00f5\u0001\u0000\u0000"+
		"\u0000\u00f5\u0102\u0005\u001c\u0000\u0000\u00f6\u00fb\n\u0011\u0000\u0000"+
		"\u00f7\u00f8\u0005\u0019\u0000\u0000\u00f8\u00f9\u0003\u001c\u000e\u0000"+
		"\u00f9\u00fa\u0005\u001a\u0000\u0000\u00fa\u00fc\u0001\u0000\u0000\u0000"+
		"\u00fb\u00f7\u0001\u0000\u0000\u0000\u00fc\u00fd\u0001\u0000\u0000\u0000"+
		"\u00fd\u00fb\u0001\u0000\u0000\u0000\u00fd\u00fe\u0001\u0000\u0000\u0000"+
		"\u00fe\u0102\u0001\u0000\u0000\u0000\u00ff\u0100\n\u0010\u0000\u0000\u0100"+
		"\u0102\u0007\u0000\u0000\u0000\u0101\u00c6\u0001\u0000\u0000\u0000\u0101"+
		"\u00c9\u0001\u0000\u0000\u0000\u0101\u00cc\u0001\u0000\u0000\u0000\u0101"+
		"\u00cf\u0001\u0000\u0000\u0000\u0101\u00d2\u0001\u0000\u0000\u0000\u0101"+
		"\u00d5\u0001\u0000\u0000\u0000\u0101\u00d8\u0001\u0000\u0000\u0000\u0101"+
		"\u00db\u0001\u0000\u0000\u0000\u0101\u00de\u0001\u0000\u0000\u0000\u0101"+
		"\u00e1\u0001\u0000\u0000\u0000\u0101\u00e4\u0001\u0000\u0000\u0000\u0101"+
		"\u00e7\u0001\u0000\u0000\u0000\u0101\u00ed\u0001\u0000\u0000\u0000\u0101"+
		"\u00f0\u0001\u0000\u0000\u0000\u0101\u00f6\u0001\u0000\u0000\u0000\u0101"+
		"\u00ff\u0001\u0000\u0000\u0000\u0102\u0105\u0001\u0000\u0000\u0000\u0103"+
		"\u0101\u0001\u0000\u0000\u0000\u0103\u0104\u0001\u0000\u0000\u0000\u0104"+
		"\u001d\u0001\u0000\u0000\u0000\u0105\u0103\u0001\u0000\u0000\u0000\u0106"+
		"\u0109\u0005:\u0000\u0000\u0107\u0109\u0003$\u0012\u0000\u0108\u0106\u0001"+
		"\u0000\u0000\u0000\u0108\u0107\u0001\u0000\u0000\u0000\u0109\u010e\u0001"+
		"\u0000\u0000\u0000\u010a\u010b\u0005\u0019\u0000\u0000\u010b\u010c\u0003"+
		"\u001c\u000e\u0000\u010c\u010d\u0005\u001a\u0000\u0000\u010d\u010f\u0001"+
		"\u0000\u0000\u0000\u010e\u010a\u0001\u0000\u0000\u0000\u010f\u0110\u0001"+
		"\u0000\u0000\u0000\u0110\u010e\u0001\u0000\u0000\u0000\u0110\u0111\u0001"+
		"\u0000\u0000\u0000\u0111\u0116\u0001\u0000\u0000\u0000\u0112\u0113\u0005"+
		"\u0019\u0000\u0000\u0113\u0115\u0005\u001a\u0000\u0000\u0114\u0112\u0001"+
		"\u0000\u0000\u0000\u0115\u0118\u0001\u0000\u0000\u0000\u0116\u0114\u0001"+
		"\u0000\u0000\u0000\u0116\u0117\u0001\u0000\u0000\u0000\u0117\u0124\u0001"+
		"\u0000\u0000\u0000\u0118\u0116\u0001\u0000\u0000\u0000\u0119\u011c\u0005"+
		":\u0000\u0000\u011a\u011c\u0003$\u0012\u0000\u011b\u0119\u0001\u0000\u0000"+
		"\u0000\u011b\u011a\u0001\u0000\u0000\u0000\u011c\u011d\u0001\u0000\u0000"+
		"\u0000\u011d\u011e\u0005\u001b\u0000\u0000\u011e\u0124\u0005\u001c\u0000"+
		"\u0000\u011f\u0122\u0005:\u0000\u0000\u0120\u0122\u0003$\u0012\u0000\u0121"+
		"\u011f\u0001\u0000\u0000\u0000\u0121\u0120\u0001\u0000\u0000\u0000\u0122"+
		"\u0124\u0001\u0000\u0000\u0000\u0123\u0108\u0001\u0000\u0000\u0000\u0123"+
		"\u011b\u0001\u0000\u0000\u0000\u0123\u0121\u0001\u0000\u0000\u0000\u0124"+
		"\u001f\u0001\u0000\u0000\u0000\u0125\u012a\u0005#\u0000\u0000\u0126\u012a"+
		"\u0003$\u0012\u0000\u0127\u012a\u0005:\u0000\u0000\u0128\u012a\u0003&"+
		"\u0013\u0000\u0129\u0125\u0001\u0000\u0000\u0000\u0129\u0126\u0001\u0000"+
		"\u0000\u0000\u0129\u0127\u0001\u0000\u0000\u0000\u0129\u0128\u0001\u0000"+
		"\u0000\u0000\u012a!\u0001\u0000\u0000\u0000\u012b\u0130\u0003$\u0012\u0000"+
		"\u012c\u0130\u0005:\u0000\u0000\u012d\u0130\u0003&\u0013\u0000\u012e\u0130"+
		"\u0003(\u0014\u0000\u012f\u012b\u0001\u0000\u0000\u0000\u012f\u012c\u0001"+
		"\u0000\u0000\u0000\u012f\u012d\u0001\u0000\u0000\u0000\u012f\u012e\u0001"+
		"\u0000\u0000\u0000\u0130#\u0001\u0000\u0000\u0000\u0131\u0132\u0007\u0007"+
		"\u0000\u0000\u0132%\u0001\u0000\u0000\u0000\u0133\u0137\u0003$\u0012\u0000"+
		"\u0134\u0137\u0005:\u0000\u0000\u0135\u0137\u0003(\u0014\u0000\u0136\u0133"+
		"\u0001\u0000\u0000\u0000\u0136\u0134\u0001\u0000\u0000\u0000\u0136\u0135"+
		"\u0001\u0000\u0000\u0000\u0137\u013a\u0001\u0000\u0000\u0000\u0138\u0139"+
		"\u0005\u0019\u0000\u0000\u0139\u013b\u0005\u001a\u0000\u0000\u013a\u0138"+
		"\u0001\u0000\u0000\u0000\u013b\u013c\u0001\u0000\u0000\u0000\u013c\u013a"+
		"\u0001\u0000\u0000\u0000\u013c\u013d\u0001\u0000\u0000\u0000\u013d\'\u0001"+
		"\u0000\u0000\u0000\u013e\u013f\u0005(\u0000\u0000\u013f\u0140\u0005:\u0000"+
		"\u0000\u0140\u0144\u0005!\u0000\u0000\u0141\u0143\u0003\u0002\u0001\u0000"+
		"\u0142\u0141\u0001\u0000\u0000\u0000\u0143\u0146\u0001\u0000\u0000\u0000"+
		"\u0144\u0142\u0001\u0000\u0000\u0000\u0144\u0145\u0001\u0000\u0000\u0000"+
		"\u0145\u0148\u0001\u0000\u0000\u0000\u0146\u0144\u0001\u0000\u0000\u0000"+
		"\u0147\u0149\u0003\u0004\u0002\u0000\u0148\u0147\u0001\u0000\u0000\u0000"+
		"\u0148\u0149\u0001\u0000\u0000\u0000\u0149\u014d\u0001\u0000\u0000\u0000"+
		"\u014a\u014c\u0003\u0002\u0001\u0000\u014b\u014a\u0001\u0000\u0000\u0000"+
		"\u014c\u014f\u0001\u0000\u0000\u0000\u014d\u014b\u0001\u0000\u0000\u0000"+
		"\u014d\u014e\u0001\u0000\u0000\u0000\u014e\u0150\u0001\u0000\u0000\u0000"+
		"\u014f\u014d\u0001\u0000\u0000\u0000\u0150\u0151\u0005\"\u0000\u0000\u0151"+
		")\u0001\u0000\u0000\u0000\u0152\u0153\u0007\b\u0000\u0000\u0153+\u0001"+
		"\u0000\u0000\u0000$/6CQZ`kn{\u0087\u008b\u008f\u0093\u0096\u009d\u00a2"+
		"\u00a9\u00b3\u00c4\u00f3\u00fd\u0101\u0103\u0108\u0110\u0116\u011b\u0121"+
		"\u0123\u0129\u012f\u0136\u013c\u0144\u0148\u014d";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}