package ir.entity.var;

import ir.entity.*;
import ir.function.Function;
import ir.irType.PtrType;

import java.util.*;

/**
 * @author F
 * 全局的指针，指向存储全局变量的固定空间
 */
public class GlobalVar extends Ptr {
    //被转为local后的变量
    public LocalVar convertedLocalVar = null;

    //记录使用了该全局变量的所有函数(name)
    public HashSet<String> occurrence = new HashSet<>();

    public GlobalVar(Storage storage,
                     String identity) {
        super(storage, identity);
    }

    public Ptr promote() {
        return Objects.requireNonNullElse(convertedLocalVar, this);
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
