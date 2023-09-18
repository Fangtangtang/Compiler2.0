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
    }

}
