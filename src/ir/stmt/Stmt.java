package ir.stmt;

import ir.IRVisitor;
import ir.entity.Entity;
import ir.entity.SSAEntity;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author F
 * IR上的语句
 * 派生出instruction（IR指令）、terminalStmt（终结语句）
 */
public abstract class Stmt implements Serializable {
    public abstract void print(PrintStream out);

    public abstract void printSSA(PrintStream out);

    public abstract void accept(IRVisitor irVisitor);

    public abstract ArrayList<Entity> getUse();

    public abstract Entity getDef();

    public abstract void promoteGlobalVar();

    //将能替换的变量替换掉（含部分常量传播）
    public abstract void propagateLocalTmpVar();

//    public abstract Stmt cloneStmt();

    public abstract void setUse(ArrayList<SSAEntity> list);

    public abstract void setDef(SSAEntity entity);

    public abstract ArrayList<SSAEntity> getSSAUse();

    public abstract SSAEntity getSSADef();
}
