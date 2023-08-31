package ir.stmt.terminal;

import ir.*;

import java.io.PrintStream;

/**
 * @author F
 * 作为block终止符的跳转语句
 */
public class Jump extends TerminalStmt {
    public String targetName;
    public BasicBlock target = null;
    public String phiLabel = null;
    public int index = 0;

    public Jump(String targetName) {
        super();
        this.targetName = targetName;
    }

    public Jump(BasicBlock target) {
        super();
        this.targetName = target.label;
        this.target = target;
    }

    public Jump(BasicBlock target, int index, String phiLabel) {
        super();
        this.targetName = target.label;
        this.target = target;
        this.index = index;
        this.phiLabel = phiLabel;
    }

    @Override
    public void print(PrintStream out) {
        out.println("\tbr label %" + targetName);
    }

    @Override
    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }
}
