package ast;

import utility.Position;

/**
 * @author F
 * ASTNode抽象类，AST的结点
 * pos：保存结点对应语句或字段在源文件中的位置
 * accept函数：接受ASTVisitor访问
 *          （可用于visit泛化，不同ASTNode都调用it.accept(this)，支持访问）
 */
abstract public class ASTNode {
    public Position pos;

    public ASTNode(Position pos) {
        this.pos = pos;
    }

    //使用 <T> 来引入一个泛型参数 T
    //返回一个类型为 T 的值
    //ASTVisitor<? extends T>
    //传递一个实现了 ParseTreeVisitor 接口的对象，并且这个对象的类型是 T 或其子类
    abstract public <T> T accept(ASTVisitor<? extends T> visitor);
}