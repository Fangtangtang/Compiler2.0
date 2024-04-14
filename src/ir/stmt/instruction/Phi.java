package ir.stmt.instruction;

import ir.IRVisitor;
import ir.entity.Entity;
import ir.entity.SSAEntity;
import ir.entity.Storage;
import ir.entity.constant.ConstBool;
import ir.entity.constant.ConstInt;
import ir.entity.constant.Constant;
import ir.entity.var.GlobalVar;
import ir.entity.var.LocalTmpVar;
import ir.entity.var.LocalVar;
import ir.entity.var.Ptr;
import ir.stmt.Stmt;
import utility.Pair;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

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
    public String inBlockLabel;
    public String phiLabel;
    public Storage ans1, ans2;
    public SSAEntity ssaAns1, ssaAns2;
    public ArrayList<String> label1 = new ArrayList<>(), label2 = new ArrayList<>();

    public Phi(LocalTmpVar result,
               Storage ans1,
               Storage ans2,
               String label1,
               String label2,
               String phiLabel) {
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
               String phiLabel) {
        this.result = result;
        this.ans1 = ans1;
        this.ans2 = ans2;
        this.label1 = label1;
        this.label2.add(label2);
        this.phiLabel = phiLabel;
    }

    public void remapLabelS2S(HashMap<String, String> blockMap) {
        ArrayList<String> newLabel1 = new ArrayList<>();
        for (String label : label1) {
            while (blockMap.containsKey(label)) {
                label = blockMap.get(label);
            }
            newLabel1.add(label);
        }
        label1 = newLabel1;
        ArrayList<String> newLabel2 = new ArrayList<>();
        for (String label : label2) {
            while (blockMap.containsKey(label)) {
                label = blockMap.get(label);
            }
            newLabel2.add(label);
        }
        label2 = newLabel2;
    }

    public void remapLabelS2M(HashMap<String, HashSet<String>> blockMap) {
        HashSet<String> labelList = new HashSet<>();
        ArrayList<String> newLabel1 = new ArrayList<>();
        for (String label : label1) {
            labelList.add(label);
            while (!labelList.isEmpty()) {
                String aLabel = labelList.iterator().next();
                if (blockMap.containsKey(aLabel)) {
                    labelList.addAll(blockMap.get(aLabel));
                } else {
                    newLabel1.add(aLabel);
                }
                labelList.remove(aLabel);
            }
        }
        label1 = newLabel1;
        ArrayList<String> newLabel2 = new ArrayList<>();
        for (String label : label2) {
            labelList.add(label);
            while (!labelList.isEmpty()) {
                String aLabel = labelList.iterator().next();
                if (blockMap.containsKey(aLabel)) {
                    labelList.addAll(blockMap.get(aLabel));
                } else {
                    newLabel2.add(aLabel);
                }
                labelList.remove(aLabel);
            }
        }
        label2 = newLabel2;
    }

    @Override
    public void print(PrintStream out) {
        String s1, s2;
        if (ans1 instanceof ConstInt constInt) {
            s1 = constInt.value;
        } else {
            s1 = ans1.toString();
        }
        if (ans2 instanceof ConstInt constInt) {
            s2 = constInt.value;
        } else {
            s2 = ans2.toString();
        }
        printFormat(out, s1, s2, result.toString());
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
    public boolean hasDef() {
        return true;
    }

    @Override
    public Entity getDef() {
        return result;
    }

    @Override
    public void promoteGlobalVar() {
        if (ans1 instanceof GlobalVar globalVar && globalVar.convertedLocalVar != null) {
            ans1 = globalVar.convertedLocalVar;
        }
        if (ans2 instanceof GlobalVar globalVar && globalVar.convertedLocalVar != null) {
            ans2 = globalVar.convertedLocalVar;
        }
    }

    @Override
    public void propagateLocalTmpVar() {
        if (ans1 instanceof Ptr ptr) {
            ans1 = ptr.valueInBasicBlock == null ? ans1 : ptr.valueInBasicBlock;
        } else if (ans1 instanceof LocalTmpVar tmpVar) {
            ans1 = tmpVar.valueInBasicBlock == null ? ans1 : tmpVar.valueInBasicBlock;
        }
        if (ans2 instanceof Ptr ptr) {
            ans2 = ptr.valueInBasicBlock == null ? ans2 : ptr.valueInBasicBlock;
        } else if (ans2 instanceof LocalTmpVar tmpVar) {
            ans2 = tmpVar.valueInBasicBlock == null ? ans2 : tmpVar.valueInBasicBlock;
        }
    }

    @Override
    public Pair<Stmt, LocalTmpVar> creatCopy(String suffix) {
        LocalTmpVar newResult = new LocalTmpVar(result.type, result.identity + suffix);
        Stmt stmt = new Phi(
                newResult, ans1, ans2,
                label1.get(0) + suffix, label2.get(0) + suffix,
                phiLabel
        );
        return new Pair<>(stmt, newResult);
    }

    @Override
    public void replaceUse(HashMap<LocalTmpVar, Constant> constantMap) {
        ans1 = (Storage) replace(ans1, constantMap);
        ans2 = (Storage) replace(ans2, constantMap);
    }

    @Override
    public void replaceUse(HashMap<LocalTmpVar, Storage> copyMap, HashMap<LocalVar, LocalVar> curAllocaMap) {
        ans1 = (Storage) replace(ans1, copyMap, curAllocaMap);
        ans2 = (Storage) replace(ans2, copyMap, curAllocaMap);
    }

    @Override
    public Constant getConstResult() {
        if (ans1 instanceof ConstInt const1 && ans2 instanceof ConstInt const2) {
            if (Objects.equals(const1.value, const2.value)) {
                return const1;
            } else {
                return null;
            }
        } else if (ans1 instanceof ConstBool const1 && ans2 instanceof ConstBool const2) {
            if (const1.value == const2.value) {
                return const1;
            } else {
                return null;
            }
        }
        return null;
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
