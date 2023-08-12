package midend;

import ast.*;
import ast.expr.*;
import ast.expr.ConstantExprNode.*;
import ast.other.*;
import ast.stmt.*;
import ir.entity.*;
import ir.entity.constant.*;
import ir.*;
import ir.entity.var.*;
import ir.function.Function;
import ir.irType.*;
import ir.stmt.instruction.*;
import ir.stmt.terminal.*;
import utility.*;
import utility.error.InternalException;
import utility.scope.*;
import utility.type.Type;

import java.util.HashMap;
import java.util.Map;


/**
 * @author F
 * 遍历AST构建IR
 * - 维护Scope用来处理重名问题
 * - 最终应该维护好block
 */
public class IRBuilder implements ASTVisitor<Entity> {
    private final IRRoot irRoot;

    //当前作用域
    //IR上不需要新建Scope，直接使用ASTNode存下来的scope
    Scope currentScope = null;
    Function currentFunction = null;
    //计数，确保函数block不重名
    Integer funcBlockCounter = 0;
    //当前的逻辑运算符
    //确保在进入一串逻辑运算前被清空
    LogicExprNode.LogicOperator operator = null;
    //operator更换++（包括null->有）
    //右式为LogicExprNode，++
    //作为label，每次在结点里记下label再访问儿子
    Integer logicExprCounter = 0;
    //label到end块的映射
    HashMap<Integer, BasicBlock> logicBlockMap;
    //当前的类
    StructType currentClass = null;
    //当前块
    BasicBlock currentBlock = null;
    //当前的函数名
    String callFuncName;
    //当前的变量，尤其用于类成员访问
    //应该是指向当前类的指针（内存中的指针类型）
    Storage currentVar = null;
    //全局变量的初始化块；负责变量的空间申请
    public Pair<BasicBlock, BasicBlock> globalInitBlock =
            new Pair<>(new BasicBlock("global_var_def"), new BasicBlock("global_var_init"));

    //当前的函数初始化块,
    private BasicBlock currentInitBlock;

    //变量名 -> <覆盖层次,对应mem空间>
    //变量重命名即为 int+name
    public HashMap<String, Integer> varMap = new HashMap<>();

    //重命名后的name -> Ptr
    public HashMap<String, Ptr> rename2mem = new HashMap<>();

    private String rename(String name) {
        Integer num;
        if (varMap.containsKey(name)) {
            num = varMap.get(name);
            num += 1;
        } else {
            num = 1;
        }
        varMap.put(name, num);
        return num + name;
    }

    //退出当前scope时，将所有新建的变量数目--
    private void exitScope() {
        Integer num;
        for (Map.Entry<String, Type> entry : currentScope.name2type.entrySet()) {
            String key = entry.getKey();
            num = varMap.get(key);
            num -= 1;
            varMap.put(key, num);
        }
        currentScope = currentScope.getParent();
    }

