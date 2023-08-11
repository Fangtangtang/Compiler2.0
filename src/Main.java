import ast.*;
import frontend.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import parser.*;
import tool.*;
import utility.MxErrorListener;
import utility.SymbolTable;
import utility.error.MxException;
import utility.scope.Scope;

import java.io.*;

/**
 * @author F
 * COMPILER
 */
public class Main {
    //程序的入口点
    //可能会抛出任何类型的异常
    public static void main(String[] args) throws Exception {
//        String fileName ="C:/Users/21672/Desktop/mx_raw/sema/function-package/function-6.mx";

//        String fileName = "testcases/function/error.mx";
//        String fileName ="C:/Users/21672/Desktop/mx_raw/sema/symbol-package/symbol-2.mx";

        InputStream inputStream = System.in;

        try {
            compile(inputStream);
            //AST root
//            RootNode astRoot;
//
//            //char -> lexer
//            MxLexer lexer = new MxLexer(CharStreams.fromStream(inputStream));
//            lexer.removeErrorListeners();
//            lexer.addErrorListener(new MxErrorListener());//            //lexer -> token
//            CommonTokenStream tokens = new CommonTokenStream(lexer);
//            //token -> parser
//            MxParser parser = new MxParser(tokens);
//            parser.removeErrorListeners();
//            parser.addErrorListener(new MxErrorListener());
//            //parse tree
//            ParseTree parseTreeRoot = parser.program();
//
//            ASTBuilder astBuilder = new ASTBuilder();
//            astRoot = (RootNode) astBuilder.visit(parseTreeRoot);
//
////            ASTPrinter printer = new ASTPrinter();
////            printer.visit(astRoot);
//
//            Scope.symbolTable = new SymbolTable();
//            SymbolCollector symbolCollector = new SymbolCollector(Scope.symbolTable);
//            symbolCollector.visit(astRoot);
//
////            Scope.symbolTable.print();
//            SemanticChecker semanticChecker=new SemanticChecker();
//            semanticChecker.visit(astRoot);

        } catch (MxException exception) {
            System.err.println(exception.toString());
            throw new RuntimeException();
        }
    }

    public static void compile(InputStream inputStream) throws Exception {
        //AST root
        RootNode astRoot;

        //char -> lexer
        MxLexer lexer = new MxLexer(CharStreams.fromStream(inputStream));
        lexer.removeErrorListeners();
        lexer.addErrorListener(new MxErrorListener());
        //lexer -> token
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        //token -> parser
        MxParser parser = new MxParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(new MxErrorListener());
        //parse tree
        ParseTree parseTreeRoot = parser.program();

        ASTBuilder astBuilder = new ASTBuilder();
        astRoot = (RootNode) astBuilder.visit(parseTreeRoot);

//            ASTPrinter printer = new ASTPrinter();
//            printer.visit(astRoot);

        Scope.symbolTable = new SymbolTable();
        SymbolCollector symbolCollector = new SymbolCollector(Scope.symbolTable);
        symbolCollector.visit(astRoot);

//            Scope.symbolTable.print();
        SemanticChecker semanticChecker = new SemanticChecker();
        semanticChecker.visit(astRoot);

    }
}

