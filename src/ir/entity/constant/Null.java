package ir.entity.constant;

import ir.irType.IRType;
import ir.irType.NullType;

/**
 * @author F
 * 空值常量
 * 默认为null型
 * 可以指定为其他类型（array\ptr\struct）的空值
 * 由被赋值对象确定类型
 */
public class Null extends Constant {
    public Null() {
        super(new NullType());
    }

    @Override
    public String toString() {
        return this.type.toString()+ " null";
    }
}
