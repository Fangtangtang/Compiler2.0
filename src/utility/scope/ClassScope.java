package utility.scope;

import ast.type.ClassType;

/**
 * @author F
 * 类作用域
 * 类定义会构建一个类作用域
 * 成员访问会进入一个类作用域
 * 类包括
 * - 自定义类
 */
public class ClassScope extends Scope {
    public ClassType classType;

    public ClassScope(Scope parent,
                      ClassType classType) {
        super(parent);
        this.classType = classType;
    }
}
