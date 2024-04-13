package ir.stmt.instruction;

import ir.IRVisitor;
import ir.entity.Entity;
import ir.entity.SSAEntity;
import ir.entity.Storage;
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

/**
 * @author F
 * 将整型数截断，取低位
 * <result> = trunc <ty> <value> to <ty2>
 * +----------------------------------------
 * |
 * |    %a = alloca i8, align 1         //bool被存为i8
 * |    %0 = load i8, ptr %a, align 1
 * |    %tobool = trunc i8 %0 to i1     //截断，取到实际的bool值
 * |
 * +--------------------------------------------
 */
public class Trunc extends Instruction {

    public LocalTmpVar result;
    public SSAEntity ssaResult;
    public Storage value;
    public SSAEntity ssaValue;

    public Trunc(LocalTmpVar result,
                 Storage value) {
        this.result = result;
        this.value = value;
    }

    @Override
    public void print(PrintStream out) {
        out.println(
                "\t" + result.toString() + " = trunc "
                        + value.type + " " + value.toString()
                        + " to " + result.type
        );
    }

    @Override
    public void printSSA(PrintStream out) {
        out.println(
                "\t" + ssaResult.toString() + " = trunc "
                        + value.type + " " + ssaValue.toString()
                        + " to " + result.type
        );
    }

    @Override
    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }

    @Override
    public ArrayList<Entity> getUse() {
        ArrayList<Entity> ret = new ArrayList<>();
        ret.add(value);
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
        if (value instanceof GlobalVar globalVar && globalVar.convertedLocalVar != null) {
            value = globalVar.convertedLocalVar;
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
        LocalTmpVar newResult = new LocalTmpVar(result.type, result.identity + suffix);
        Stmt stmt = new Trunc(newResult, value);
        return new Pair<>(stmt, newResult);
    }

    @Override
    public void replaceUse(HashMap<LocalTmpVar, Constant> constantMap) {
        value = (Storage) replace(value, constantMap);
    }

    @Override
    public void replaceUse(HashMap<LocalTmpVar, Storage> copyMap, HashMap<LocalVar, LocalVar> curAllocaMap) {
        value = (Storage) replace(value, copyMap, curAllocaMap);
    }

    @Override
    public Constant getConstResult() {
        if (value instanceof Constant constant) {
            return constant;
        }
        return null;
    }

    @Override
    public void setUse(ArrayList<SSAEntity> list) {
        ssaValue = list.get(0);
    }

    @Override
    public void setDef(SSAEntity entity) {
        ssaResult = entity;
    }

    @Override
    public ArrayList<SSAEntity> getSSAUse() {
        ArrayList<SSAEntity> ret = new ArrayList<>();
        ret.add(ssaValue);
        return ret;
    }

    @Override
    public SSAEntity getSSADef() {
        return ssaResult;
    }
}
