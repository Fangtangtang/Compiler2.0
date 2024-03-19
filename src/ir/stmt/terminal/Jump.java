package ir.stmt.terminal;

import ir.*;
import ir.entity.Entity;
import ir.entity.SSAEntity;
import ir.entity.Storage;
import ir.entity.var.*;
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
    public Storage result;
    public SSAEntity ssaResult;

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
        this.result = result;
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
    public void printSSA(PrintStream out) {
        out.println("\tbr label %" + targetName);
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
        Stmt stmt = new Jump(targetName + suffix, index, phiLabel, result);
        return new Pair<>(stmt, null);
    }

    @Override
    public void replaceUse(HashMap<LocalTmpVar, Storage> copyMap, HashMap<LocalVar, LocalVar> curAllocaMap) {
        result = (Storage) replace(result, copyMap, curAllocaMap);
    }

    @Override
    public void setUse(ArrayList<SSAEntity> list) {
        return;
    }

    @Override
    public void setDef(SSAEntity entity) {
        return;
    }

    @Override
    public ArrayList<SSAEntity> getSSAUse() {
        return null;
    }

    @Override
    public SSAEntity getSSADef() {
        return null;
    }
}
