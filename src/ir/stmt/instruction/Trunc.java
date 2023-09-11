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
}
