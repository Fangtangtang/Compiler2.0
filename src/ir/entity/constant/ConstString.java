package ir.entity.constant;

import ir.irType.ArrayType;
import ir.irType.IRType;
import ir.irType.IntType;
import ir.irType.PtrType;

/**
 * @author F
 * 字符串常量
 * IR类型：指向字符数组的指针
 */
public class ConstString extends Constant {

    private final String value;

    public ConstString(String value) {
        super(new PtrType(
                new ArrayType(
                        new IntType(IntType.TypeName.CHAR),
                        value.length()
                )
        ));
        this.value = value;
    }

    @Override
    public String toString() {
        return type.toString() + " " + value;
    }
}
