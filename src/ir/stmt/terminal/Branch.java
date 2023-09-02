package ir.stmt.terminal;

import ir.BasicBlock;
import ir.IRVisitor;
import ir.entity.Entity;

import java.io.PrintStream;
import java.util.ArrayList;

/**
 * @author F
 * 作为block终止符的分支语句
 */
public class Branch extends TerminalStmt {
    //判断条件
    public Entity condition;
    //两个分支
    public BasicBlock trueBranch, falseBranch;

    public String phiLabel = null;
    public int index = 0;

    public Branch(Entity condition,
                  BasicBlock trueBranch,
                  BasicBlock falseBranch) {
        super();
        this.condition = condition;
        this.trueBranch = trueBranch;
        this.falseBranch = falseBranch;
    }

    public Branch(Entity condition,
                  BasicBlock trueBranch,
                  BasicBlock falseBranch,
                  int index,
                  String phiLabel) {
        super();
        this.condition = condition;
        this.trueBranch = trueBranch;
        this.falseBranch = falseBranch;
        this.index = index;
        this.phiLabel = phiLabel;
    }

    //br i1 %cmp, label %return, label %if.end
    @Override
    public void print(PrintStream out) {
        String str = "\tbr " + condition.type.toString() + " " + condition.toString() +
                ", label %" + trueBranch.label +
                ", label %" + falseBranch.label;
        out.println(str);
    }

    @Override
    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }

    @Override
    public ArrayList<Entity> getUse() {
        ArrayList<Entity> ret = new ArrayList<>();
        ret.add(condition);
        return ret;
    }

    @Override
    public Entity getDef() {
        return null;
    }
}
