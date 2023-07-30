package utility.type;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author F
 * 数组类型
 * eleType：数组基本元素的类型
 * dimensionList：数组的维度列表
 */
public class ArrayType extends Type {
    public Type eleType;
    public Integer dimensions;
    public ArrayList<Integer> dimensionList = new ArrayList<>();

    public ArrayType(Type eleType,
                     int dimensions) {
        this.typeName = Types.ARRAY;
        this.eleType = eleType;
        this.dimensions = dimensions;
        //数组内建方法
        this.members = new HashMap<>();
        this.members.put(
                "size",
                new FunctionType(new IntType())
        );
    }

    @Override
    public String toString() {
        String str = typeName.name() + ' '
                + eleType.toString();
        return String.format("%s dim:%d", str, dimensions);
    }
}
