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

}
