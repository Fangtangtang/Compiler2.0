package asm.operand;

/**
 * @author F
 * ASM立即数
 */
public class Imm extends Operand {
    int value;

    public Imm(int value) {
        this.value = value;
    }
}
