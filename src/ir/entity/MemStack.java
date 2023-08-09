package ir.entity;

import ir.irType.IRType;

/**
 * @author F
 * 派生自Entity的MemStack
 * 内存栈(存放局部变量等)
 * 一个变量分配一份对应内存空间
 */
public class MemStack extends Entity {
    //总数目
    private static int cnt = -1;
    private final int index;
    private final String identity;

    //由类型能确定占多少空间
    //TODO：type是否必要
    public MemStack(IRType type,
                    String identity) {
        super(type);
        ++cnt;
        this.index = cnt;
        this.identity = identity;
    }

    //TODO：打印内容
    @Override
    public String toString() {
        return "memStack " + index + ": " + identity + " " + type.toString();
    }
}


