package asm.operand;

import backend.optimizer.interferenceGraph.Colors;

import java.util.Objects;

/**
 * @author F
 * ASM寄存器
 */
public abstract class Register extends Operand {
    public Colors.Color color;

    static int id = 0;
    int index;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Register register)) {
            return false;
        }
        return index == register.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }
}
