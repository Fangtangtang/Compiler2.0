package ir.entity.constant;

import ir.irType.ArrayType;
import ir.irType.IRType;
import ir.irType.IntType;
import ir.irType.PtrType;

/**
 * @author F
 * 字符串常量
 * IR类型：字符数组
 */
public class ConstString extends Constant {

    private final String value;

    public ConstString(String value) {
        super(new ArrayType(
                new IntType(IntType.TypeName.CHAR),
                value.length()
        ));
        this.value = value;
    }

    @Override
    public String toString() {
        return type.toString() + " " + value;
    }
}
