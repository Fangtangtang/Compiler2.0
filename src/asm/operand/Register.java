package asm.operand;

import backend.optimizer.interferenceGraph.Colors;

/**
 * @author F
 * ASM寄存器
 */
public abstract class Register extends Operand {
    public Colors.Color color;
}
