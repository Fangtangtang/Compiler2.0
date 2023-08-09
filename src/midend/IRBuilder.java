package midend;

import ast.*;
import ast.expr.*;
import ast.expr.ConstantExprNode.*;
import ast.other.*;
import ast.stmt.*;
import ir.BasicBlock;
import ir.entity.Entity;
import ir.entity.constant.Constant;
import ir.IRRoot;
import ir.stmt.instruction.Alloca;
import utility.Pair;
import utility.SymbolTable;
import utility.scope.*;


/**
 * @author F
 * 遍历AST构建IR
 */
public class IRBuilder implements ASTVisitor<Entity> {
    private IRRoot irRoot;

    //当前作用域
    Scope currentScope = null;

    //当前块
    BasicBlock block = null;

    //全局变量的初始化块
    public Pair<BasicBlock, BasicBlock> globalInitBlock =
            new Pair<>(new BasicBlock("global_var_def"), new BasicBlock("global_val_init"));

    //当前的初始化块
    private Pair<BasicBlock, BasicBlock> currentInitBlock =
            new Pair<>(new BasicBlock("var_def"), new BasicBlock("val_init"));

    private IRBuilder(SymbolTable symbolTable) {
        irRoot = new IRRoot(symbolTable);
    }

    /**
     * RootNode
     * 访问AST根结点
     * 进入program，构建GlobalScope为当前作用域
     * 访问root的所有子结点
     * - 将所有的全局变量加到IRRoot下
     * - 构建全局变量的初始化函数
     * - 找到main，将所有全局变量的初始化函数加到main的entry
     *
     * @param node RootNode
     */
    @Override
    public Entity visit(RootNode node) {
        currentScope = new GlobalScope();
        node.declarations.forEach(
                def -> def.accept(this)
        );
        currentScope = currentScope.getParent();
        return null;
    }

    @Override
    public Entity visit(BlockStmtNode node) {

    }

    @Override
    public Entity visit(BreakStmtNode node) {

    }

    @Override
    public Entity visit(ConstructorDefStmtNode node) {

    }

    @Override
    public Entity visit(ContinueStmtNode node) {

    }

    @Override
    public Entity visit(ExprStmtNode node) {

    }

    @Override
    public Entity visit(ForStmtNode node) {

    }

    @Override
    public Entity visit(FuncDefStmtNode node) {

    }

    @Override
    public Entity visit(IfStmtNode node) {

    }

    @Override
    public Entity visit(ReturnStmtNode node) {

    }

    @Override
    public Entity visit(VarDefStmtNode node) {

    }

    @Override
    public Entity visit(WhileStmtNode node) {

    }

    @Override
    public Entity visit(ArrayVisExprNode node) {

    }

    @Override
    public Entity visit(AssignExprNode node) {

    }

    @Override
    public Entity visit(BinaryExprNode node) {

    }

    @Override
    public Entity visit(CmpExprNode node) {

    }

    @Override
    public Entity visit(FuncCallExprNode node) {

    }

    @Override
    public Entity visit(LogicExprNode node) {

    }

    @Override
    public Entity visit(LogicPrefixExprNode node) {

    }

    @Override
    public Entity visit(MemberVisExprNode node) {

    }

    @Override
    public Entity visit(NewExprNode node) {

    }

    @Override
    public Entity visit(PrefixExprNode node) {

    }

    @Override
    public Entity visit(PointerExprNode node) {

    }

    @Override
    public Entity visit(SuffixExprNode node) {

    }

    @Override
    public Entity visit(TernaryExprNode node) {

    }

    @Override
    public Entity visit(VarNameExprNode node) {

    }

    @Override
    public Entity visit(BoolConstantExprNode node) {

    }

    @Override
    public Entity visit(IntConstantExprNode node) {

    }

    @Override
    public Entity visit(NullConstantExprNode node) {

    }

    @Override
    public Entity visit(StrConstantExprNode node) {

    }

    @Override
    public Entity visit(ClassDefNode node) {

    }

    @Override
    public Entity visit(InitNode node) {

    }

    @Override
    public Entity visit(TypeNode node) {

    }

    /**
     * VarDefUnitNode变量定义
     * 开空间+可能有初始化,
     * - 全局变量，加到IRRoot下
     * - 局部变量:若被常量初始化，加入init
     *
     * @param node VarDefUnitNode
     */
    @Override
    public Entity visit(VarDefUnitNode node) {
        Entity entity = node.typeNode.accept(this);
        Alloca allocaInstruction;
        //global var
        if (currentScope instanceof GlobalScope) {
            allocaInstruction = new Alloca(true, entity.type, node.name);
            globalInitBlock.getFirst().pushBack(allocaInstruction);
            //have init?
            if (node.initExpr != null) {
                entity = node.initExpr.accept(this);
                //字面量,直接初始化；非字面量，用init函数
                if (entity instanceof Constant) {

                }
            }
        }
        //local var
        else {
            allocaInstruction = new Alloca(false, irType, node.name);
            currentInitBlock.getFirst().pushBack(allocaInstruction);
            //have init?
            if (node.initExpr != null) {

            }
        }

    }
}
