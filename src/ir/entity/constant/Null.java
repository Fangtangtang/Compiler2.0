package ir.entity.constant;

import ir.irType.IRType;
import ir.irType.NullType;

/**
 * @author F
 * null
 */
public class Null extends Constant {

    public Null() {
        super(new NullType());
    }

    @Override
    public String toString() {
        return this.type.toString();
    }
}
