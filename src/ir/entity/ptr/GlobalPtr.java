package ir.entity.ptr;

import ir.entity.*;
import ir.irType.PtrType;

/**
 * @author F
 * 全局的指针，指向存储全局变量的固定空间
 */
public class GlobalPtr extends Entity {
    private final String identity;
    public final Storage storage;

    public GlobalPtr(Storage storage,
                     String identity) {
        super(new PtrType());
        this.storage = storage;
        this.identity = identity;
    }

    @Override
    public String toString() {
        return "@" + identity;
    }
}
