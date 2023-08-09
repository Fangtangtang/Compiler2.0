package midend;

import ast.ASTVisitor;
import ast.RootNode;
import ir.BasicBlock;
import ir.IRRoot;
import utility.SymbolTable;
import utility.type.Type;

/**
 * @author F
 * 遍历AST构建IR
 */
public class IRBuilder implements ASTVisitor<Type> {
    private IRRoot irRoot;

    //全局变量的初始化块，加到main的entry
    private BasicBlock globalInitBlock = new BasicBlock("global_init");

    private IRBuilder(SymbolTable symbolTable) {
        irRoot = new IRRoot(symbolTable);
    }

    /**
     * 访问AST根结点
     * - 将所有的全局变量加到IRRoot下
     * - 构建全局变量的初始化函数
     * - 找到main，将所有全局变量的初始化函数加到main的entry
     *
     * @param node RootNode
     */
    @Override
    public Type visit(RootNode node) {
        //访问root的所有子结点
    }
}
