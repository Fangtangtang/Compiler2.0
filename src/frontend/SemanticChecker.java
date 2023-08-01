package frontend;

import ast.ASTVisitor;
import ast.*;
import ast.expr.*;
import ast.stmt.*;
import utility.error.SemanticException;
import utility.scope.*;
import utility.type.*;

/**
 * @author F
 * 语法检查，遍历所有的ASTNode
 * 仅起检查正确性作用，不按break等顺序走
 */
public class SemanticChecker implements ASTVisitor<Type> {
    //当前所在的作用域
    Scope currentScope = null;

    private FuncScope getParentFuncScope() {
        if (currentScope instanceof FuncScope) {
            return (FuncScope) currentScope;
        }
        if (currentScope instanceof BlockScope) {
            return ((BlockScope) currentScope).parentFuncScope;
        }
        if (currentScope instanceof LoopScope) {
            return ((LoopScope) currentScope).parentFuncScope;
        }
        return null;
    }

    private LoopScope getParentLoopScope() {
        if (currentScope instanceof BlockScope) {
            return ((BlockScope) currentScope).parentLoopScope;
        }
        if (currentScope instanceof LoopScope) {
            return (LoopScope) currentScope;
        }
        return null;
    }

    public SemanticChecker() {
    }

    /**
     * RootNode
     * 进入program，构建GlobalScope为当前作用域
     * 进而检查子结点
     * 退出program，返回上层scope
     *
     * @param node ast的根
     * @return null
     */
    @Override
    public Type visit(RootNode node) {
        currentScope = new GlobalScope();
        node.declarations.forEach(
                def -> def.accept(this)
        );
        currentScope = currentScope.getParent();
        return null;
    }

    /**
     * BlockStmtNode
     * 用{}显式表示为作用域块
     * 进入时构建BlockScope
     * （将亲代的funcScope、loopScope加入）
     * 进而检查子结点
     * 退出返回到上层作用域
     *
     * @param node block结点
     * @return null
     */
    @Override
    public Type visit(BlockStmtNode node) {
        FuncScope funcScope = getParentFuncScope();
        LoopScope loopScope = getParentLoopScope();
        currentScope = new BlockScope(currentScope, funcScope, loopScope);
        node.statements.forEach(
                stmt -> stmt.accept(this)
        );
        currentScope = currentScope.getParent();
        return null;
    }

    /**
     * BreakStmtNode
     * 向上找到第一个循环语句块
     * 仅检查，不跳出
     *
     * @param node Break语句
     * @return null
     */
    @Override
    public Type visit(BreakStmtNode node) {
        LoopScope loopScope = getParentLoopScope();
        //break不在循环块中
        if (loopScope == null) {
            throw new SemanticException(node.pos, "invalid break statement");
        }
        return null;
    }

    /**
     * ConstructorDefStmtNode
     * 类的构造函数，从类作用域进入函数作用域
     * 结束后返回类作用域
     *
     * @param node 构造函数定义
     * @return classType
     */
    @Override
    public Type visit(ConstructorDefStmtNode node) {
        //返回类型为自定义类，无参数
        currentScope = new FuncScope(
                currentScope,
                Scope.symbolTable.getSymbol(node.name, node.pos),
                null
        );
        ((FuncScope) currentScope).isConstructor = true;
        visit(node.suite);
        currentScope = currentScope.getParent();
        return null;
    }

    /**
     * ContinueStmtNode
     * 跳过执行到一半的循环，开启下一次循环
     * 仍然在同一个循环作用域
     *
     * @param node continue语句
     * @return null
     */
    @Override
    public Type visit(ContinueStmtNode node) {
        LoopScope loopScope = getParentLoopScope();
        //break不在循环块中
        if (loopScope == null) {
            throw new SemanticException(node.pos, "invalid continue statement");
        }
        return null;
    }

    /**
     * ExprStmtNode
     * 不改变作用域，一一访问子结点
     *
     * @param node 表达式组成的语句
     * @return null
     */
    @Override
    public Type visit(ExprStmtNode node) {
        node.exprList.forEach(expr -> expr.accept(this));
        return null;
    }

    /**
     * ForStmtNode
     * 进入新LoopScope
     * initializationStmt作为作用域中第一个语句
     * 检查完后返回上一层
     *
     * @param node for循环
     * @return null
     */
    @Override
    public Type visit(ForStmtNode node) {
        FuncScope funcScope = getParentFuncScope();
        if (funcScope == null) {
            throw new SemanticException(node.pos, "for loop should be in function");
        }
        currentScope = new LoopScope(currentScope, node.condition, node.step, funcScope);
        if (!(node.condition.accept(this) instanceof BoolType)) {
            throw new SemanticException(node.condition.pos, "condition expr should be bool");
        }
        node.step.accept(this);
        node.initializationStmt.accept(this);
        node.statement.accept(this);
        currentScope = currentScope.getParent();
        return null;
    }

