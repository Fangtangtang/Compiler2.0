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
    static int id = 0;
    int index;
    //节点相关的move表
    public HashSet<MoveInst> moveList = new HashSet<>();

    @Override
    public abstract String toString();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Node node)) {
            return false;
        }
        return index == node.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }
}
