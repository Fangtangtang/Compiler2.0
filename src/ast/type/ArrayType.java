package ast.type;

import ast.expr.ExprNode;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author F
 * 数组类型
 * eleType：数组基本元素的类型
 */
public class ArrayType extends Type {
    public Type eleType;
    public static HashMap<String, Type> members = new HashMap<>();
    public Integer dimensions;
    public ArrayList<ExprNode> dimensionList = new ArrayList<>();

    public ArrayType(Type eleType,
                     int dimensions) {
        this.typeName = Types.ARRAY;
        this.eleType = eleType;
        this.dimensions = dimensions;
    }

    public static void addBuildInFunc() {
        //数组内建方法

        members.put(
                "size",
                new FunctionType(new IntType())
        );
    }

    public void clarifyEleType(Type eleType) {
        this.eleType = eleType;
    }

    //    @Override
    public String print() {
        String str = typeName.name() + ' '
                + eleType.toString();
        return String.format("%s dim:%d", str, dimensions);
    }

    @Override
    public String toString() {
        return eleType.toString();
    }

    //同为数组，基本类型相同，维度相同
    @Override
    public boolean equals(Type other) {
        if (other instanceof NullType) {
            return true;
        }
        return (other instanceof ArrayType
                && this.eleType.equals(((ArrayType) other).eleType)
                && this.dimensions.equals(((ArrayType) other).dimensions));
    }

}

