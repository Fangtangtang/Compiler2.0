package utility.type;

import java.util.*;

/**
 * @author F
 * class类型，类内定义成员
 */
public class ClassType extends Type {

    public HashMap<String, Type> members = new HashMap<>();

    public ClassType() {
        this.type = Types.CLASS;
    }
}
