package asm.operand;

/**
 * @author F
 * 虚拟寄存器
 * 未被实际分配
 */
public class VirtualRegister extends Register {
    public String name;
    //相对fp的偏移量
    public int offset;

    public VirtualRegister(String name, int size) {
        this.name = name;
        this.size = size;
        index = ++id;
    }

    public VirtualRegister(int offset, int size) {
        this.offset = offset;
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
        return String.valueOf(offset) + "(fp)";
    }

    @Override
    public String toRegColoringString() {
        return color.name();
    }
}
