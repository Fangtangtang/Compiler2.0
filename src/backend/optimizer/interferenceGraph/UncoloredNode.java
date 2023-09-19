package backend.optimizer.interferenceGraph;

import asm.operand.*;

/**
 * @author F
 * 未着色结点
 * 对应于Virtual Register \ Stack Register
 */
public class UncoloredNode extends Node {
    public VirtualRegister register;

    public UncoloredNode(VirtualRegister register) {
        this.register = register;
        index = ++id;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public String toString() {
        return "virtual(" + register.name + ")";
    }
}
