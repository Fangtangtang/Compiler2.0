package ir.stmt.terminal;

import ir.*;

import java.io.PrintStream;

/**
 * @author F
 * 作为block终止符的跳转语句
 */
public class Jump extends TerminalStmt {
    public String targetName;

    public Jump(String targetName) {
        super();
        this.targetName = targetName;
    }

    @Override
    public void print(PrintStream out) {
        out.println("br label %" + targetName);
    }

    @Override
    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }
}
