package ir.entity.constant;

import ir.irType.IRType;
import ir.irType.IntType;

/**
 * @author F
 * 整型常量
 * i32
 */
public class ConstInt extends Constant {

    private final int value;

    public ConstInt(int value) {
        super(new IntType(IntType.TypeName.INT));
        this.value = value;
    }

    @Override
    public String toString() {
        return this.type.toString() + " " + value;
    }
}
