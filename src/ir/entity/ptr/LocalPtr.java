package ir.entity.ptr;

import ir.entity.*;
import ir.irType.PtrType;

/**
 * @author F
 * 局部的指针，指向存储局部变量的固定空间
 */
public class LocalPtr extends Entity {
    private final String identity;
    public final Storage storage;

    public LocalPtr(Storage storage,
                    String identity) {
        super(new PtrType());
        this.storage = storage;
        this.identity = identity;
    }

    @Override
    public String toString() {
        return "%" + identity;
    }
}
