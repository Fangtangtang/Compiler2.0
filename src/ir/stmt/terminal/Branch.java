package ir.stmt.terminal;

import ir.BasicBlock;
import ir.IRVisitor;
import ir.entity.Entity;

/**
 * @author F
 * 作为block终止符的分支语句
 */
public class Branch extends TerminalStmt {
    //判断条件
    public Entity condition;
    //两个分支
    public BasicBlock trueBranch, falseBranch;

    public Branch(Entity condition,
                  BasicBlock trueBranch,
                  BasicBlock falseBranch) {
        super();
        this.condition = condition;
        this.trueBranch = trueBranch;
        this.falseBranch = falseBranch;
    }

    //br i1 %cmp, label %return, label %if.end
    @Override
    public void print() {
        String str = "br " + condition.toString() +
                ", label " + trueBranch.label +
                ", label " + falseBranch.label;
        System.out.println(str);
    }

    @Override
    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }
}
