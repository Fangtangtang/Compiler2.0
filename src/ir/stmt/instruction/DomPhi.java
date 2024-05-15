package ir.stmt.instruction;

import ir.IRVisitor;
import ir.entity.*;
import ir.entity.constant.*;
import ir.entity.var.*;
import ir.stmt.Stmt;
import utility.Pair;
import utility.error.InternalException;

import java.io.PrintStream;
import java.util.*;

/**
 * @author F
 * 在 dom frountier（除了我，还有别人会对这里赋值）插入的phi指令
 * 多个label，在转asm时消去
 */
public class DomPhi extends Instruction {
    public LocalTmpVar result;

    public HashMap<String, Storage> phiList = new HashMap<>();

    public DomPhi(LocalTmpVar result) {
        this.result = result;
    }

    public DomPhi(LocalTmpVar result, String label, Storage ans) {
        this.result = result;
        phiList.put(label, ans);
    }

    public DomPhi(LocalTmpVar result, HashMap<String, Storage> phiList) {
        this.result = result;
        this.phiList = phiList;
    }

    public void put(String label, Storage value) {
        phiList.put(label, value);
    }

    @Override
    public void print(PrintStream out) {
        StringBuilder str = new StringBuilder("\t" + result.toString() + " = phi ");
        boolean isFirst = true;
        for (Map.Entry<String, Storage> entry : phiList.entrySet()) {
            Storage ans = entry.getValue();
            String s;
            if (ans instanceof ConstInt constInt) {
                s = constInt.value;
            } else {
                s = ans.toString();
            }
            if (isFirst) {
                isFirst = false;
                str.append(ans.type);
                str.append(" [ ").append(s).append(", %").append(entry.getKey()).append(" ]");
            } else {
                str.append(", [ ").append(s).append(", %").append(entry.getKey()).append(" ]");
            }
        }
        out.println(str);
    }

    @Override
    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }

    @Override
    public ArrayList<Entity> getUse() {
        ArrayList<Entity> ret = new ArrayList<>();
        for (Map.Entry<String, Storage> entry : phiList.entrySet()) {
            ret.add(entry.getValue());
        }
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
        throw new InternalException("[DomPhi]: won't use global variable!");
    }

    @Override
    public void propagateLocalTmpVar() {
        HashMap<String, Storage> newList = new HashMap<>();
        for (Map.Entry<String, Storage> entry : phiList.entrySet()) {
            Storage rep = entry.getValue();
            if (rep instanceof Ptr ptr) {
                rep = ptr.valueInBasicBlock == null ? rep : ptr.valueInBasicBlock;
            } else if (rep instanceof LocalTmpVar tmpVar) {
                rep = tmpVar.valueInBasicBlock == null ? rep : tmpVar.valueInBasicBlock;
            }
            newList.put(entry.getKey(), rep);
        }
        phiList = newList;
    }

    @Override
    public Pair<Stmt, LocalTmpVar> creatCopy(String suffix) {
        LocalTmpVar newResult = new LocalTmpVar(result.type, result.identity + suffix);
        HashMap<String, Storage> newList = new HashMap<>();
        for (Map.Entry<String, Storage> entry : phiList.entrySet()) {
            newList.put(
                    entry.getKey() + suffix,
                    entry.getValue()
            );
        }
        Stmt stmt = new DomPhi(
                newResult, newList
        );
        return new Pair<>(stmt, newResult);
    }

    @Override
    public void replaceUse(HashMap<LocalTmpVar, Constant> constantMap) {
        HashMap<String, Storage> newList = new HashMap<>();
        for (Map.Entry<String, Storage> entry : phiList.entrySet()) {
            newList.put(
                    entry.getKey(),
                    (Storage) replace(entry.getValue(), constantMap)
            );
        }
        phiList = newList;
    }

    @Override
    public void replaceUse(HashMap<LocalTmpVar, Storage> copyMap, HashMap<LocalVar, LocalVar> curAllocaMap) {
        HashMap<String, Storage> newList = new HashMap<>();
        for (Map.Entry<String, Storage> entry : phiList.entrySet()) {
            newList.put(
                    entry.getKey(),
                    (Storage) replace(entry.getValue(), copyMap, curAllocaMap)
            );
        }
        phiList = newList;
    }

    @Override
    public Constant getConstResult() {
        Constant constant = null;
        for (Map.Entry<String, Storage> entry : phiList.entrySet()) {
            if (entry.getValue() instanceof Constant constResult) {
                if (constant == null) {
                    constant = constResult;
                } else {
                    if (constant instanceof ConstInt const1 && constResult instanceof ConstInt const2) {
                        if (!Objects.equals(const1.value, const2.value)) {
                            return null;
                        }
                    }
                    if (constant instanceof ConstBool const1 && constResult instanceof ConstBool const2) {
                        if (!Objects.equals(const1.value, const2.value)) {
                            return null;
                        }
                    }
                }
            } else {
                return null;
            }
        }
        return constant;
    }
}
