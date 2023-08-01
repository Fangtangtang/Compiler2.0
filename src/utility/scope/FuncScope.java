package utility.scope;

import utility.ParameterUnit;
import utility.type.Type;
import utility.type.VoidType;

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
    public boolean hasReturn = false;
    public boolean isConstructor = false;
    public ClassScope parentClassScope = null;

    public FuncScope(Scope parent,
                     Type returnType,
                     ArrayList<ParameterUnit> parameters,
                     ClassScope parentClassScope) {
        super(parent);
        this.parentClassScope = parentClassScope;
        this.returnType = returnType;
        if (returnType instanceof VoidType) {
            hasReturn = true;
        }
        if (parameters != null) {
            parameters.forEach(
                    parameter -> this.name2type.put(parameter.name, parameter.type)
            );
        }
    }
}