    /**
     * FuncDefStmtNode
     * 构建新的FuncScope
     * 考虑全局函数、类成员函数
     * 检查函数是否有return（特判全局main函数）
     *
     * @param node 函数定义
     * @return null
     */
    @Override
    public Type visit(FuncDefStmtNode node) {
        FunctionType type;
        boolean flag = false;
        //全局函数
        if (currentScope instanceof GlobalScope) {
            type = (FunctionType) Scope.symbolTable.getSymbol(node.name, node.pos);
            if ("main".equals(node.name)) {
                flag = true;
            }
        }
        //类成员函数
        else {
            type = (FunctionType) ((ClassScope) currentScope).classType.classMembers.get(node.name);
        }
        currentScope = new FuncScope(
                currentScope,
                type.returnType,
                type.parameters
        );
        visit(node.functionBody);
        if (!((FuncScope) currentScope).hasReturn && !flag) {
            throw new SemanticException(node.pos, "invalid function without return");
        }
        currentScope = currentScope.getParent();
        return null;
    }

    /**
     * IfStmtNode
     * 条件选择表达式，
     * trueStatement、falseStatement可能为隐式语句块
     * 若为隐式语句块，手动加blockScope
     *
     * @param node if语句
     * @return null
     */
    @Override
    public Type visit(IfStmtNode node) {
        if (!(node.condition.accept(this) instanceof BoolType)) {
            throw new SemanticException(node.condition.pos, "condition expr should be bool");
        }
        //trueStatement
        if (node.trueStatement instanceof BlockStmtNode) {
            node.trueStatement.accept(this);
        } else {
            FuncScope funcScope = getParentFuncScope();
            LoopScope loopScope = getParentLoopScope();
            currentScope = new BlockScope(currentScope, funcScope, loopScope);
            node.trueStatement.accept(this);
            currentScope = currentScope.getParent();
        }
        //falseStatement
        if (node.falseStatement != null) {
            if (node.falseStatement instanceof BlockStmtNode) {
                node.falseStatement.accept(this);
            } else {
                FuncScope funcScope = getParentFuncScope();
                LoopScope loopScope = getParentLoopScope();
                currentScope = new BlockScope(currentScope, funcScope, loopScope);
                node.falseStatement.accept(this);
                currentScope = currentScope.getParent();
            }
        }
        return null;
    }

    /**
     * ReturnStmtNode
     * 检查是否出现在FuncScope内
     * 检查返回的表达式类型和函数返回类型是否相符
     * - 自定义类类名
     * - 数组维度
     * TODO:构造函数允许return？
     * (暂认为不允许构造函数用return)
     *
     * @param node return表达式
     * @return null
     */
    @Override
    public Type visit(ReturnStmtNode node) {
        FuncScope funcScope = getParentFuncScope();
        //是否在非构造函数的函数作用域内
        if (funcScope == null || funcScope.isConstructor) {
            throw new SemanticException(node.pos, "invalid return statement");
        }
        //是否有返回值
        if (node.expression == null) {
            if (!(funcScope.returnType instanceof VoidType)) {
                throw new SemanticException(node.pos, "expect return, get null");
            }
        }
        //返回值类型是否合法
        else {
            Type type = node.expression.accept(this);
            funcScope.hasReturn = true;
            if (!(funcScope.returnType.equals(type))) {
                throw new SemanticException(node.expression.pos,
                        "return type " + type.toString() + " not match " + funcScope.returnType.toString()
                );
            }
        }
        return null;
    }

    /**
     * VarDefStmtNode
     * 不改变作用域
     *
     * @param node 变量定义语句
     * @return null
     */
    @Override
    public Type visit(VarDefStmtNode node) {
        node.varDefUnitNodes.forEach(
                var -> var.accept(this)
        );
        return null;
    }

    /**
     * WhileStmtNode
     * 循环语句应该出现在函数作用域内
     * 新建循环作用域
     *
     * @param node while循环语句
     * @return null
     */
    @Override
    public Type visit(WhileStmtNode node) {
        FuncScope funcScope = getParentFuncScope();
        if (funcScope == null) {
            throw new SemanticException(node.pos, "for loop should be in function");
        }
        currentScope = new LoopScope(
                currentScope,
                node.condition,
                null,
                funcScope
        );
        if (!(node.condition.accept(this) instanceof BoolType)) {
            throw new SemanticException(node.condition.pos, "condition expr should be bool");
        }
        node.statement.accept(this);
        currentScope = currentScope.getParent();
        return null;
    }

