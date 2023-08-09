package ir.entity.ptr;

import ir.entity.*;
import ir.irType.PtrType;

/**
 * @author F
 * 局部的指针，指向存储局部变量的固定空间
 */
public class LocalPtr extends Ptr {


    public LocalPtr(Storage storage,
                    String identity) {
        super(storage, identity);
    }

    @Override
    public String toString() {
        return "%" + this.identity;
    }
}
