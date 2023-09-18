package ir.stmt.instruction;

import ir.IRVisitor;
import ir.entity.Entity;
import ir.entity.SSAEntity;
import ir.entity.var.*;

import java.io.PrintStream;
import java.util.ArrayList;


/**
 * @author F
 * 将指针指向的值赋值给result
 * <result> = load <ty>, ptr <pointer>
 * + -----------------------------------
 * |
 * |    int a = b;
 * |    %0 = load i32, ptr %b
 * |    store i32 %0, ptr %a
 * |
 * |    char a;
 * |    char b = a;
 * |    %a = alloca i8, align 1
 * |    %b = alloca i8, align 1
 * |    %0 = load i8, ptr %a, align 1 //局部变量，char型
 * |    store i8 %0, ptr %b, align 1
 * |
 * + ------------------------------------
 */
public class Load extends Instruction {
    public Entity result;
    public SSAEntity ssaResult;
    public Entity pointer;
    public SSAEntity ssaPtr;
    public boolean loadRet = false;

    public Load(Entity result,
                Entity pointer) {
        this.result = result;
        this.pointer = pointer;
    }

    public Load(Entity result,
                Entity pointer,
                boolean loadRet) {
        this.result = result;
        this.pointer = pointer;
    }

    @Override
    public void print(PrintStream out) {
        out.println("\t" + result.toString() + " = load "
                + result.type.toString() + ", ptr " + pointer.toString());
    }

    @Override
    public void printSSA(PrintStream out) {
        out.println("\t" + ssaResult.toString() + " = load "
                + result.type.toString() + ", ptr " + ssaPtr.toString());
    }

    @Override
    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }

    @Override
    public ArrayList<Entity> getUse() {
        ArrayList<Entity> ret = new ArrayList<>();
        ret.add(pointer);
        return ret;
    }

    @Override
    public Entity getDef() {
        return result;
    }

    @Override
    public void setUse(ArrayList<SSAEntity> list) {
        ssaPtr = list.get(0);
    }

    @Override
    public void setDef(SSAEntity entity) {
        ssaResult = entity;
    }

    @Override
    public ArrayList<SSAEntity> getSSAUse() {
        ArrayList<SSAEntity> ret = new ArrayList<>();
        ret.add(ssaPtr);
        return ret;
    }

    @Override
    public SSAEntity getSSADef() {
        return ssaResult;
    }
}
