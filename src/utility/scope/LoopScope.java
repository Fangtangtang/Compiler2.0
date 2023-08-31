package utility.scope;

import ast.expr.ExprNode;

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
    public int label;
    public FuncScope parentFuncScope = null;
    public ClassScope parentClassScope = null;
    public boolean hasCond = true;
    public boolean hasInc = true;

    public LoopScope(Scope parent,
                     ExprNode condition,
                     ExprNode step,
                     FuncScope parentFuncScope,
                     ClassScope parentClassScope) {
        super(parent);
        if (condition == null) {
            hasCond = false;
        }
        if (step == null) {
            hasInc = false;
        }
        this.parentFuncScope = parentFuncScope;
        this.parentClassScope = parentClassScope;
    }
}
