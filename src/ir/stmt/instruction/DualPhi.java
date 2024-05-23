package ir.stmt.instruction;

import ir.IRVisitor;
import ir.entity.Entity;
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
import java.util.Objects;

/**
 * @author F
 * 仅两个label的phi指令
 * 通过跳转来源给变量赋值
 * <result> = phi <ty> [ <val0>, <label0>], ...
 * +------------------------------------------------
 * |
 * |    a==1?a:1;
 * |    %cond = phi i32 [ %1, %cond.true ], [ 1, %cond.false ]
 * |
 * +------------------------------------------------
 */
public class DualPhi extends Instruction {
    public LocalTmpVar result;
    public String phiLabel;
    public Storage ans1, ans2;
    public String label1, label2;

    public DualPhi(LocalTmpVar result,
                   Storage ans1,
                   Storage ans2,
                   String label1,
                   String label2,
                   String phiLabel) {
        this.result = result;
        this.ans1 = ans1;
        this.ans2 = ans2;
        this.label1 = label1;
        this.label2 = label2;
        this.phiLabel = phiLabel;
    }

    public void remapLabel(HashMap<String, String> blockMap) {
        String label = label1;
        while (blockMap.containsKey(label)) {
            label = blockMap.get(label);
        }
        label1 = label;
        label = label2;
        while (blockMap.containsKey(label)) {
            label = blockMap.get(label);
        }
        label2 = label;
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
        out.println(
                "\t" + result.toString() + " = phi " + ans1.type
                        + " [ " + s1 + ", %" + label1 + " ]" + ","
                        + " [ " + s2 + ", %" + label2 + " ]"
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
        Stmt stmt = new DualPhi(
                newResult, ans1, ans2,
                label1 + suffix, label2 + suffix,
                phiLabel
        );
        return new Pair<>(stmt, newResult);
    }

    @Override
    public void replaceUse(HashMap<String, Storage> constantMap) {
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
}
