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
        index = ++id;
    }

    //形式physical reg，不唯一
    public PhysicalRegister(String name, int size) {
        this.name = name;
        this.size = size;
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
        return name;
    }

    @Override
    public String toRegColoringString() {
        return name;
    }

}
