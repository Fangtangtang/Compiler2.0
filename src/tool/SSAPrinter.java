package tool;

import ast.type.Type;
import ir.BasicBlock;
import ir.entity.Entity;
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
    public void visit(BasicBlock basicBlock) {
        if (basicBlock.statements.size() == 0 && basicBlock.tailStmt == null) {
            return;
        }
        output.println(basicBlock.label + ":");
        Pair<String, Pair<String[], Entity[]>> pair;
        for (Map.Entry<String, Pair<String, Pair<String[], Entity[]>>> entry :
                basicBlock.phiMap.entrySet()) {
            pair = entry.getValue();
            Pair<String[], Entity[]> phiDefs = pair.getSecond();
            StringBuilder str = new StringBuilder("%" + pair.getFirst() + " = phi " + phiDefs.getSecond()[0].type);
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
