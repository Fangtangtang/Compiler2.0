package ir.stmt.instruction;

import ir.IRVisitor;
import ir.entity.*;
import ir.entity.constant.*;
import ir.entity.var.*;
import ir.irType.IRType;
import ir.irType.IntType;
import ir.stmt.Stmt;
import utility.Pair;

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
    public HashSet<String> validList = new HashSet<>();

    public DomPhi(LocalTmpVar result) {
        this.result = result;
    }

    public DomPhi(LocalTmpVar result, HashMap<String, Storage> phiList) {
        this.result = result;
        this.phiList = phiList;
    }

    public DomPhi(LocalTmpVar result,
                  Storage ans1,
                  Storage ans2,
                  String label1,
                  String label2) {
        this.result = result;
        this.phiList.put(label1, ans1);
        this.phiList.put(label2, ans2);
        validList.add(label1);
        validList.add(label2);
    }

    public void put(String label, Storage value) {
        if (value instanceof Null) {
            if (phiList.containsKey(label)) {
                return;
            } else {
                if (result.type instanceof IntType intType) {
                    if (intType.typeName.equals(IntType.TypeName.BOOL) ||
                            intType.typeName.equals(IntType.TypeName.TMP_BOOL)) {
                        value = new ConstBool(false);
                    } else if (intType.typeName.equals(IntType.TypeName.INT)) {
                        value = new ConstInt("0");
                    }
                }
            }
        } else {
            validList.add(label);
        }
        phiList.put(label, value);
    }

    public void remapLabel(HashMap<String, String> blockMap) {
        HashMap<String, Storage> newMap = new HashMap<>();
        for (Map.Entry<String, Storage> entry : phiList.entrySet()) {
            String label = entry.getKey();
            while (blockMap.containsKey(label)) {
                label = blockMap.get(label);
            }
            newMap.put(label, entry.getValue());
        }
        phiList = newMap;
    }

    @Override
    public void print(PrintStream out) {
        StringBuilder str = new StringBuilder("\t" + result.toString() + " = phi ");
        boolean isFirst = true;
        IRType type = result.type;
        StringBuilder varStr = new StringBuilder();
        for (Map.Entry<String, Storage> entry : phiList.entrySet()) {
            Storage ans = entry.getValue();
            String s;
            if (ans instanceof Null) {
                if (type instanceof IntType) {
                    s = "0";
                } else {
                    s = "null";
                }
            } else if (ans instanceof ConstInt constInt) {
                s = constInt.value;
            } else {
                s = ans.toString();
            }
            if (!isFirst) {
                varStr.append(",");
            } else {
                isFirst = false;
            }
            varStr.append(" [ ").append(s).append(", %").append(entry.getKey()).append(" ]");
        }
        str.append(type.toString()).append(varStr);
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
        HashMap<String, Storage> newList = new HashMap<>();
        for (Map.Entry<String, Storage> entry : phiList.entrySet()) {
            Storage rep = entry.getValue();
            if (rep instanceof GlobalVar globalVar && globalVar.convertedLocalVar != null) {
                rep = globalVar.convertedLocalVar;
            }
            newList.put(entry.getKey(), rep);
        }
        phiList = newList;
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
    public void replaceUse(HashMap<String, Storage> constantMap) {
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
