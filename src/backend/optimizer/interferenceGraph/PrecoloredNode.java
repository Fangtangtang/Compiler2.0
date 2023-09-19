package backend.optimizer.interferenceGraph;

import asm.operand.PhysicalRegister;

/**
 * @author F
 * 预着色结点，对应于Physical Register
 */
public class PrecoloredNode extends Node {
    public PhysicalRegister register;

    public PrecoloredNode(PhysicalRegister register) {
        this.register = register;
        this.color = Colors.Color.valueOf(register.name);
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
        return "physical(" + register.name + ")";
    }
}
