package ir.entity.constant;

import ir.irType.IRType;
import ir.irType.IntType;

/**
 * @author F
 * 整型常量
 * 数值暂时存为string
 * 特殊：-1，2^31
 * i32
 */
public class ConstInt extends Constant {

    public String value;

    public ConstInt(String value) {
        super(new IntType(IntType.TypeName.INT));
        this.value = value;
    }

    @Override
    public String toString() {
        return this.type.toString() + " " + value;
    }

    @Override
    public String renamed(String remae) {
        return this.type.toString() + " " + value;
    }

    public String printValue() {
        return value;
    }
}
