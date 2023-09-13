package ir.entity.constant;

import ir.irType.*;

/**
 * @author F
 * bool常量
 * IR类型为i1
 * 值为true、false
 */
public class ConstBool extends Constant {
    public boolean value;

    public ConstBool(boolean value) {
        super(new IntType(IntType.TypeName.BOOL));
        this.value = value;
    }

    @Override
    public String toString() {
        return this.type.toString() + " " + (value ? "1" : "0");
    }

    public String printValue() {
        return value ? "1" : "0";
    }

    @Override
    public String renamed(String rename) {
        return value ? "1" : "0";
    }
}
