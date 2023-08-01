package utility.scope;

import utility.ParameterUnit;
import utility.type.Type;

import java.util.ArrayList;

/**
 * @author F
 * 函数作用域块
 * 仅可能出现在全局或类定义
 * - 进入该作用域时，把参数表中的id->type映射加入
 * - 允许出现return，且语法检查时检查return类型
 * 特殊：全局main函数可缺省return，默认return 0
 */
public class FuncScope extends Scope {
    public Type returnType;

    public FuncScope(Scope parent,
                     Type returnType,
                     ArrayList<ParameterUnit> parameters) {
        super(parent);
        this.returnType = returnType;
        parameters.forEach(
                parameter -> this.name2type.put(parameter.name, parameter.type)
        );
    }
}
