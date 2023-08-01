package frontend;

import ast.ASTVisitor;
import ast.*;
import ast.expr.*;
import ast.expr.ConstantExprNode.BoolConstantExprNode;
import ast.expr.ConstantExprNode.IntConstantExprNode;
import ast.expr.ConstantExprNode.NullConstantExprNode;
import ast.expr.ConstantExprNode.StrConstantExprNode;
import ast.other.ClassDefNode;
import ast.other.InitNode;
import ast.other.TypeNode;
import ast.other.VarDefUnitNode;
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

    /**
     * 带成员的类型
     * 包括内置的string、array
     * 用于在成员访问语句中传递信息
     * ------------------------------
     * 成员访问时，将currentClass置为获得的成员类型（lhs）
     * 要获得identifier的类型
     * 先看currentClass是否是null
     * 如果null，在作用域找
     * 如果不是null，在currentClass的member里找
     */
    Type currentClass = null;

    private ClassScope getParentClassScope() {
        if (currentScope instanceof BlockScope) {
            return ((BlockScope) currentScope).parentClassScope;
        }
        if (currentScope instanceof LoopScope) {
            return ((LoopScope) currentScope).parentClassScope;
        }
        if (currentScope instanceof FuncScope) {
            return ((FuncScope) currentScope).parentClassScope;
        }
        if (currentScope instanceof ClassScope) {
            return (ClassScope) currentScope;
        }
        return null;
    }

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
        ClassScope classScope = getParentClassScope();
        FuncScope funcScope = getParentFuncScope();
        LoopScope loopScope = getParentLoopScope();
        currentScope = new BlockScope(currentScope, funcScope, loopScope, classScope);
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
        ClassScope classScope = getParentClassScope();
        //返回类型为自定义类，无参数
        currentScope = new FuncScope(
                currentScope,
                Scope.symbolTable.getSymbol(node.name, node.pos),
                null,
                classScope
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
     * 先initializationStmt
     * 然后进入新LoopScope
     * 检查完后返回上一层
     *
     * @param node for循环
     * @return null
     */
    @Override
    public Type visit(ForStmtNode node) {
        ClassScope classScope = getParentClassScope();
        FuncScope funcScope = getParentFuncScope();
        if (funcScope == null) {
            throw new SemanticException(node.pos, "for loop should be in function");
        }
        if (node.initializationStmt != null) {
            node.initializationStmt.accept(this);
        }
        currentScope = new LoopScope(currentScope, node.condition, node.step, funcScope, classScope);
        if (node.condition != null) {
            if (!(node.condition.accept(this) instanceof BoolType)) {
                throw new SemanticException(node.condition.pos, "condition expr should be bool");
            }
        }
        if (node.step != null) {
            node.step.accept(this);
        }
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
        //获取函数标签：返回类型、参数表
        FunctionType type;
        ClassScope classScope = null;
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
            classScope = (ClassScope) currentScope;
            type = (FunctionType) ((ClassScope) currentScope).classType.classMembers.get(node.name);
        }
        //构建函数作用域
        currentScope = new FuncScope(
                currentScope,
                type.returnType,
                type.parameters,
                classScope
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
            ClassScope classScope = getParentClassScope();
            FuncScope funcScope = getParentFuncScope();
            LoopScope loopScope = getParentLoopScope();
            currentScope = new BlockScope(currentScope, funcScope, loopScope, classScope);
            node.trueStatement.accept(this);
            currentScope = currentScope.getParent();
        }
        //falseStatement
        if (node.falseStatement != null) {
            if (node.falseStatement instanceof BlockStmtNode) {
                node.falseStatement.accept(this);
            } else {
                ClassScope classScope = getParentClassScope();
                FuncScope funcScope = getParentFuncScope();
                LoopScope loopScope = getParentLoopScope();
                currentScope = new BlockScope(currentScope, funcScope, loopScope, classScope);
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
        ClassScope classScope = getParentClassScope();
        FuncScope funcScope = getParentFuncScope();
        if (funcScope == null) {
            throw new SemanticException(node.pos, "for loop should be in function");
        }
        currentScope = new LoopScope(
                currentScope,
                node.condition,
                null,
                funcScope,
                classScope
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
     * 调用访问数组名 -> arrayName类型，是否为数组、是否为null
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
//        if (!node.arrayName.isAssignable) {
//            throw new SemanticException(node.arrayName.pos, "visit invalid array");
//        }
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
        node.lhs.accept(this);
        if (!node.lhs.isAssignable) {
            throw new SemanticException(node.pos, "invalid assign to a not assignable object");
        }
        if (node.lhs.exprType.equals(node.rhs.accept(this))) {
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
     * - array和null视为同一类型（array.equals(null)==true）
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
        if (!left.equals(right)) {
            throw new SemanticException(node.pos, "invalid compare expression. unmatched types");
        }
        //左右同类型
        if (left instanceof ClassType || left instanceof BoolType || left instanceof ArrayType) {
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
        Type type = node.func.accept(this);
        if (!(type instanceof FunctionType function)) {
            throw new SemanticException(node.pos, "invalid function name");
        }
        //参数检查
        if (node.parameterList.size() != function.parameters.size()) {
            throw new SemanticException(node.pos, "invalid function parameterList");
        }
        for (int i = 0; i < node.parameterList.size(); ++i) {
            if (!(node.parameterList.get(i).accept(this)).equals(function.parameters.get(i).type)) {
                throw new SemanticException(node.parameterList.get(i).pos, "unmatched function parameter type");
            }
        }
        node.exprType = function.returnType;
        return node.exprType;
    }

    /**
     * LogicExprNode
     * 检查左右两式，都应该为bool
     *
     * @param node 二元逻辑运算表达式
     * @return node.exprType
     */
    @Override
    public Type visit(LogicExprNode node) {
        Type left = node.lhs.accept(this);
        Type right = node.rhs.accept(this);
        if (!left.equals(right)) {
            throw new SemanticException(node.pos, "invalid logic expression. unmatched types");
        } else if (!(left instanceof BoolType)) {
            throw new SemanticException(node.pos, "logic expression should operate on bool types");
        } else {
            node.exprType = new BoolType();
        }
        return node.exprType;
    }

    /**
     * LogicPrefixExprNode
     * bool
     *
     * @param node 逻辑运算前缀表达式
     * @return node.exprType
     */
    @Override
    public Type visit(LogicPrefixExprNode node) {
        if (!(node.expression.accept(this) instanceof BoolType)) {
            throw new SemanticException(node.pos, "logic expression should operate on bool type");
        } else {
            node.exprType = new BoolType();
        }
        return node.exprType;
    }

    /**
     * MemberVisExprNode
     * 获取类的成员函数、成员对象
     * 调用访问lhs获得类型，给currentClass赋值
     * 再去获得左式
     *
     * @param node 成员访问
     * @return node.exprType
     */
    @Override
    public Type visit(MemberVisExprNode node) {
        currentClass = node.lhs.accept(this);
        node.exprType = node.rhs.accept(this);
        return node.exprType;
    }

    /**
     * NewExprNode
     * arrayConstruction | varConstruction
     * 数组：typeNode为array型
     *
     * @param node 新建变量、数组
     * @return node.exprType
     * 新建的类型
     */
    @Override
    public Type visit(NewExprNode node) {
        node.exprType = node.typeNode.accept(this);
        if(node.exprType instanceof ArrayType){
            node.dimensions.forEach(
                    dim -> {
                        if (!(dim.accept(this) instanceof IntType ind)) {
                            throw new SemanticException(node.pos, "invalid array index");
                        }
                        ((ArrayType)node.exprType).dimensionList.add(dim);
                    }
            );
        }
//        if (node.dimension > 0) {
//            node.exprType = new ArrayType(eleType, node.dimension);
//        } else {
//            node.exprType = eleType;
//        }
        return node.exprType;
    }

    /**
     * PrefixExprNode
     * ++a,--a可以赋值
     *
     * @param node 非逻辑运算的前缀表达式
     * @return node.exprType (int)
     */
    @Override
    public Type visit(PrefixExprNode node) {
        Type type = node.expression.accept(this);
        if (type instanceof IntType) {
            node.exprType = new IntType();
            if (node.operator == PrefixExprNode.PrefixOperator.PlusPlus
                    || node.operator == PrefixExprNode.PrefixOperator.Minus) {
                node.isAssignable = true;
            }
            return node.exprType;
        }
        throw new SemanticException(node.pos, "invalid type in prefix expression");
    }

    /**
     * PointerExprNode
     * 仅可在类作用域内层使用
     * 不可赋值
     *
     * @param node 类的this指针访问
     * @return node.exprType
     * 类类型
     */
    @Override
    public Type visit(PointerExprNode node) {
        ClassScope classScope = getParentClassScope();
        if (classScope == null) {
            throw new SemanticException(node.pos, "'this' should appear in class declaration");
        }
        node.exprType = classScope.classType;
        return node.exprType;
    }

    /**
     * SuffixExprNode
     * 不可赋值
     *
     * @param node 后缀表达式
     * @return node.exprType（int）
     */
    @Override
    public Type visit(SuffixExprNode node) {
        Type type = node.expression.accept(this);
        if (type instanceof IntType) {
            node.exprType = new IntType();
            return node.exprType;
        }
        throw new SemanticException(node.pos, "invalid type in suffix expression");
    }

    /**
     * TernaryExprNode
     * a ? b : c
     * a 为 bool 类型
     * b 与 c 的类型一致
     *
     * @param node 三目运算表达式
     * @return node.exprType
     * 返回值类型为 b 与 c 的类型
     */
    @Override
    public Type visit(TernaryExprNode node) {
        if (!(node.condition.accept(this) instanceof BoolType)) {
            throw new SemanticException(node.condition.pos, "condition expr should be bool");
        }
        Type left = node.trueExpr.accept(this);
        Type right = node.falseExpr.accept(this);
        if (left.equals(right)) {
            node.exprType = left;
            return node.exprType;
        }
        throw new SemanticException(node.condition.pos, "unmatched type in ternary expression");
    }

    /**
     * VarNameExprNode
     * 根据currentClass判断应该去哪里找
     * 若通过currentClass找，消耗
     *
     * @param node identifier变量名
     * @return node.exprType 变量类型
     */
    @Override
    public Type visit(VarNameExprNode node) {
        if (currentClass == null) {
            node.exprType = currentScope.getType(node.name);
            if (node.exprType == null) {
                throw new SemanticException(node.pos, "variable not defined");
            }
            //非类成员的函数
            if (!(node.exprType instanceof FunctionType)) {
                node.isAssignable = true;
            }
            return node.exprType;
        }
        //通过currentClass找，类成员
        if (currentClass instanceof ClassType) {
            node.exprType = ((ClassType) currentClass).classMembers.get(node.name);
            //类成员变量：访问除 string 外的基本类型 int, bool 的成员变量返回一个实值；
            //访问其他类型成员变量返回引用。
            if (!(node.exprType instanceof IntType || node.exprType instanceof BoolType)) {
                node.isAssignable = true;
            }
        } else {
            if (currentClass instanceof StringType) {
                node.exprType = StringType.members.get(node.name);
            } else if (currentClass instanceof ArrayType) {
                node.exprType = ArrayType.members.get(node.name);
            } else {
                throw new SemanticException(node.pos, "type have no member");
            }
        }
        currentClass = null;
        return node.exprType;
    }

    @Override
    public Type visit(BoolConstantExprNode node) {
        return node.exprType;
    }

    @Override
    public Type visit(IntConstantExprNode node) {
        return node.exprType;
    }

    @Override
    public Type visit(NullConstantExprNode node) {
        return node.exprType;
    }

    @Override
    public Type visit(StrConstantExprNode node) {
        return node.exprType;
    }

    /**
     * ClassDefNode
     * 构建新的类作用域，
     * 作用域中有类的成员标签
     * 结束后跳回上一层
     *
     * @param node 类定义
     * @return null
     */
    @Override
    public Type visit(ClassDefNode node) {
        currentScope = new ClassScope(currentScope,
                (ClassType) Scope.symbolTable.getSymbol(node.name, node.pos));
        node.members.forEach(
                stmt -> stmt.accept(this)
        );
        currentScope = currentScope.getParent();
        return null;
    }

    /**
     * InitNode
     *
     * @param node 变量定义单元的集合
     * @return null
     */
    @Override
    public Type visit(InitNode node) {
        node.varDefUnitNodes.forEach(
                var -> var.accept(this)
        );
        return null;
    }

    /**
     * TypeNode
     *
     * @param node 类型结点
     * @return node.type
     */
    @Override
    public Type visit(TypeNode node) {
        return node.type;
    }

    /**
     * VarDefUnitNode
     * 向当前作用域中加入变量
     * 检查当前作用域中是否会导致重名
     * - 类定义作用域中不会出现要在这一步检查重名的变量
     *
     * @param node 变量定义单元
     * @return null
     */
    @Override
    public Type visit(VarDefUnitNode node) {
        Type varType = node.typeNode.accept(this);
        Type iniType;
        if (node.initExpr != null) {
            iniType = node.initExpr.accept(this);
            if (!varType.equals(iniType)) {
                throw new SemanticException(node.pos, "initiation type not match");
            }
            varType=iniType;
        }
        currentScope.addIdentifier(
                node.name,
                varType,
                node.pos
        );
        return null;
    }
}
