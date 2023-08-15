package ir.stmt.terminal;

import ir.IRVisitor;
import ir.entity.Entity;
import ir.entity.Storage;
import ir.irType.VoidType;

import java.io.PrintStream;

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
        StringBuilder str = new StringBuilder("ret");
        if (value.type instanceof VoidType) {
            str.append(" void");
        } else {
            str.append(" ").append(value.toString());
        }
        out.println(str.toString());
    }

    @Override
    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }
}
