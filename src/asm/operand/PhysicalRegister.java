package asm.operand;

/**
 * @author F
 * 物理寄存器
 */
public class PhysicalRegister extends Register {
    public String name;

    public PhysicalRegister(String name) {
        this.name = name;
        size = 4;
    }

    @Override
    public String toString() {
        return name;
    }
}
