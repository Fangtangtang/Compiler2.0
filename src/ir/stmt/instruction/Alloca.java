package ir.stmt.instruction;

import ir.IRVisitor;
import ir.entity.*;
import ir.entity.constant.Constant;
import ir.entity.var.*;
import ir.irType.IRType;

import java.io.PrintStream;
import java.util.ArrayList;


/**
 * @author F
 * 局部变量分配内存空间（在栈上开空间）的指令
 * <result> = alloca <type>
 * + --------------------------------
 * |
 * | char a;    ->  %a = alloca i8
 * | int b;     ->  %b = alloca i32
 * |
 * +---------------------------------
 * result为指针类型(LocalVar)
 * type为指针指向对象的类型
 */
public class Alloca extends Instruction {
    public LocalVar result;
    public SSAEntity ssaResult;

    public Alloca(IRType irType,
                  String identifier) {
        this.result = new LocalVar(new Storage(irType), identifier);
    }

    @Override
    public void print(PrintStream out) {
        out.println("\t" + result.toString() + " = alloca " + result.storage.toString());
    }

    @Override
    public void printSSA(PrintStream out) {
        out.println("\t" + ssaResult.toString() + " = alloca " + result.storage.toString());
    }

    @Override
    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }

    @Override
    public ArrayList<Entity> getUse() {
        return null;
    }

    @Override
    public Entity getDef() {
        return result;
    }

    @Override
    public void promoteGlobalVar() {
        return;
    }

    @Override
    public void propagateLocalTmpVar() {
        return;
    }

    @Override
    public Constant getConstResult() {
        return null;
    }

    @Override
    public void setUse(ArrayList<SSAEntity> list) {
        return;
    }

    @Override
    public void setDef(SSAEntity entity) {
        ssaResult = entity;
    }

    @Override
    public ArrayList<SSAEntity> getSSAUse() {
        return null;
    }

    @Override
    public SSAEntity getSSADef() {
        return ssaResult;
    }


}
