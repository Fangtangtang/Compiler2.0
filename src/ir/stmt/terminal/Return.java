package ir.stmt.terminal;

import ir.IRVisitor;
import ir.entity.Entity;
import ir.entity.Storage;
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

    public Return() {
        super();
        this.value = new Storage(new VoidType());
    }

    public Return(Entity value) {
        super();
        this.value = value;
    }

    @Override
    public void print(PrintStream out) {
        StringBuilder str = new StringBuilder("\tret");
        if (value.type instanceof VoidType) {
            str.append(" void");
        } else {
            str.append(" ").append(value.type.toString()).append(" ").append(value.toString());
        }
        out.println(str.toString());
    }

    @Override
    public void printSSA(PrintStream out) {
        StringBuilder str = new StringBuilder("\tret");
        if (value.type instanceof VoidType) {
            str.append(" void");
        } else {
            str.append(" ").append(value.type.toString()).append(" ").append(value.renamedToString());
        }
        out.println(str.toString());
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
        return null;
    }
}
