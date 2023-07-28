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
    Position pos;

    public ASTNode(Position pos) {
        this.pos = pos;
    }

    abstract public void accept(ASTVisitor visitor);
}