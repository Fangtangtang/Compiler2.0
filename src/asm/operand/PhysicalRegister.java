package asm.operand;

/**
 * @author F
 * 物理寄存器
 */
public class PhysicalRegister extends Register {
    public String name;
    public int valueSize = 4;

    public PhysicalRegister(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
