package ir.stmt.instruction;


import ir.IRVisitor;
import ir.entity.Entity;
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
 * trunc逆操作
 * zero extends its operand to type ty2
 * <result> = zext <ty> <value> to <ty2>
 * +-----------------------------------
 * |
 * |    %frombool = zext i1 %lnot to i8
 * |    store i8 %frombool, ptr %a, align 1
 * |
 * +----------------------------------------
 */
public class Zext extends Instruction {

    public LocalTmpVar result;
    public Storage value;

    public Zext(LocalTmpVar result,
                Storage value) {
        this.result = result;
        this.value = value;
    }

    @Override
    public void print(PrintStream out) {
        out.println(
                "\t" + result.toString() + " = zext "
                        + value.type + " " + value.toString()
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
        Stmt stmt = new Zext(newResult, value);
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
}
