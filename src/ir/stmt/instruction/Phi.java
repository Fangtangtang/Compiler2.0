package ir.stmt.instruction;

import ir.IRVisitor;
import ir.entity.Entity;
import ir.entity.SSAEntity;
import ir.entity.Storage;
import ir.entity.var.LocalTmpVar;

import java.io.PrintStream;
import java.util.ArrayList;

/**
 * @author F
 * 通过跳转来源给变量赋值
 * 暂存前驱基本块，用于后继判断来源
 * <result> = phi <ty> [ <val0>, <label0>], ...
 * +------------------------------------------------
 * |
 * |    a==1?a:1;
 * |    %cond = phi i32 [ %1, %cond.true ], [ 1, %cond.false ]
 * |
 * +------------------------------------------------
 */
public class Phi extends Instruction {
    public LocalTmpVar result;
    public SSAEntity ssaResult;
    public int phiLabel;
    public Storage ans1, ans2;
    public SSAEntity ssaAns1, ssaAns2;
    public ArrayList<String> label1 = new ArrayList<>(), label2 = new ArrayList<>();

    public Phi(LocalTmpVar result,
               Storage ans1,
               Storage ans2,
               String label1,
               String label2,
               int phiLabel) {
        this.result = result;
        this.ans1 = ans1;
        this.ans2 = ans2;
        this.label1.add(label1);
        this.label2.add(label2);
        this.phiLabel = phiLabel;
    }

    public Phi(LocalTmpVar result,
               Storage ans1,
               Storage ans2,
               ArrayList<String> label1,
               String label2,
               int phiLabel) {
        this.result = result;
        this.ans1 = ans1;
        this.ans2 = ans2;
        this.label1 = label1;
        this.label2.add(label2);
        this.phiLabel = phiLabel;
    }

    @Override
    public void print(PrintStream out) {
        printFormat(out, ans1.toString(), ans2.toString(), result.toString());
    }

    @Override
    public void printSSA(PrintStream out) {
        printFormat(out, ssaAns1.toString(), ssaAns2.toString(), ssaResult.toString());
    }

    private void printFormat(PrintStream out, String string, String string2, String string3) {
        StringBuilder l1 = new StringBuilder(" [ " + string + ", %" + label1.get(0) + " ]");
        StringBuilder l2 = new StringBuilder(" [ " + string2 + ", %" + label2.get(0) + " ]");
        for (int i = 1; i < label1.size(); ++i) {
            l1.append(", [ ").append(string).append(", %").append(label1.get(i)).append(" ]");
        }
        for (int i = 1; i < label2.size(); ++i) {
            l2.append(", [ ").append(string2).append(", %").append(label2.get(i)).append(" ]");
        }
        out.println(
                "\t" + string3 + " = phi " + ans1.type + l1 + "," + l2
        );
    }

    @Override
    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }

    @Override
    public ArrayList<Entity> getUse() {
        ArrayList<Entity> ret = new ArrayList<>();
        ret.add(ans1);
        ret.add(ans2);
        return ret;
    }

    @Override
    public Entity getDef() {
        return result;
    }

    @Override
    public void setUse(ArrayList<SSAEntity> list) {
        ssaAns1 = list.get(0);
        ssaAns2 = list.get(1);
    }

    @Override
    public void setDef(SSAEntity entity) {
        ssaResult = entity;
    }

    @Override
    public ArrayList<SSAEntity> getSSAUse() {
        ArrayList<SSAEntity> ret = new ArrayList<>();
        ret.add(ssaAns1);
        ret.add(ssaAns2);
        return ret;
    }

    @Override
    public SSAEntity getSSADef() {
        return ssaResult;
    }
}
