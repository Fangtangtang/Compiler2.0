package ir.stmt.terminal;

import ir.IRVisitor;
import ir.entity.Entity;
import ir.entity.SSAEntity;
import ir.entity.Storage;
import ir.entity.var.GlobalVar;
import ir.irType.VoidType;

import java.io.PrintStream;
import java.util.ArrayList;

/**
 * @author F
 * return语句
 * 函数中语句块的终结符
 * 可能有返回类型及返回值
 */
public class Return extends TerminalStmt {

    public Entity value;

    public SSAEntity ssaEntity;

    public Return() {
        super();
//        this.value = new Storage(new VoidType());
    }

    public Return(Entity value) {
        super();
        this.value = value;
    }

    @Override
    public void print(PrintStream out) {
        StringBuilder str = new StringBuilder("\tret");
//        if (value.type instanceof VoidType) {
        if (value == null) {
            str.append(" void");
        } else {
            str.append(" ").append(value.type.toString()).append(" ").append(value.toString());
        }
        out.println(str.toString());
    }

    @Override
    public void printSSA(PrintStream out) {
        StringBuilder str = new StringBuilder("\tret");
//        if (value.type instanceof VoidType) {
        if (value == null) {
            str.append(" void");
        } else {
            str.append(" ").append(value.type.toString()).append(" ").append(ssaEntity.toString());
        }
        out.println(str.toString());
    }

    @Override
    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }

    @Override
    public ArrayList<Entity> getUse() {
        if (value == null) {
            return null;
        }
        ArrayList<Entity> ret = new ArrayList<>();
        ret.add(value);
        return ret;
    }

    @Override
    public Entity getDef() {
        return null;
    }

    @Override
    public void promoteGlobalVar() {
        if (value instanceof GlobalVar globalVar && globalVar.convertedLocalVar != null){
            value = globalVar.convertedLocalVar;
        }
    }

    @Override
    public void setUse(ArrayList<SSAEntity> list) {
        ssaEntity = list.get(0);
    }

    @Override
    public void setDef(SSAEntity entity) {
        return;
    }

    @Override
    public ArrayList<SSAEntity> getSSAUse() {
        ArrayList<SSAEntity> ret = new ArrayList<>();
        ret.add(ssaEntity);
        return ret;
    }

    @Override
    public SSAEntity getSSADef() {
        return null;
    }
}
