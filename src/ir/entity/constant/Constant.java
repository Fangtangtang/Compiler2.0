package ir.entity.constant;

import ir.entity.Entity;
import ir.irType.IRType;

/**
 * @author F
 * 常量的抽象类
 */
public abstract class Constant extends Entity {

    public Constant(IRType type) {
        super(type);
    }

}
