package ir.stmt.terminal;

import ir.BasicBlock;
import ir.IRVisitor;
import ir.entity.Entity;
import ir.entity.SSAEntity;
import ir.entity.Storage;
import ir.entity.var.GlobalVar;
import ir.entity.var.LocalTmpVar;
import ir.entity.var.Ptr;

import java.io.PrintStream;
import java.util.ArrayList;

/**
 * @author F
 * 作为block终止符的分支语句
 */
public class Branch extends TerminalStmt {
    //判断条件
    public Entity condition;
    public SSAEntity ssaCondition;
    //两个分支
    public BasicBlock trueBranch, falseBranch;

    public String phiLabel = null;
    public String index;
    public Storage result;
    public SSAEntity ssaResult;

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
                  String index,
                  String phiLabel,
                  Storage result) {
        super();
        this.condition = condition;
        this.trueBranch = trueBranch;
        this.falseBranch = falseBranch;
        this.index = index;
        this.phiLabel = phiLabel;
        this.result = result;
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
    public void printSSA(PrintStream out) {
        String str = "\tbr " + condition.type.toString() + " " + ssaCondition.toString() +
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

    @Override
    public void promoteGlobalVar() {
        if (condition instanceof GlobalVar globalVar && globalVar.convertedLocalVar != null){
            condition = globalVar.convertedLocalVar;
        }
    }

    @Override
    public void propagateLocalTmpVar() {
        if (condition instanceof Ptr ptr) {
            condition = ptr.valueInBasicBlock == null ? condition : ptr.valueInBasicBlock;
        } else if (condition instanceof LocalTmpVar tmpVar){
            condition = tmpVar.valueInBasicBlock == null ? condition : tmpVar.valueInBasicBlock;
        }
    }

    @Override
    public void setUse(ArrayList<SSAEntity> list) {
        ssaCondition = list.get(0);
    }

    @Override
    public void setDef(SSAEntity entity) {
        return;
    }

    @Override
    public ArrayList<SSAEntity> getSSAUse() {
        ArrayList<SSAEntity> ret = new ArrayList<>();
        ret.add(ssaCondition);
        return ret;
    }

    @Override
    public SSAEntity getSSADef() {
        return null;
    }
}
