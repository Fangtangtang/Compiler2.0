package ir.stmt.instruction;

import ir.stmt.Stmt;

/**
 * @author F
 * IR指令的抽象类
 * 派生类实现构造、accept、print
 * 考虑每一个指令需要有哪些信息、entity(constant\register)...
 */
public abstract class Instruction extends Stmt {
}
