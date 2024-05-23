package ir.stmt.terminal;

import ir.*;
import ir.entity.Entity;
import ir.entity.Storage;
import ir.entity.constant.Constant;
import ir.entity.var.*;
import ir.irType.VoidType;
import ir.stmt.Stmt;
import utility.Pair;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author F
 * 作为block终止符的跳转语句
 */
public class Jump extends TerminalStmt {
    public String targetName;
    public BasicBlock target = null;
    public String phiLabel = null;
    public String index;
    public Storage result = null;

    public Jump(String targetName) {
        super();
        this.targetName = targetName;
    }

    public Jump(BasicBlock target) {
        super();
        this.targetName = target.label;
        this.target = target;
    }

    public Jump(BasicBlock target, String index,
                String phiLabel, Storage result) {
        super();
        this.targetName = target.label;
        this.target = target;
        this.index = index;
        this.phiLabel = phiLabel;
        if (result != null && (!(result.type instanceof VoidType))) {
            this.result = result;
        }
    }

    public Jump(String targetName, String index,
                String phiLabel, Storage result) {
        super();
        this.targetName = targetName;
        this.index = index;
        this.phiLabel = phiLabel;
        this.result = result;
    }

    @Override
    public void print(PrintStream out) {
        out.println("\tbr label %" + targetName);
    }

    @Override
    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }

    @Override
    public ArrayList<Entity> getUse() {
        ArrayList<Entity> ret = new ArrayList<>();
        ret.add(result);
        return ret;
//        return null;
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
        return;
    }

    @Override
    public void propagateLocalTmpVar() {
        if (result instanceof Ptr ptr) {
            result = ptr.valueInBasicBlock == null ? result : ptr.valueInBasicBlock;
        } else if (result instanceof LocalTmpVar tmpVar) {
            result = tmpVar.valueInBasicBlock == null ? result : tmpVar.valueInBasicBlock;
        }
    }

    @Override
    public Pair<Stmt, LocalTmpVar> creatCopy(String suffix) {
        Stmt stmt = new Jump(targetName + suffix, index + suffix, phiLabel, result);
        return new Pair<>(stmt, null);
    }

    @Override
    public void replaceUse(HashMap<String, Storage> constantMap) {
        result = (Storage) replace(result, constantMap);
    }

    @Override
    public void replaceUse(HashMap<LocalTmpVar, Storage> copyMap, HashMap<LocalVar, LocalVar> curAllocaMap) {
        result = (Storage) replace(result, copyMap, curAllocaMap);
    }
}
