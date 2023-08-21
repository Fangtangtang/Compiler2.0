package asm.operand;

/**
 * @author F
 * 虚拟寄存器
 * 未被实际分配
 */
public class VirtualRegister extends Register {
    //相对fp的偏移量
    public int offset;

    public VirtualRegister(int offset) {
        this.offset = offset;
    }
}
