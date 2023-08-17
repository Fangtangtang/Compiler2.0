package tool;

import ir.*;
import ir.function.*;
import ir.stmt.instruction.*;
import ir.stmt.terminal.*;


import java.io.PrintStream;
import java.util.Map;

/**
 * @author F
 * 打印IR
 */
public class IRPrinter implements IRVisitor {

    PrintStream output;

    public IRPrinter(PrintStream output) {
        this.output = output;
    }

    @Override
    public void visit(IRRoot root) {
        root.printStruct(output);
        root.globalVarDefBlock.statements.forEach(stmt -> stmt.accept(this));
        visit(root.globalVarInitFunction);
        output.println("\n");
        for (Map.Entry<String, Function> entry : root.funcDef.entrySet()) {
            Function func = entry.getValue();
            visit(func);
        }
    }

    /**
     * ---------------------------------------------------------------------------
     * define i32 @func(i32 %0, i32 %1) #0 {
     * %3 = alloca i32, align 4
     * %4 = alloca i32, align 4
     * store i32 %0, ptr %3, align 4
     * store i32 %1, ptr %4, align 4
     * ret i32 1
     * }
     * ----------------------------------------------------------------------------
     *
     * @param function
     */
    @Override
    public void visit(Function function) {
        //借用的函数，要声明
        if (function.entry == null) {
            output.print("\ndeclare " + function.retType + " @" + function.funcName);
            output.println(function.printParameterList());
            return;
        }
        output.print("\ndefine " + function.retType + " @" + function.funcName);
        output.println(function.printParameterList());
        output.println("{");
        for (Map.Entry<String, BasicBlock> entry : function.blockMap.entrySet()) {
            BasicBlock block = entry.getValue();
            visit(block);
        }
        output.println("}");
    }

    @Override
    public void visit(BasicBlock basicBlock) {
        if (basicBlock.statements.size() == 0) {
            return;
        }
        output.println(basicBlock.label + ":");
        basicBlock.statements.forEach(stmt -> stmt.accept(this));
    }

    @Override
    public void visit(Alloca stmt) {
        stmt.print(output);
    }

    @Override
    public void visit(Binary stmt) {
        stmt.print(output);
    }

    @Override
    public void visit(Call stmt) {
        stmt.print(output);
    }

    @Override
    public void visit(GetElementPtr stmt) {
        stmt.print(output);
    }

    @Override
    public void visit(Global stmt) {
        stmt.print(output);
    }

    @Override
    public void visit(Icmp stmt) {
        stmt.print(output);
    }

    @Override
    public void visit(Load stmt) {
        stmt.print(output);
    }

    @Override
    public void visit(Store stmt) {
        stmt.print(output);
    }

    @Override
    public void visit(Branch stmt) {
        stmt.print(output);
    }

    @Override
    public void visit(Jump stmt) {
        stmt.print(output);
    }

    @Override
    public void visit(Return stmt) {
        stmt.print(output);
    }

    @Override
    public void visit(Trunc stmt) {
        stmt.print(output);
    }

    @Override
    public void visit(Zext stmt) {
        stmt.print(output);
    }

    @Override
    public void visit(Phi stmt) {
        stmt.print(output);
    }

    @Override
    public void visit(Malloc stmt) {
        stmt.print(output);
    }

    @Override
    public void visit(GlobalStr stmt) {
        stmt.print(output);
    }
}
