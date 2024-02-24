import asm.Program;
import ast.other.RootNode;
import backend.ASMOptimizer;
import backend.InstructionSelectorOnEntity;
import frontend.*;
import midend.IROptimizer;
import midend.IRBuilder;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import parser.*;
import utility.error.MxErrorListener;
import utility.SymbolTable;
import utility.error.MxException;
import utility.scope.Scope;
import tool.*;

import java.io.*;

/**
 * @author F
 * COMPILER
 */
public class Main {
    //程序的入口点
    //可能会抛出任何类型的异常
    public static void main(String[] args) throws Exception {
        InputStream inputStream = System.in;

        try {
            compile(inputStream);
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

        Scope.symbolTable = new SymbolTable();
        SymbolCollector symbolCollector = new SymbolCollector(Scope.symbolTable);
        symbolCollector.visit(astRoot);

        SemanticChecker semanticChecker = new SemanticChecker();
        semanticChecker.visit(astRoot);

        IRBuilder irBuilder = new IRBuilder(Scope.symbolTable);
        irBuilder.visit(astRoot);

        PrintStream outputStream = System.out;

        //IR
//        IRPrinter printer = new IRPrinter(outputStream);
//        printer.visit(irBuilder.irRoot);

        //OPT
//        IROptimizer optimizer = new IROptimizer(irBuilder.irRoot);
//        optimizer.execute();
//        Program program = new Program();
//        InstructionSelector selector = new InstructionSelector(program);
//        selector.visit(irBuilder.irRoot);
//
//        ASMOptimizer asmOptimizer = new ASMOptimizer(program.text, selector.registerMap);
//        asmOptimizer.execute();
//
//        program.printRegColoring(outputStream);

        //without optimize
//        Program program = new Program();
//        InstructionSelectorOnEntity instSelector = new InstructionSelectorOnEntity(program);
//        instSelector.visit(irBuilder.irRoot);
//        RegisterAllocator allocator = new RegisterAllocator(instSelector.registerMap);
//        allocator.visit(program);
//        program.print(outputStream);

        Program program = new Program();
        // ir -> asm:含“mem2reg”
        InstructionSelectorOnEntity selector = new InstructionSelectorOnEntity(program);
        selector.visit(irBuilder.irRoot);
        // asm上优化
        ASMOptimizer asmOptimizer = new ASMOptimizer(program.text, selector.registerMap);
        asmOptimizer.execute();

        program.printRegColoring(outputStream);
    }
}

