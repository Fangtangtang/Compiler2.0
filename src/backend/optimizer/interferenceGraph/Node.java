package backend.optimizer.interferenceGraph;

import asm.instruction.MoveInst;

import java.util.*;

/**
 * @author F
 * 冲突图结点抽象类
 */
public abstract class Node {
    public int degree;
    public Node alias;
    public Colors.Color color;
    //节点相关的move表
    public HashSet<MoveInst> moveList = new HashSet<>();

    @Override
    public abstract String toString();

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Node)) {
            return false;
        }
        return this.toString().equals(obj.toString());
    }
}
