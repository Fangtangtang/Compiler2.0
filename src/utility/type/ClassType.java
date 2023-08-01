package utility.type;

import java.util.*;

/**
 * @author F
 * class类型，类内定义成员、函数
 * classMembers：不同的class自己的成员
 */
public class ClassType extends Type {

    public String name;
    public HashMap<String, Type> classMembers = new HashMap<>();

    public ClassType() {
        this.typeName = Types.CLASS;
    }

    public ClassType(String name) {
        this.name = name;
        this.typeName = Types.CLASS;
        this.classMembers = new HashMap<>();
    }

    @Override
    public String toString() {
        return this.name;
    }

    //同为自定义类，类名相同
    @Override
    public boolean equals(Type other) {
        if(other instanceof NullType){
            return true;
        }
        return (other instanceof ClassType
                && this.name.equals(((ClassType) other).name));
    }
}
