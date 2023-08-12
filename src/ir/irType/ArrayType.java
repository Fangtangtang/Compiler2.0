package ir.irType;

/**
 * @author F
 * 指向数组的指针
 * IR一维数组(一长串的空间)
 * 基本元素的类型和数组长度
 * 高维的数组由数组嵌套得到
 */
public class ArrayType extends IRType {
    public IRType type;

    //数组维度
    public int dimension;

    //数组大小
    public int size;

    public ArrayType(IRType type, int dimension) {
        this.type = type;
        this.dimension = dimension;
    }

    @Override
    public String toString() {
        return "[" + dimension + " x " + type.toString() + "]";
    }
}
