import ast.*;
import frontend.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import parser.*;
import tool.*;
import utility.error.MxException;

import java.io.*;

/**
 * @author F
 * COMPILER
 * TODO:expr能否被赋值
 */
public class Main {
    //程序的入口点
    //可能会抛出任何类型的异常
    public static void main(String[] args) throws Exception {
        String fileName = "testcases/variable/array.mx";
        InputStream inputStream = new FileInputStream(fileName);

        try {
            //AST root
            RootNode ASTRoot;

            //char -> lexer
            MxLexer lexer = new MxLexer(CharStreams.fromStream(inputStream));
            //lexer -> token
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            //token -> parser
            MxParser parser = new MxParser(tokens);

            ParseTree parseTreeRoot = parser.program();

            ASTBuilder astBuilder = new ASTBuilder();
            ASTRoot = (RootNode) astBuilder.visit(parseTreeRoot);

            ASTPrinter printer = new ASTPrinter();
            printer.visit(ASTRoot);

        } catch (MxException exception) {
            System.err.println(exception.toString());
            throw new RuntimeException();
        }
    }
}
