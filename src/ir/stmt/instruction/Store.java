package ir.stmt.instruction;

import ir.IRVisitor;
import ir.entity.Entity;
import ir.entity.SSAEntity;
import ir.entity.Storage;
import ir.entity.constant.*;
import ir.entity.var.*;
import ir.irType.PtrType;
import ir.stmt.Stmt;
import utility.Pair;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author F
 * 将entity存到指针指向位置
 * store <ty> <value>, ptr <pointer>
 * + -----------------------------------
 * |
 * |    int a = b;
 * |    %0 = load i32, ptr %b
 * |    store i32 %0, ptr %a
 * |
 * |    b = 1;
 * |    store i32 1, ptr %b
 * |
 * + ------------------------------------
 */
public class Store extends Instruction {
    public Entity value;
    public SSAEntity ssaValue;
    public Entity pointer;
    public SSAEntity ssaPtr;

    public Store(Entity value,
                 Entity pointer) {
        this.value = value;
        this.pointer = pointer;
    }

    @Override
    public void print(PrintStream out) {
        String str;
        if (value instanceof LocalTmpVar) {
            str = value.type.toString() + " " + value.toString();
        } else if (value instanceof Ptr ptr) {
            str = ptr.storage.type + " " + ptr;
        } else if (value instanceof ConstBool constBool) {
            str = "i8 " + constBool.toString();
        } else {
            str = value.toString();
        }
        out.println("\tstore " + str
                + ", ptr " + pointer.toString());
    }

    @Override
    public void printSSA(PrintStream out) {
        String str;
        if (value instanceof LocalTmpVar) {
            str = value.type.toString() + " " + ssaValue.toString();
        } else if (value instanceof Ptr ptr) {
            str = ptr.storage.type + " " + ptr;
        } else {
            str = ssaValue.toString();
        }
        out.println("\tstore " + str
                + ", ptr " + ssaPtr.toString());
    }

    @Override
    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }

    //todo:correct??
    @Override
    public ArrayList<Entity> getUse() {
        ArrayList<Entity> ret = new ArrayList<>();
        ret.add(value);
        ret.add(pointer);
        return ret;
    }

    @Override
    public boolean hasDef() {
        return pointer instanceof Ptr;
    }

    @Override
    public Entity getDef() {
        if (pointer instanceof Ptr) {
            return pointer;
        }
        return null;
    }

    @Override
    public void promoteGlobalVar() {
        if (value instanceof GlobalVar globalVar && globalVar.convertedLocalVar != null) {
            value = globalVar.convertedLocalVar;
        }
        if (pointer instanceof GlobalVar globalVar && globalVar.convertedLocalVar != null) {
            pointer = globalVar.convertedLocalVar;
        }
    }

    @Override
    public void propagateLocalTmpVar() {
        if (value instanceof Ptr ptr) {
            value = ptr.valueInBasicBlock == null ? value : ptr.valueInBasicBlock;
        } else if (value instanceof LocalTmpVar tmpVar) {
            value = tmpVar.valueInBasicBlock == null ? value : tmpVar.valueInBasicBlock;
        }
    }

    @Override
    public Pair<Stmt, LocalTmpVar> creatCopy(String suffix) {
        Stmt stmt = new Store(value, pointer);
        return new Pair<>(stmt, null);
    }

    @Override
    public void replaceUse(HashMap<LocalTmpVar, Constant> constantMap) {
        value = replace(value, constantMap);
        pointer = replace(pointer, constantMap);
    }

    @Override
    public void replaceUse(HashMap<LocalTmpVar, Storage> copyMap, HashMap<LocalVar, LocalVar> curAllocaMap) {
        value = replace(value, copyMap, curAllocaMap);
        pointer = replace(pointer, copyMap, curAllocaMap);
    }

    @Override
    public Constant getConstResult() {
        return null;
    }

    @Override
    public void setUse(ArrayList<SSAEntity> list) {
        ssaValue = list.get(0);
    }

    @Override
    public void setDef(SSAEntity entity) {
        ssaPtr = entity;
    }

    @Override
    public ArrayList<SSAEntity> getSSAUse() {
        ArrayList<SSAEntity> ret = new ArrayList<>();
        ret.add(ssaValue);
        return ret;
    }

    @Override
    public SSAEntity getSSADef() {
        return ssaPtr;
    }
}
