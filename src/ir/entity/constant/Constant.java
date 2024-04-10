package ir.entity.constant;

import ir.entity.Entity;
import ir.entity.Storage;
import ir.irType.IRType;

/**
 * @author F
 * 常量的抽象类
 */
public abstract class Constant extends Storage {

    public Constant(IRType type) {
        super(type);
    }

    public static boolean equalInValue(Constant a, Constant b) {
        if (a.getClass() != b.getClass()) {
            return false;
        }
        if (a instanceof ConstBool aBool) {
            return aBool.value == ((ConstBool) b).value;
        }
        if (a instanceof ConstInt aInt) {
            return aInt.value.equals(((ConstInt) b).value);
        }
        if (a instanceof ConstString aStr) {
            return aStr.value.equals(((ConstString) b).value);
        }
        return true;
    }

}
