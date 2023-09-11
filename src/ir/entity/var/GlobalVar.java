package ir.entity.var;

import ir.entity.*;
import ir.irType.PtrType;

/**
 * @author F
 * 全局的指针，指向存储全局变量的固定空间
 */
public class GlobalVar extends Ptr {
    public GlobalVar(Storage storage,
                     String identity) {
        super(storage, identity);
    }

    @Override
    public String toString() {
        return "@" + this.identity;
    }

    @Override
    public String renamed(String rename) {
        return "@" + rename;
    }
}
