package ir.entity;

import ir.irType.*;

/**
 * @author F
 * 实体抽象类
 * 派生出constant、register
 */
public abstract class Entity {
    public IRType type;

    public Entity(IRType type) {
        this.type = type;
    }

    @Override
    public abstract String toString();

    public abstract String renamedToString();
}
