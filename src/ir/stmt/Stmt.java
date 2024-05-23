package ir.stmt;

import ir.IRVisitor;
import ir.entity.Entity;
import ir.entity.Storage;
import ir.entity.constant.Constant;
import ir.entity.var.LocalTmpVar;
import ir.entity.var.LocalVar;
import utility.Pair;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author F
 * IR上的语句
 * 派生出instruction（IR指令）、terminalStmt（终结语句）
 */
public abstract class Stmt implements Serializable {
    public String inBlockLabel;

    public boolean isDead = false;

    public abstract void print(PrintStream out);

    public abstract void accept(IRVisitor irVisitor);

    public abstract ArrayList<Entity> getUse();

    public abstract boolean hasDef();

    public abstract Entity getDef();

    public abstract void promoteGlobalVar();

    //将能替换的变量替换掉（含部分常量传播）
    public abstract void propagateLocalTmpVar();

    //为内联的语句创建副本
    //包括需要新建的def
    public abstract Pair<Stmt, LocalTmpVar> creatCopy(String suffix);

//    public abstract void replaceUse(HashMap<LocalTmpVar, Constant> constantMap);

    public abstract void replaceUse(HashMap<LocalTmpVar, Storage> copyMap, HashMap<LocalVar, LocalVar> curAllocaMap);

    public abstract void replaceUse(HashMap<String, Storage> copyMap);

    public Entity replace(Entity entity, HashMap<String, Storage> constantMap) {
        if (entity != null && constantMap.containsKey(entity.toString())) {
            return constantMap.get(entity.toString());
        }
        return entity;
    }

//    public Entity replace(Entity entity, HashMap<String, Constant> constantMap) {
//        if (entity instanceof LocalTmpVar tmpVar && constantMap.containsKey(tmpVar)) {
//            return constantMap.get(tmpVar.toString());
//        }
//        return entity;
//    }

    public Entity replace(Entity entity, HashMap<LocalTmpVar, Storage> copyMap, HashMap<LocalVar, LocalVar> curAllocaMap) {
        if (entity instanceof LocalVar && curAllocaMap != null && curAllocaMap.containsKey(entity)) {
            entity = curAllocaMap.get(entity);
        } else if (entity instanceof LocalTmpVar && copyMap != null && copyMap.containsKey(entity)) {
            entity = copyMap.get(entity);
        }
        return entity;
    }

}
