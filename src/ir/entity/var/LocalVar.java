package ir.entity.var;

import ir.entity.*;
import ir.irType.PtrType;

/**
 * @author F
 * 局部的指针，指向存储局部变量的固定空间
 */
public class LocalVar extends Ptr {

    public LocalVar(Storage storage,
                    String identity) {
        super(storage, identity);
    }

    @Override
    public String toString() {
        return "%" + this.identity;
    }
}
