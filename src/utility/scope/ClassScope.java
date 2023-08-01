package utility.scope;

import utility.type.ClassType;

/**
 * @author F
 * 类定义作用域
 */
public class ClassScope extends Scope {
    public ClassType classType;

    public ClassScope(Scope parent,
                      ClassType classType) {
        super(parent);
        this.classType = classType;
    }
}
