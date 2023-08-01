package utility.scope;

/**
 * @author F
 * while或for循环产生的作用域
 * 初始化表达式：该作用域的第一个stmt
 * TODO:附加什么
 * 循环条件
 * step表达式
 */
public class LoopScope extends Scope{
    public LoopScope(Scope parent) {
        super(parent);
    }
}
