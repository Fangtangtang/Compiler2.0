package ir.entity;

import ir.irType.IRType;

/**
 * @author F
 * 派生自Entity
 * 内存中的数据区，存放全局变量
 */
public class DataStection extends Entity {
    private static int cnt = -1;
    private final int index;
    private final String identity;


    //由类型能确定占多少空间
    //TODO：type是否必要
    public DataStection(IRType type,
                    String identity) {
        super(type);
        ++cnt;
        this.index = cnt;
        this.identity = identity;
    }

    @Override
    public String toString() {
        return "DataStection " + index + ": " + identity + " " + type.toString();
    }
}
