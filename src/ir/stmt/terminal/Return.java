package ir.stmt.terminal;

import ir.IRVisitor;
import ir.entity.Entity;
import ir.entity.Storage;
import ir.entity.constant.ConstInt;
import ir.entity.constant.Constant;
import ir.entity.var.GlobalVar;
import ir.entity.var.LocalTmpVar;
import ir.entity.var.LocalVar;
import ir.entity.var.Ptr;
import ir.irType.VoidType;
import ir.stmt.Stmt;
import utility.Pair;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author F
 * return语句
 * 函数中语句块的终结符
 * 可能有返回类型及返回值
 */
public class Return extends TerminalStmt {

    public Entity value;

    public Return() {
        super();
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
            if (value instanceof ConstInt constant) {
                str.append(" ").append(value.type.toString()).append(" ").append(((ConstInt) value).printValue());
            } else {
                str.append(" ").append(value.type.toString()).append(" ").append(value.toString());
            }
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
    public boolean hasDef() {
        return false;
    }

    @Override
    public Entity getDef() {
        return null;
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
        }
        if (value instanceof LocalTmpVar tmpVar) {
            value = tmpVar.valueInBasicBlock == null ? value : tmpVar.valueInBasicBlock;
        }
    }

    @Override
    public Pair<Stmt, LocalTmpVar> creatCopy(String suffix) {
        Stmt stmt = new Return(value);
        return new Pair<>(stmt, null);
    }

    @Override
    public void replaceUse(HashMap<String, Storage> constantMap) {
        value = replace(value, constantMap);
    }

    @Override
    public void replaceUse(HashMap<LocalTmpVar, Storage> copyMap, HashMap<LocalVar, LocalVar> curAllocaMap) {
        value = replace(value, copyMap, curAllocaMap);
    }
}
