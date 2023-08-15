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
        visit(root.globalVarDefBlock);
        visit(root.globalVarInitFunction);
        output.println("\n");
        for (Map.Entry<String, Function> entry : root.funcDef.entrySet()) {
            Function func = entry.getValue();
            visit(func);
            output.println("\n");
        }
    }

    @Override
    public void visit(Function function) {
        output.println(function.funcName + " " + function.retType);
        function.printParameterList(output);
        for (Map.Entry<String, BasicBlock> entry : function.blockMap.entrySet()) {
            BasicBlock block = entry.getValue();
            visit(block);
        }
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
}
