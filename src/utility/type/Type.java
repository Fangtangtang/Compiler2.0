package utility.type;

import ir.irType.IRType;

import java.util.HashMap;

/**
 * @author F
 * 标识类型的基类
 * members:类公有的方法（static）
 * classMembers：class的成员
 */
abstract public class Type {
    enum Types {
        BOOL, INT, STRING,
        VOID,
        ARRAY, CLASS, FUNCTION,
        NULL
    }

    public Types typeName;

    @Override
    public String toString() {
        return typeName.name();
    }

    public abstract boolean equals(Type other);
}
