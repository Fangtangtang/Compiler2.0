package utility.type;

import utility.ParameterUnit;

import java.util.HashMap;

/**
 * @author F
 * 内置string类型
 */
public class StringType extends Type {
    boolean isConstant = false;

    public StringType() {
        this.typeName = Types.STRING;
    }

    public static void addBuildInFunc() {
        members = new HashMap<>();
        //内建方法
        //int length();
        FunctionType lengthFunc = new FunctionType(new IntType());
        members.put("length", lengthFunc);

        //string substring(int left, int right);
        FunctionType substringFunc = new FunctionType(new StringType());
        substringFunc.parameters.add(
                new ParameterUnit(new IntType(), "left")
        );
        substringFunc.parameters.add(
                new ParameterUnit(new IntType(), "right")
        );
        members.put("substring", substringFunc);

        //int parseInt();
        FunctionType parseIntFunc = new FunctionType(new IntType());
        members.put("parseInt", parseIntFunc);

        //int ord(int pos);
        FunctionType ordFunc = new FunctionType(new IntType());
        ordFunc.parameters.add(
                new ParameterUnit(new IntType(), "pos")
        );
        members.put("ord", ordFunc);
    }

    public StringType(boolean isConstant) {
        this.isConstant = isConstant;
        this.typeName = Types.STRING;
    }

    @Override
    public String toString() {
        return "string";
    }
}
