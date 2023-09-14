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
    }

    public VirtualRegister(int offset, int size) {
        this.offset = offset;
        this.size = size;
    }

    @Override
    public String toString() {
        return String.valueOf(offset) + "(fp)";
    }
}