    /**
     * ArrayVisExprNode
     * 调用访问数组名 -> arrayName类型，是否为数组
     * 调用访问indexList -> index是否都为int
     * 计算返回类型的维度
     *
     * @param node 数组下标访问表达式
     * @return node.exprType
     * - 仍然为数组
     * - 单个变量
     */
    @Override
    public Type visit(ArrayVisExprNode node) {
        Type tmp = node.arrayName.accept(this);
        if (!(tmp instanceof ArrayType array)) {
            throw new SemanticException(node.arrayName.pos, "invalid array name");
        }
        node.indexList.forEach(expr -> {
            if (!(expr.accept(this) instanceof IntType)) {
                throw new SemanticException(expr.pos, "invalid array index");
            }
        });
        if (node.indexList.size() > array.dimensions) {
            throw new SemanticException(node.pos, "dimension out of bound");
        }
        if (node.indexList.size() == array.dimensions) {
            node.exprType = array.eleType;
        } else {
            node.exprType = new ArrayType(
                    array.eleType,
                    array.dimensions - node.indexList.size()
            );
        }
        return node.exprType;
    }

    /**
     * AssignExprNode
     * 检查赋值表达式左右两边表达式类型是否一致
     *
     * @param node 赋值表达式
     * @return null
     * 赋值表达式类型无意义
     */
    @Override
    public Type visit(AssignExprNode node) {
        if (!node.lhs.isAssignable) {
            throw new SemanticException(node.pos, "invalid assign to a not assignable object");
        }
        if ((node.lhs.accept(this)).equals(node.rhs.accept(this))) {
            return null;
        }
        throw new SemanticException(node.pos, "invalid assign. unmatched types");
    }


    /**
     * BinaryExprNode
     * int、string(+)
     * 调用访问左右两式获得类型
     * 类型是否匹配？
     * 是否为合法运算
     *
     * @param node 二元计算表达式
     * @return node.exprType
     */
    @Override
    public Type visit(BinaryExprNode node) {
        Type left = node.lhs.accept(this);
        Type right = node.rhs.accept(this);
        if (!left.equals(right)) {
            throw new SemanticException(node.pos, "invalid binary expression. unmatched types");
        }
        if ((left instanceof StringType) && node.operator == BinaryExprNode.BinaryOperator.Plus) {
            node.exprType = new StringType();
        } else if (left instanceof IntType) {
            node.exprType = new IntType();
        } else {
            throw new SemanticException(node.pos, "invalid types in binary expression");
        }
        return node.exprType;
    }

    /**
     * CmpExprNode
     * string\int
     * array (== | !=) null
     * bool\class (== | !=)
     * 调用访问左右两式获得类型
     * 类型是否匹配？
     * 是否为合法运算     *
     *
     * @param node 二元比较表达式
     * @return node.exprType（bool）
     */
    @Override
    public Type visit(CmpExprNode node) {
        Type left = node.lhs.accept(this);
        Type right = node.rhs.accept(this);
        //左右异类
        //array - null
        if ((left instanceof ArrayType)
                && (right instanceof NullType)) {
            if (node.operator == CmpExprNode.CmpOperator.Equal
                    || node.operator == CmpExprNode.CmpOperator.NotEqual) {
                node.exprType = new BoolType();
            } else {
                throw new SemanticException(node.pos, "invalid operator in array compare expression.");
            }
        } else if (!left.equals(right)) {
            throw new SemanticException(node.pos, "invalid compare expression. unmatched types");
        }
        //左右同类型
        else {
            if (left instanceof ClassType || left instanceof BoolType) {
                if (node.operator == CmpExprNode.CmpOperator.Equal
                        || node.operator == CmpExprNode.CmpOperator.NotEqual) {
                    node.exprType = new BoolType();
                } else {
                    throw new SemanticException(node.pos, "invalid operator in " + left.toString() + " compare expression.");
                }
            } else if (left instanceof StringType || left instanceof IntType) {
                node.exprType = new BoolType();
            } else {
                throw new SemanticException(node.pos, "invalid types in binary compare expression");
            }
        }
        return node.exprType;
    }

    /**
     * FuncCallExprNode
     * 调用访问func，检查是否为FunctionType
     * 参数表是否对应
     *
     * @param node 函数调用表达式
     * @return node.exprType
     * 函数返回类型
     */
    @Override
    public Type visit(FuncCallExprNode node) {
        Type type=node.func.accept(this);
        if(!(type instanceof FunctionType function)){
            throw new SemanticException(node.pos, "invalid function name");
        }

    }
}
