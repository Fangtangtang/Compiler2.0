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
import ir.stmt.instruction.Global;
import ir.stmt.instruction.Store;
import utility.Pair;
import utility.SymbolTable;
import utility.scope.*;

import java.util.HashMap;


/**
 * @author F
 * 遍历AST构建IR
 */
public class IRBuilder implements ASTVisitor<Entity> {
    private IRRoot irRoot;

    //当前作用域
    Scope currentScope = null;

    //当前块
    BasicBlock currentBlock = null;

    //全局变量的初始化块；负责变量的空间申请
    public Pair<BasicBlock, BasicBlock> globalInitBlock =
            new Pair<>(new BasicBlock("global_var_def"), new BasicBlock("global_val_init"));

    //当前的初始化块,
    private BasicBlock currentInitBlock;
//            =new Pair<>(new BasicBlock("var_def"), new BasicBlock("val_init"));

    //计数id重复次数，更换id，避免重名
    private final HashMap<String, Integer> idCounter = new HashMap<>();

    /**
     * 将name转化，避免重名
     * - 全局\类成员不重命名
     * - 普通变量更名
     * 如果当前作用域中已经为该变量重命名，直接取rename
     * 否则创建新名，加入map
     *
     * @param name currentName
     * @return rename
     */
    private String rename(String name) {
        if (currentScope instanceof GlobalScope
                || currentScope instanceof ClassScope) {
            return name;
        }
        if (currentScope.nameMap.containsKey(name)) {
            return currentScope.nameMap.get(name);
        }
        Integer cnt;
        if (idCounter.containsKey(name)) {
            cnt = idCounter.get(name);
            cnt += 1;
        } else {
            cnt = 1;
            idCounter.put(name, cnt);
        }
        String rename = "_" + name + "_" + cnt.toString();
        currentScope.nameMap.put(name, rename);
        return rename;
    }

    public IRBuilder(SymbolTable symbolTable) {
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
        String name = rename(node.name);
        //entity为该类的0或null常量
        Constant constant = (Constant) node.typeNode.accept(this);
        Entity entity = null;
        //global var
        if (currentScope instanceof GlobalScope) {
            //have init?
            if (node.initExpr != null) {
                //如果为字面量，返回constant,直接初始化
                //否则返回在初始化函数中用赋值语句初始化
                entity = node.initExpr.accept(this);
                if (entity instanceof Constant) {
                    constant = (Constant) entity;
                }
            }
            //调用Global指令为全局变量分配空间
            Global stmt = new Global(constant, name);
            globalInitBlock.getFirst().pushBack(stmt);
            //向Scope中加入该全局变量
            currentScope.name2mem.put(name, stmt.result.storage);
            //非字面量初始化
            //在全局的初始化函数中加入赋值语句
            if (entity != null && !(entity instanceof Constant)) {
                globalInitBlock.getSecond().pushBack(
                        new Store(entity, stmt.result)
                );
            }
        }
        //local var
        else if (currentScope instanceof ClassScope) {
            //TODO:类内变量，加到类的初始化函数里
        } else {
            //普通局部变量，Alloca分配空间
            Alloca stmt = new Alloca(constant.type, name);
            currentInitBlock.pushBack(stmt);
            //向Scope中加入该局部变量
            currentScope.name2mem.put(name, stmt.result.storage);
            //若有初始化语句，在走到该部分时用赋值语句
            if (node.initExpr != null) {
                node.initExpr.accept(this);
            }
        }
        return null;
    }
}
