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
    public SSAEntity ssaResult;
    public Storage value;
    public SSAEntity ssaValue;

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
    public void printSSA(PrintStream out) {
        out.println(
                "\t" + ssaResult.toString() + " = zext "
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
    public Entity getDef() {
        return result;
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
