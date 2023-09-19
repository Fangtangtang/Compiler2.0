package asm.operand;

/**
 * @author F
 * 分配到栈上的virtual register的映射
 */
public class StackRegister extends Register {
    //相对fp的偏移量
    public int offset;

    public StackRegister(int offset, int size) {
        this.offset = offset;
        this.size = size;
        index = ++id;
    }

    @Override
    public String toString() {
        return String.valueOf(offset) + "(fp)";
    }

    @Override
    public String toRegColoringString() {
        return String.valueOf(offset) + "(fp)";
    }
}
