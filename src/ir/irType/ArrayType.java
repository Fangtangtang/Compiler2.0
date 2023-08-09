package ir.irType;

/**
 * @author F
 * IR一维数组(一长串的空间)
 * 基本元素的类型和数组长度
 * 高维的数组由数组嵌套得到
 */
public class ArrayType extends IRType {
    private final IRType type;

    //数组维度
    private final int dimension;

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
