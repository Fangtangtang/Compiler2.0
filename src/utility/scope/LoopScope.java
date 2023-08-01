package utility.scope;

import ast.*;

/**
 * @author F
 * while或for循环产生的作用域
 * 初始化表达式：该作用域的第一个stmt
 * TODO:附加什么
 * 循环条件
 * step表达式
 * 记录所在的func，loop即为自身
 */
public class LoopScope extends Scope {
    public FuncScope parentFuncScope = null;
    public ExprNode condition = null;
    public ExprNode step = null;

    public LoopScope(Scope parent,
                     ExprNode condition,
                     ExprNode step,
                     FuncScope parentFuncScope) {
        super(parent);
        this.condition = condition;
        this.step = step;
        this.parentFuncScope = parentFuncScope;
    }
}