    public IRBuilder(SymbolTable symbolTable) {
        irRoot = new IRRoot(symbolTable);
        irRoot.globalVarDefBlock = globalInitBlock.getFirst();
        irRoot.globalVarInitBlock = globalInitBlock.getSecond();
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
     * @return null
     */
    @Override
    public Entity visit(RootNode node) {
        currentScope = node.scope;
        node.declarations.forEach(
                def -> def.accept(this)
        );
        exitScope();
        return null;
    }

    /**
     * BlockStmtNode
     * 用{}显式表示为作用域块
     * - scope
     * 进入时构建BlockScope
     * （将亲代的funcScope、loopScope加入）
     * 进而访问子结点
     * 退出返回到上层作用域
     * - block
     * 无操作
     *
     * @param node BlockStmtNode
     * @return null
     */
    @Override
    public Entity visit(BlockStmtNode node) {
        currentScope = node.scope;
        node.statements.forEach(
                stmt -> stmt.accept(this)
        );
        exitScope();
        return null;
    }

    /**
     * BreakStmtNode
     * 从LoopScope中找到loop在该函数中的label
     * 无条件跳转到end块
     *
     * @param node BreakStmtNode
     * @return null
     */
    @Override
    public Entity visit(BreakStmtNode node) {
        LoopScope loopScope = currentScope.getParentLoopScope();
        currentBlock.pushBack(
                new Jump(currentFunction.blockMap.get("loop.end" + loopScope.label))
        );
        return null;
    }

    /**
     * ContinueStmtNode
     * 直接跳转到inc
     *
     * @param node ContinueStmtNode
     * @return null
     */
    @Override
    public Entity visit(ContinueStmtNode node) {
        LoopScope loopScope = currentScope.getParentLoopScope();
        if (loopScope.hasInc) {
            currentBlock.pushBack(
                    new Jump(currentFunction.blockMap.get("loop.inc" + loopScope.label))
            );
        } else if (loopScope.hasCond) {
            currentBlock.pushBack(
                    new Jump(currentFunction.blockMap.get("loop.cond" + loopScope.label))
            );
        } else {
            currentBlock.pushBack(
                    new Jump(currentFunction.blockMap.get("loop.body" + loopScope.label))
            );
        }
        return null;
    }

    /**
     * ExprStmtNode
     *
     * @param node ExprStmtNode
     * @return null
     */
    @Override
    public Entity visit(ExprStmtNode node) {
        node.exprList.forEach(expr -> {
            operator = null;
            expr.accept(this);
        });
        return null;
    }

    /**
     * ForStmtNode
     * 进入新LoopScope
     * 先initializationStmt，和当前block同一个
     * loop.cond、loop.body、loop.inc、loop.end四个block依次创建加入func（注意block名）
     * loop.body,loop.end保证存在
     * 中间可能穿插别的block
     *
     * @param node ForStmtNode
     * @return null
     */
    @Override
    public Entity visit(ForStmtNode node) {
        currentScope = node.scope;
        Entity entity = null;
        //作为当前block的特殊标识符
        LoopScope loopScope = (LoopScope) currentScope;
        loopScope.label = funcBlockCounter++;
        //loop的组分
        BasicBlock condBlock = null, incBlock = null;
        BasicBlock bodyBlock = new BasicBlock("loop.body" + loopScope.label),
                endBlock = new BasicBlock("loop.end" + loopScope.label);
        //init加在currentBlock
        if (node.initializationStmt != null) {
            node.initializationStmt.accept(this);
        }
        BasicBlock start;
        //cond若存在，条件跳转，否则直接跳转到body
        if (loopScope.hasCond) {
            condBlock = new BasicBlock("loop.cond" + loopScope.label);
            start = condBlock;
            currentBlock.pushBack(
                    new Jump(condBlock)
            );
            currentBlock = condBlock;
            operator = null;
            entity = node.condition.accept(this);
            currentBlock.pushBack(
                    new Branch(entity, bodyBlock, endBlock)
            );
        } else {
            start = bodyBlock;
            currentBlock.pushBack(
                    new Jump(bodyBlock)
            );
        }
        //body若存在，访问，
        //至少存在跳到下一次循环的语句
        currentBlock = bodyBlock;
        if (node.statement != null) {
            node.statement.accept(this);
        }
        if (loopScope.hasInc) {
            incBlock = new BasicBlock("loop.inc" + loopScope.label);
            currentBlock.pushBack(
                    new Jump(incBlock)
            );
            currentBlock = incBlock;
            operator = null;
            node.step.accept(this);
        }
        currentBlock.pushBack(
                new Jump(start)
        );
        //for循环结束后，当前block为loop.end
        currentBlock = endBlock;
        exitScope();
        return null;
    }

    /**
     * ConstructorDefStmtNode
     * 类的构造函数定义
     * 函数名与类名相同
     * 唯一的参数：this
     * 函数的入口为var_def，var_def跳转到start
     * 返回void
     *
     * @param node ConstructorDefStmtNode
     * @return null
     */
    @Override
    public Entity visit(ConstructorDefStmtNode node) {
        currentScope = node.scope;
        getCurrentFunc(node.name);
        //添加隐含的this参数
        addThisParam();
        currentBlock = new BasicBlock("start");
        currentFunction.blockMap.put("start", currentBlock);
        node.suite.accept(this);
        currentBlock.pushBack(
                new Jump(currentFunction.blockMap.get("return"))
        );
        currentInitBlock.pushBack(
                new Jump(currentFunction.blockMap.get("start"))
        );
        exitScope();
        return null;
    }

    //添加隐含的this参数
    private void addThisParam() {
        LocalVar var = new LocalVar(
                new Storage(new PtrType(currentClass)),
                "this"
        );
        currentFunction.parameterList.add(var);
        rename2mem.put(var.identity, var);
        //构建var_def
        Alloca stmt = new Alloca(var.storage.type, "this.addr");
        currentInitBlock.pushBack(stmt);
        currentInitBlock.pushBack(
                new Store(var, stmt.result)
        );
    }

    /**
     * FuncDefStmtNode
     * 函数定义
     * 新建currentFunc，出函数定义块，置为null
     * - 全局函数 funcName
     * - 自定义类函数 className.funcName
     * 所有函数形成新的作用域
     * 函数的第一个block为所有局部变量的alloca
     * 入参：
     * int和bool类型，函数参数采用值传递；
     * 其他类型，函数参数采用引用传递。（指针指向同一块内存空间）
     * 引用本身采用值传递（如果在函数里改变了指针指向，不影响函数外）
     *
     * @param node FuncDefStmtNode
     * @return null
     */
    @Override
    public Entity visit(FuncDefStmtNode node) {
        //清空所有函数构建中的辅助变量
        funcBlockCounter = 0;
        logicExprCounter = 0;
        logicBlockMap = new HashMap<>();
        Entity entity = node.returnType.accept(this);
        ClassScope classScope;
        //参数复制、局部变量定义
        //全局函数
        if (currentScope instanceof GlobalScope) {
            getCurrentFunc(node.name);
        }
        //类成员函数
        else {
            classScope = (ClassScope) currentScope;
            getCurrentFunc(classScope.classType.name + "." + node.name);
            //参数this
            addThisParam();
        }
        //入参（参数表为VarDefUnitNode数组）
        if (node.parameterList != null) {
            node.parameterList.varDefUnitNodes.forEach(this::addParam);
        }
        //函数作用域
        currentScope = node.scope;
        currentBlock = new BasicBlock("start");
        node.functionBody.accept(this);
        currentInitBlock.pushBack(
                new Jump(currentFunction.blockMap.get("return"))
        );
        currentInitBlock.pushBack(
                new Jump(currentFunction.blockMap.get("start"))
        );
        exitScope();
        return null;
    }

    //添加函数参数
    //不访问结点，直接处理
    private void addParam(VarDefUnitNode node) {
        Entity entity = node.typeNode.accept(this);
        //构造局部变量，加入参数表
        LocalVar var = new LocalVar(
                new Storage(entity.type),
                node.name
        );
        currentFunction.parameterList.add(var);
        rename2mem.put(var.identity, var);
        //构建var_def
        Alloca stmt = new Alloca(var.storage.type, rename(node.name));
        currentInitBlock.pushBack(stmt);
        currentInitBlock.pushBack(
                new Store(var, stmt.result)
        );
    }

    /**
     * 根据函数名取出函数
     * 构建变量定义块，将其作为currentInitBlock
     *
     * @param funcName 转化过后的函数名
     */
    private void getCurrentFunc(String funcName) {
        currentFunction = irRoot.getFunc(funcName);
        currentInitBlock = new BasicBlock("var_def");
        //进入函数的第一个块为变量、参数初始化
        currentFunction.entry = currentInitBlock;
        //如果有返回值，先给retVal分配空间
        if (!(currentFunction.retType instanceof VoidType)) {
            Alloca stmt = new Alloca(currentFunction.retType, "retVal");
            currentInitBlock.pushBack(stmt);
            currentFunction.retVal = stmt.result;
            //main有缺省的返回值
            if ("main".equals(funcName)) {
                currentInitBlock.pushBack(
                        new Store(new ConstInt("0"), stmt.result)
                );
            }
        }
    }

    /**
     * IfStmtNode
     * 条件跳转
     * if.then,if.else,if.end
     * 可能没有else
     *
     * @param node IfStmtNode
     * @return null
     */
    @Override
    public Entity visit(IfStmtNode node) {
        int label = funcBlockCounter++;
        BasicBlock trueStmtBlock = new BasicBlock("if.then" + label);
        BasicBlock falseStmtBlock = null;
        BasicBlock endBlock = new BasicBlock("if.end" + label);
        BasicBlock next = endBlock;
        if (node.falseStatement != null) {
            falseStmtBlock = new BasicBlock("if.else" + label);
            next = falseStmtBlock;
        }
        //cond：在上一个块里
        operator = null;
        Entity entity = node.condition.accept(this);
        currentBlock.pushBack(
                new Branch(entity, trueStmtBlock, next)
        );
        currentBlock = trueStmtBlock;
        node.trueStatement.accept(this);
        currentBlock.pushBack(
                new Jump(endBlock)
        );
        if (node.falseStatement != null) {
            currentBlock = falseStmtBlock;
            node.falseStatement.accept(this);
            currentBlock.pushBack(
                    new Jump(endBlock)
            );
        }
        currentBlock = endBlock;
        return null;
    }

    /**
     * ReturnStmtNode
     * 函数的return
     * - 有无返回值
     * //TODO：优化，不借助retVal、提前退出函数
     * 初步处理方式
     * 如果有返回值，进入函数时分配retVal空间，
     * return语句给retVal赋值，br return
     * -----------------------------------
     * store i32 1, ptr %retval, align 4
     * br label %return
     * -----------------------------------
     *
     * @param node ReturnStmtNode
     * @return null
     */
    @Override
    public Entity visit(ReturnStmtNode node) {
        //带有返回值
        if (node.expression != null) {
            operator = null;
            Entity val = node.expression.accept(this);
            currentBlock.pushBack(
                    new Store(val, currentFunction.retVal)
            );
        }
        currentBlock.pushBack(
                new Jump(currentFunction.ret)
        );
        return null;
    }

    /**
     * VarDefStmtNode
     * 整句的变量定义
     * 访问各个单个的变量定义
     *
     * @param node VarDefStmtNode
     * @return null
     */
    @Override
    public Entity visit(VarDefStmtNode node) {
        node.varDefUnitNodes.forEach(
                var -> var.accept(this)
        );
        return null;
    }

    /**
     * WhileStmtNode
     * while循环作用域
     * loop.cond,loop.body,loop.end
     *
     * @param node WhileStmtNode
     * @return null
     */
    @Override
    public Entity visit(WhileStmtNode node) {
        currentScope = node.scope;
        Entity entity = null;
        //作为当前block的特殊标识符
        LoopScope loopScope = (LoopScope) currentScope;
        loopScope.label = funcBlockCounter++;
        BasicBlock condBlock = new BasicBlock("loop.cond" + loopScope.label);
        BasicBlock bodyBlock = new BasicBlock("loop.body" + loopScope.label);
        BasicBlock endBlock = new BasicBlock("loop.end" + loopScope.label);
        currentBlock.pushBack(
                new Jump(condBlock)
        );
        currentBlock = condBlock;
        operator = null;
        entity = node.condition.accept(this);
        currentBlock.pushBack(
                new Branch(entity, bodyBlock, endBlock)
        );
        currentBlock = bodyBlock;
        if (node.statement != null) {
            node.statement.accept(this);
        }
        currentBlock.pushBack(
                new Jump(condBlock)
        );
        currentBlock = endBlock;
        return null;
    }

    /**
     * ArrayVisExprNode
     * 数组下标访问，层层嵌套
     * 对每一层[]调用getelementptr
     *
     * @param node ArrayVisExprNode
     * @return result
     */
    @Override
    public Entity visit(ArrayVisExprNode node) {
        //取出数组名
        Entity array = node.arrayName.accept(this);
        LocalTmpVar arrayName;
        if (array instanceof Ptr ptr) {
            arrayName = new LocalTmpVar(ptr.storage.type);
            currentBlock.pushBack(
                    new Load(arrayName, ptr)
            );
        } else if (array instanceof LocalTmpVar) {
            arrayName = (LocalTmpVar) array;
        } else {
            throw new InternalException("unexpected array name");
        }
        //一一访问下标，层层解析
        node.indexList.forEach(
                indexExpr -> {
                    Entity entity = indexExpr.accept(this);

                    if (entity instanceof Constant) {

                    }
                }
        );
    }

    /**
     * AssignExprNode
     * 赋值语句，左边为IR指针类
     * 如果右边为Ptr，先load
     * +----------------------------
     * |    %0 = load i32, ptr %a, align 4
     * |    store i32 %0, ptr %b,
     * +---------------------------
     *
     * @param node AssignExprNode
     * @return null
     */
    @Override
    public Entity visit(AssignExprNode node) {
        Entity left = node.lhs.accept(this);
        operator = null;
        Entity right = node.rhs.accept(this);
        if (right instanceof Constant) {
            currentBlock.pushBack(
                    new Store(right, left)
            );
            return null;
        }
        Storage tmp = getValue(right);
        currentBlock.pushBack(
                new Store(tmp, left)
        );
        return null;
    }

    /**
     * BinaryExprNode
     * 二元算数计算
     * 转化为binary指令
     * 字面量\localTmpVar直接运算，
     * 变量先load到localTmpVar  | %0 = load i32, ptr |
     * 运算结果放到localTmpVar
     *
     * @param node BinaryExprNode
     * @return result(localTmpVar)
     */
    @Override
    public Entity visit(BinaryExprNode node) {
        Entity left = node.lhs.accept(this);
        Entity right = node.rhs.accept(this);
        Storage lhs = getValue(left), rhs = getValue(right);
        //IR运算语句
        LocalTmpVar result = new LocalTmpVar(lhs.type);
        currentBlock.pushBack(
                new Binary(node.operator,
                        result,
                        lhs,
                        rhs
                )
        );
        return result;
    }

    /**
     * CmpExprNode
     * 二元比较表达式
     * 转化为icmp指令
     * 字面量\localTmpVar直接运算，
     * 变量先load到localTmpVar  | %0 = load i32, ptr |
     * 运算结果放到localTmpVar
     *
     * @param node CmpExprNode
     * @return result
     */
    @Override
    public Entity visit(CmpExprNode node) {
        operator = null;
        Entity left = node.lhs.accept(this);
        operator = null;
        Entity right = node.rhs.accept(this);
        Storage lhs = getValue(left), rhs = getValue(right);
        //IR比较语句
        LocalTmpVar result = new LocalTmpVar(new IntType(IntType.TypeName.BOOL));
        currentBlock.pushBack(
                new Icmp(node.operator,
                        result,
                        lhs,
                        rhs
                )
        );
        return result;
    }

    private Storage getValue(Entity entity) {
        LocalTmpVar tmp;
        if (entity instanceof Ptr) {
            tmp = new LocalTmpVar(((Ptr) entity).storage.type);
            currentBlock.pushBack(
                    new Load(tmp, entity)
            );
            return tmp;
        } else {
            return (Storage) entity;
        }
    }

    /**
     * FuncCallExprNode
     * 函数调用
     * 先访问函数名结点，将名称转化后存到builder的私有成员中
     *
     * @param node FuncCallExprNode
     * @return result
     */
    @Override
    public Entity visit(FuncCallExprNode node) {
        //得到函数名
        node.func.accept(this);
        Function function;
        LocalTmpVar result;
        Call stmt;
        //类的成员函数
        if (currentVar != null) {
            StructType classType = (StructType) ((PtrType) currentVar.type).type;
            function = irRoot.getFunc(classType.name + "." + callFuncName);
            result = new LocalTmpVar(function.retType);
            stmt = new Call(function, result);
            //第一个参数为this
            stmt.parameterList.add(currentVar);
            currentVar = null;
        }
        //普通函数
        else {
            function = irRoot.getFunc(callFuncName);
            result = new LocalTmpVar(function.retType);
            stmt = new Call(function, result);
        }
        node.parameterList.forEach(
                parameter -> stmt.parameterList.add(
                        (Storage) parameter.accept(this)
                ));
        currentBlock.pushBack(stmt);
        return result;
    }

    /**
     * LogicExprNode
     * 逻辑运算语句
     * 需要支持短路求值（跳转语句）
     * 使用phi指令，提前退出，都退到end
     * （phi仅使用两个label，如果提前退出，都为同一个结果，使用一个虚拟label表示）
     * - 只有根有end、返回result
     *
     * @param node LogicExprNode
     * @return result\null
     */
    @Override
    public Entity visit(LogicExprNode node) {
        //换符号，换根
        if (!node.operator.equals(operator)) {
            ++logicExprCounter;
            operator = node.operator;
        }
        int label = logicExprCounter;
        int exprLabel = funcBlockCounter++;//子跳转
        //用到的块
        BasicBlock endBlock;
        boolean rootFlag = false;//表示为连续逻辑计算的根结点
        if (logicBlockMap.containsKey(label)) {
            endBlock = logicBlockMap.get(label);
        } else {
            endBlock = new BasicBlock("logic.end" + label);
            logicBlockMap.put(label, endBlock);
            rootFlag = true;
        }
        BasicBlock nextBlock = new BasicBlock("logic.next" + exprLabel);
        Entity entity = node.lhs.accept(this);
        if (entity != null) {
            LocalTmpVar leftToBool = toBool(entity);
            //结束当前块，跳转
            if (node.operator.equals(LogicExprNode.LogicOperator.AndAnd)) {
                currentBlock.pushBack(
                        new Branch(leftToBool, nextBlock, endBlock)
                );
            } else {
                currentBlock.pushBack(
                        new Branch(leftToBool, endBlock, nextBlock)
                );
            }
            currentBlock = nextBlock;
        }
        //右儿子
        //如果是LogicExprNode，计数
        if (node.rhs instanceof LogicExprNode) {
            ++logicExprCounter;
        }
        LocalTmpVar rightToBool = toBool(node.rhs.accept(this));
        if (rootFlag) {//当前为根
            currentBlock.pushBack(
                    new Jump(endBlock)
            );
            String str = currentBlock.label;
            currentBlock = endBlock;
            LocalTmpVar result = new LocalTmpVar(new IntType(IntType.TypeName.TMP_BOOL));
            if (node.operator.equals(LogicExprNode.LogicOperator.AndAnd)) {
                currentBlock.pushBack(
                        new Phi(result,
                                new ConstBool(true), rightToBool,
                                "virtual_block", str)
                );
            } else {
                currentBlock.pushBack(
                        new Phi(result,
                                new ConstBool(false), rightToBool,
                                "virtual_block", str)
                );
            }
            return result;
        } else {//当前非根
            exprLabel = funcBlockCounter++;
            nextBlock = new BasicBlock("logic.next" + exprLabel);
            if (node.operator.equals(LogicExprNode.LogicOperator.AndAnd)) {
                currentBlock.pushBack(
                        new Branch(rightToBool, nextBlock, endBlock)
                );
            } else {
                currentBlock.pushBack(
                        new Branch(rightToBool, endBlock, nextBlock)
                );
            }
            currentBlock = nextBlock;
            return null;
        }
    }

    private LocalTmpVar toBool(Entity entity) {
        LocalTmpVar tmp, toBool;
        if (entity instanceof Ptr) {
            tmp = new LocalTmpVar(((Ptr) entity).storage.type);
            currentBlock.pushBack(
                    new Load(tmp, entity)
            );
        } else {
            tmp = (LocalTmpVar) entity;
        }
        if (tmp.type.equals(new IntType(IntType.TypeName.BOOL))) {
            toBool = new LocalTmpVar(new IntType(IntType.TypeName.TMP_BOOL));
            currentBlock.pushBack(
                    new Trunc(toBool, tmp)
            );
        } else {
            toBool = tmp;
        }
        return toBool;
    }

    /**
     * LogicPrefixExprNode
     * 前缀逻辑运算
     * ---------------------------------------------
     * %0 = load i8, ptr %a, align 1
     * %tobool = trunc i8 %0 to i1
     * %lnot = xor i1 %tobool, true
     * -----------------------------------------------
     *
     * @param node LogicPrefixExprNode
     * @return result(TMP_BOOL)
     */
    @Override
    public Entity visit(LogicPrefixExprNode node) {
        operator = null;
        Entity entity = node.expression.accept(this);
        LocalTmpVar result = new LocalTmpVar(new IntType(IntType.TypeName.TMP_BOOL));
        if (entity instanceof Constant) {//常量
            currentBlock.pushBack(
                    new Binary(
                            BinaryExprNode.BinaryOperator.Xor,
                            result, entity, new ConstBool(true)
                    )
            );
            return result;
        }
        Storage tmp = getValue(entity);
        LocalTmpVar toBool = new LocalTmpVar(new IntType(IntType.TypeName.TMP_BOOL));
        currentBlock.pushBack(
                new Trunc(toBool, tmp)
        );
        currentBlock.pushBack(
                new Binary(
                        BinaryExprNode.BinaryOperator.Xor,
                        result, toBool, new ConstBool(true)
                )
        );
        return result;
    }

    /**
     * MemberVisExprNode
     * 类成员访问
     *
     * @param node MemberVisExprNode
     * @return result
     */
    @Override
    public Entity visit(MemberVisExprNode node) {
        //左式，取值结果为指向class的指针
        currentVar = getValue(node.lhs.accept(this));
        return node.rhs.accept(this);
    }

    @Override
    public Entity visit(NewExprNode node) {

    }

    /**
     * PrefixExprNode
     * 前缀++ --，返回自增自减后的值
     * -取负数
     * ~按位取反
     *
     * @param node PrefixExprNode
     * @return result
     */
    @Override
    public Entity visit(PrefixExprNode node) {
        Entity entity = node.expression.accept(this);
        LocalTmpVar result = new LocalTmpVar(entity.type);
        //++\--需要存原始值作为返回值
        if (node.operator == PrefixExprNode.PrefixOperator.PlusPlus ||
                node.operator == PrefixExprNode.PrefixOperator.MinusMinus) {
            Storage tmp = getValue(entity);
            Entity right = new ConstInt("1");
            BinaryExprNode.BinaryOperator operator =
                    node.operator == PrefixExprNode.PrefixOperator.PlusPlus ?
                            BinaryExprNode.BinaryOperator.Plus :
                            BinaryExprNode.BinaryOperator.Minus;
            //运算
            currentBlock.pushBack(
                    new Binary(operator, result, tmp, right)
            );
            //赋值给a
            currentBlock.pushBack(
                    new Store(result, entity)
            );
            return result;
        }
        Storage value = getValue(entity);
        //-取相反数
        if (node.operator == PrefixExprNode.PrefixOperator.Minus) {
            Entity left = new ConstInt("0");
            currentBlock.pushBack(
                    new Binary(BinaryExprNode.BinaryOperator.Minus,
                            result, left, value)
            );
            return result;
        }
        //按位取反
        Entity right = new ConstInt("-1");
        currentBlock.pushBack(
                new Binary(BinaryExprNode.BinaryOperator.Xor,
                        result, value, right)
        );
        return result;
    }

    /**
     * PointerExprNode
     * this指针
     * 仅出现在类成员函数中
     * 函数有一个变量this.addr
     * 返回指向当前类型的指针
     *
     * @param node PointerExprNode
     * @return result
     */
    @Override
    public Entity visit(PointerExprNode node) {
        Ptr ptr = rename2mem.get("this.addr");
        //指向当前类的指针
        LocalTmpVar result = new LocalTmpVar(new PtrType(currentClass));
        currentBlock.pushBack(
                new Load(result, ptr)
        );
        return result;
    }

    /**
     * SuffixExprNode
     * 返回原先值
     *
     * @param node SuffixExprNode
     * @return tmp
     */
    @Override
    public Entity visit(SuffixExprNode node) {
        Entity entity = node.expression.accept(this);
        LocalTmpVar result = new LocalTmpVar(entity.type);
        LocalTmpVar tmp;
        if (entity instanceof Ptr) {
            tmp = new LocalTmpVar(entity.type);
            currentBlock.pushBack(
                    new Load(tmp, entity)
            );
        } else {
            tmp = (LocalTmpVar) entity;
        }
        Entity right = new ConstInt("1");
        BinaryExprNode.BinaryOperator operator =
                node.operator == SuffixExprNode.SuffixOperator.PlusPlus ?
                        BinaryExprNode.BinaryOperator.Plus :
                        BinaryExprNode.BinaryOperator.Minus;
        currentBlock.pushBack(
                new Binary(operator, result, tmp, result)
        );
        currentBlock.pushBack(
                new Store(result, entity)
        );
        return tmp;
    }

    /**
     * TernaryExprNode
     * 三目运算表达式
     * 类似于if
     * cond.true,cond.false,cond.end
     *
     * @param node TernaryExprNode
     * @return result
     */
    @Override
    public Entity visit(TernaryExprNode node) {
        int label = funcBlockCounter++;
        BasicBlock trueStmtBlock = new BasicBlock("cond.true" + label);
        BasicBlock falseStmtBlock = new BasicBlock("cond.false" + label);
        BasicBlock endBlock = new BasicBlock("cond.end" + label);
        //cond：在上一个块里
        operator = null;
        Entity entity = node.condition.accept(this);
        currentBlock.pushBack(
                new Branch(entity, trueStmtBlock, falseStmtBlock)
        );
        currentBlock = trueStmtBlock;
        operator = null;
        Storage trueAns = (Storage) node.trueExpr.accept(this);
        currentBlock.pushBack(
                new Jump(endBlock)
        );
        currentBlock = falseStmtBlock;
        operator = null;
        Storage falseAns = (Storage) node.falseExpr.accept(this);
        currentBlock.pushBack(
                new Jump(endBlock)
        );
        currentBlock = endBlock;
        LocalTmpVar result = new LocalTmpVar(trueAns.type);
        currentBlock.pushBack(
                new Phi(result, trueAns, falseAns,
                        "cond.true" + label, "cond.false" + label)
        );
        return result;
    }

    /**
     * VarNameExprNode
     * 变量名localPtr、globalPtr
     * 从rename2mem找到重命名后的变量ptr返回
     * 不可以返回storage（可能是赋值语句的left）
     * TODO:类成员、函数
     *
     * @param node VarNameExprNode
     * @return ptr
     */
    @Override
    public Entity visit(VarNameExprNode node) {
        //变量名
        if (varMap.containsKey(node.name)) {
            //该变量在当前的重命名
            String name = varMap.get(node.name) + node.name;
            return rename2mem.get(name);
        }
        //类的成员变量名
        //非下标访问（类内）
        //TODO:类成员的下标访问


    }

    /**
     * BoolConstantExprNode
     * bool类型的常量
     *
     * @param node BoolConstantExprNode
     * @return constBool
     */
    @Override
    public Entity visit(BoolConstantExprNode node) {
        return new ConstBool(((BoolConstantExprNode) node).value);
    }

    /**
     * IntConstantExprNode
     * 整型常量
     *
     * @param node IntConstantExprNode
     * @return constInt
     */
    @Override
    public Entity visit(IntConstantExprNode node) {
        return new ConstInt(((IntConstantExprNode) node).value);
    }

    /**
     * NullConstantExprNode
     * 空指针
     *
     * @param node NullConstantExprNode
     * @return Null
     */
    @Override
    public Entity visit(NullConstantExprNode node) {
        return new Null();
    }

    /**
     * StrConstantExprNode
     *
     * @param node StrConstantExprNode
     * @return constString
     */
    @Override
    public Entity visit(StrConstantExprNode node) {
        return new ConstString(((StrConstantExprNode) node).value);
    }

    /**
     * ClassDefNode
     * 类定义
     * 进入类的作用域
     * TODO：进入类定义时构建构造函数，为类分配空间（应该需要考虑调用内建函数？）
     *
     * @param node ClassDefNode
     * @return null
     */
    @Override
    public Entity visit(ClassDefNode node) {
        currentScope = node.scope;
        currentClass = (StructType) irRoot.types.get(node.name);
        boolean hasConstructor=false;
        StmtNode stmt;
        for (int i=0;i<node.members.size();++i){
            stmt=node.members.get(i);
            if (stmt instanceof FuncDefStmtNode) {
                stmt.accept(this);
            } else if (stmt instanceof ConstructorDefStmtNode){
                stmt.accept(this);
                hasConstructor=true;
            }
        }
        //如果没有构造函数，加入默认的构造函数
        if(!hasConstructor){
            getCurrentFunc(node.name);
            //添加隐含的this参数
            addThisParam();
            //直接返回
            currentInitBlock.pushBack(
                    new Jump(currentFunction.blockMap.get("return"))
            );
        }
        currentClass = null;
        exitScope();
        return null;
    }

    @Override
    public Entity visit(InitNode node) {
        node.varDefUnitNodes.forEach(
                var -> var.accept(this)
        );
        return null;
    }

    /**
     * TypeNode
     * 处理AST上的type
     * 转化为IR上的type的空值常量
     *
     * @param node TypeNode
     * @return constant/
     */
    @Override
    public Entity visit(TypeNode node) {
        if (node.type instanceof utility.type.IntType) {
            return new ConstInt("0");
        }
        if (node.type instanceof utility.type.BoolType) {
            return new ConstBool(true);
        }
        if (node.type instanceof utility.type.NullType) {
            return new Null();
        }
        if (node.type instanceof utility.type.StringType) {
            return new ConstString("");
        }
        if (node.type instanceof utility.type.ArrayType arrayType) {
            return new Storage(
                    new ArrayType(irRoot.type2irType(arrayType.eleType),
                            arrayType.dimensions)
            );
        }
        if (node.type instanceof utility.type.ClassType classType) {
            return new Storage(irRoot.types.get(classType.name));
        } else {
            throw new InternalException("unexpected type in typeNode");
        }
    }

    /**
     * VarDefUnitNode变量定义
     * 开空间+可能有初始化,
     * - 全局变量，加到IRRoot下
     * - 局部变量:若被常量初始化，加入init
     * （类成员已经在ipRoot中收集，且类成员默认初始化表达式为未定义行为）
     *
     * @param node VarDefUnitNode
     * @return null
     */
    @Override
    public Entity visit(VarDefUnitNode node) {
        String name;
        //变量类型
        IRType irType = node.typeNode.accept(this).type;
        Entity entity = null;
        //global var
        if (currentScope instanceof GlobalScope) {
            name = rename(node.name);
            //标明类型的空间，无初始值
            Storage initVar = new Storage(irType);
            //have init?
            if (node.initExpr != null) {
                //如果为字面量，返回constant,直接初始化
                //否则返回在初始化函数中用赋值语句初始化
                operator = null;
                entity = node.initExpr.accept(this);
                if (entity instanceof Constant) {
                    initVar = (Storage) entity;
                }
            }
            //调用Global指令为全局变量分配空间
            Global stmt = new Global(initVar, name);
            globalInitBlock.getFirst().pushBack(stmt);
            //将rename -> mem映射存入map
            rename2mem.put(name, stmt.result);
            //非字面量初始化
            //在全局的初始化函数中加入赋值语句
            if (entity != null && !(entity instanceof Constant)) {
                globalInitBlock.getSecond().pushBack(
                        new Store(entity, stmt.result)
                );
            }
        }
        //局部变量
        else {
            name = rename(node.name);
            //普通局部变量，Alloca分配空间
            Alloca stmt = new Alloca(irType, name);
            currentInitBlock.pushBack(stmt);
            //将rename -> mem映射存入map
            rename2mem.put(name, stmt.result);
            //若有初始化语句，在走到该部分时用赋值语句
            if (node.initExpr != null) {
                operator = null;
                node.initExpr.accept(this);
            }
        }
        return null;
    }
}
