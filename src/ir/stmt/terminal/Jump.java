package ir.stmt.terminal;

import ir.*;

/**
 * @author F
 * 作为block终止符的跳转语句
 */
public class Jump extends TerminalStmt {
    public BasicBlock target;

    public Jump(BasicBlock target) {
        super();
        this.target = target;
    }

    @Override
    public void print() {
        System.out.println("br label %" + target.label);
    }

    @Override
    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }
}
