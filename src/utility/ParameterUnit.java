package utility;

import utility.type.Type;

/**
 * @author F
 * 函数参数表组成部分
 */
public class ParameterUnit {
    public Type type;
    public String name;

    public ParameterUnit(Type type,
                         String name) {
        this.type = type;
        this.name = name;
    }
}
