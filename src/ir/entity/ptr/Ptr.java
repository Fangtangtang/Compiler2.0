package ir.entity.ptr;

import ir.entity.Entity;
import ir.entity.Storage;
import ir.irType.PtrType;

/**
 * @author F
 * alloca变量时得到的指针
 */
public class Ptr extends Entity {
    public final String identity;
    public final Storage storage;

    public Ptr(Storage storage,
               String identity) {
        super(new PtrType());
        this.storage = storage;
        this.identity = identity;
    }

    @Override
    public String toString() {
        return null;
    }
}
