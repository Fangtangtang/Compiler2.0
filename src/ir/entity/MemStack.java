package ir.entity;

import ir.irType.IRType;

/**
 * @author F
 * 派生自Entity的MemStack
 * 内存栈(存放局部变量等)
 * 一个变量分配一份对应内存空间
 * ----------------------------------------------------------------
 * 局部变量使用alloca定义，用一个指针（i32）指向MemStack中一段空间
 */
public class MemStack extends Entity {
    //总数目
    private static int cnt = -1;
    private final int index;
    private final String identity;

    //由类型能确定占多少空间
    //指针指向的对象在内存中占空间的量
    public MemStack(IRType type,
                    String identity) {
        super(type);
        ++cnt;
        this.index = cnt;
        this.identity = identity;
    }

    @Override
    public String toString() {
        return "%" + identity;
    }
}


