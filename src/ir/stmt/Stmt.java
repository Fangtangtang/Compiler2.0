package ir.stmt;

import ir.IRVisitor;

import java.io.PrintStream;

/**
 * @author F
 * IR上的语句
 * 派生出instruction（IR指令）、terminalStmt（终结语句）
 */
public abstract class Stmt {
    public abstract void print(PrintStream out);

    public abstract void accept(IRVisitor irVisitor);
}
