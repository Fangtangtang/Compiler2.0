package tool;

import ast.type.Type;
import ir.BasicBlock;
import ir.IRRoot;
import ir.entity.SSAEntity;
import ir.function.Function;
import ir.stmt.instruction.*;
import ir.stmt.terminal.Branch;
import ir.stmt.terminal.Jump;
import ir.stmt.terminal.Return;
import utility.Pair;

import java.io.PrintStream;
import java.util.Map;

/**
 * @author F
 * IR转化得到的SSA
 */
public class SSAPrinter extends IRPrinter {
    public SSAPrinter(PrintStream output) {
        super(output);
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
        for (Map.Entry<String, Function> entry : root.builtinFuncDef.entrySet()) {
            Function func = entry.getValue();
            visit(func);
        }
    }

    @Override
    public void visit(BasicBlock basicBlock) {
        if (basicBlock.statements.size() == 0 && basicBlock.tailStmt == null) {
            return;
        }
        output.println(basicBlock.label + ":");
        Pair<SSAEntity, Pair<String[], SSAEntity[]>> pair;
        for (Map.Entry<String, Pair<SSAEntity, Pair<String[], SSAEntity[]>>> entry :
                basicBlock.phiMap.entrySet()) {
            pair = entry.getValue();
            Pair<String[], SSAEntity[]> phiDefs = pair.getSecond();
            StringBuilder str = new StringBuilder("\t" + pair.getFirst() + " = phi " + phiDefs.getSecond()[0].origin.type);
            for (int i = 0; i < phiDefs.getFirst().length; i++) {
                str.append("[").append(phiDefs.getFirst()[i]).append(", ").append(phiDefs.getSecond()[i]).append("] ");
            }
            output.println(str);
        }
        basicBlock.statements.forEach(stmt -> stmt.accept(this));
        basicBlock.tailStmt.accept(this);
    }


    @Override
    public void visit(Alloca stmt) {
        stmt.printSSA(output);
    }

    @Override
    public void visit(Binary stmt) {
        stmt.printSSA(output);
    }

    @Override
    public void visit(Call stmt) {
        stmt.printSSA(output);
    }

    @Override
    public void visit(GetElementPtr stmt) {
        stmt.printSSA(output);
    }

    @Override
    public void visit(Global stmt) {
        stmt.printSSA(output);
    }

    @Override
    public void visit(Icmp stmt) {
        stmt.printSSA(output);
    }

    @Override
    public void visit(Load stmt) {
        stmt.printSSA(output);
    }

    @Override
    public void visit(Store stmt) {
        stmt.printSSA(output);
    }

    @Override
    public void visit(Branch stmt) {
        stmt.printSSA(output);
    }

    @Override
    public void visit(Jump stmt) {
        stmt.printSSA(output);
    }

    @Override
    public void visit(Return stmt) {
        stmt.printSSA(output);
    }

    @Override
    public void visit(Trunc stmt) {
        stmt.printSSA(output);
    }

    @Override
    public void visit(Zext stmt) {
        stmt.printSSA(output);
    }

    @Override
    public void visit(Phi stmt) {
        stmt.printSSA(output);
    }

}
