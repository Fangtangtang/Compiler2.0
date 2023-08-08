package ir.entity.constant;

import ir.irType.*;

/**
 * @author F
 * bool常量
 * IR类型为i8
 * 值为true、false
 */
public class ConstBool extends Constant {
    private final boolean value;

    public ConstBool(boolean value) {
        super(new IntType(IntType.TypeName.BOOL));
        this.value = value;
    }

    @Override
    public String toString() {
        return this.type.toString() + " " + value;
    }
}
