package asm.operand;

/**
 * @author F
 * ASM立即数
 */
public class Imm extends Operand {
    public int value;

    public Imm(int value) {
        this.value = value;
        size = 4;
    }

    public Imm(boolean bool) {
        if (bool) {
            this.value = 1;
        } else {
            this.value = 0;
        }
        size = 1;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
