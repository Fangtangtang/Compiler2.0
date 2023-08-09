package ir.irType;

/**
 * @author F
 * IR一维数组
 * 基本元素的类型和数组长度
 * 高维的数组由数组嵌套得到
 */
public class ArrayType extends IRType {
    private final IRType type;

    private final int size;

    public ArrayType(IRType type, int size) {
        this.type = type;
        this.size = size;
    }

    @Override
    public String toString() {
        return "[" + size + " x " + type.toString() + "]";
    }
}
