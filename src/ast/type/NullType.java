package ast.type;

/**
 * @author F
 * null
 */
public class NullType extends Type {
    public NullType() {
        this.typeName = Types.NULL;
    }

    @Override
    public String toString() {
        return "null";
    }

    //任意未赋值的类型都为null（不指向任何对象的值）
    //字符串对象赋值为 null 是语法错误
    //字符串仅可与相同类型对象进行运算
    //TODO:字符串和null的运算等的合法性
    @Override
    public boolean equals(Type other) {
        return (! (other instanceof StringType));
//        return (other instanceof NullType || other instanceof VoidType);
    }
}
