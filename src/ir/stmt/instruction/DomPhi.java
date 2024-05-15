package ir.stmt.instruction;

import ir.IRVisitor;
import ir.entity.Entity;
import ir.entity.Storage;
import ir.entity.constant.Constant;
import ir.entity.var.LocalTmpVar;
import ir.entity.var.LocalVar;
import ir.stmt.Stmt;
import utility.Pair;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author F
 * 在 dom frountier插入的phi指令
 */
public class DomPhi extends Instruction {
    public LocalTmpVar result;

    public HashMap<String, Storage> phiList = new HashMap<>();

    // todo ==========================================================================================
    @Override
    public void print(PrintStream out) {

    }

    @Override
    public void accept(IRVisitor irVisitor) {

    }

    @Override
    public ArrayList<Entity> getUse() {
        return null;
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

    }

    @Override
    public void propagateLocalTmpVar() {

    }

    @Override
    public Pair<Stmt, LocalTmpVar> creatCopy(String suffix) {
        return null;
    }

    @Override
    public void replaceUse(HashMap<LocalTmpVar, Constant> constantMap) {

    }

    @Override
    public void replaceUse(HashMap<LocalTmpVar, Storage> copyMap, HashMap<LocalVar, LocalVar> curAllocaMap) {

    }

    @Override
    public Constant getConstResult() {
        return null;
    }
    // todo ==========================================================================================

}
