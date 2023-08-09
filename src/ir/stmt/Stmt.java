package ir.stmt;

import ir.IRVisitor;

/**
 * @author F
 * IR上的语句
 * 派生出instruction（IR指令）、terminalStmt（终结语句）
 */
public abstract class Stmt {
    public abstract void print();

    public abstract void accept(IRVisitor irVisitor);
}
