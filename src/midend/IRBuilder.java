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
import ir.function.GlobalVarInitFunction;
import ir.function.MainFunc;
import ir.irType.*;
import ir.stmt.Stmt;
import ir.stmt.instruction.*;
import ir.stmt.terminal.*;
import utility.*;
import utility.error.InternalException;
import utility.scope.*;
import ast.type.Type;

import java.util.*;


/**
 * @author F
 * 遍历AST构建IR
 * - 维护Scope用来处理重名问题
 * - 最终应该维护好block
 */
public class IRBuilder implements ASTVisitor<Entity> {
    public IRRoot irRoot;

    //当前作用域
    //IR上不需要新建Scope，直接使用ASTNode存下来的scope
    Scope currentScope = null;
    IRType intType = new IntType(IntType.TypeName.INT);
    IRType boolType = new IntType(IntType.TypeName.BOOL);
    IRType tmpBoolType = new IntType(IntType.TypeName.TMP_BOOL);
    Constant zero = new ConstInt("0");
    ConstBool boolTrue = new ConstBool(true);
    ConstBool boolFalse = new ConstBool(false);
    Function currentFunction = null;
    Function malloc;
    Function malloc_array;
    Counter tmpCounter;
    Counter retCounter;
    Counter phiCounter;
    //计数，确保函数block不重名
    Integer funcBlockCounter = 0;
    //当前的逻辑运算符
    //确保在进入一串逻辑运算前被清空
    LogicExprNode.LogicOperator operator = null;
    //operator更换++（包括null->有）
    //右式为LogicExprNode，++
    //作为label，每次在结点里记下label再访问儿子
    Integer logicExprCounter = 0;
    ArrayList<String> logicLabelList = new ArrayList<>();
    //label到end块的映射
    HashMap<Integer, BasicBlock> logicBlockMap;
    //当前的类
    StructType currentClass = null;
    //当前块
    BasicBlock currentBlock = null;
    boolean terminated = false;
    //当前的函数名
    String callFuncName;
    //当前的变量，尤其用于类成员访问
    //特殊处理内置类array、string
    //应该是指向当前类的指针（内存中的指针类型）
    //非成员访问：zero作为virtual value
    Stack<Storage> currentVar = new Stack<>();
    boolean getFuncName = false;
    boolean getMemberVar = false;
    boolean getMemberFunc = false;
    //类内直接访问成员函数
    boolean addThis = false;
    //全局变量的初始化块；负责变量的空间申请
    BasicBlock globalVarDefBlock = new BasicBlock("_global_var_def");
    GlobalVarInitFunction globalInitFunc = new GlobalVarInitFunction();
    //字符串字面量，重复的仅在全局定义一次
    HashMap<String, GlobalVar> constStringMap = new HashMap<>();
    int constStrCounter = -1;
    //当前的函数初始化块,
    private LinkedList<Stmt> currentInitStmts;

    //变量名 -> <覆盖层次,对应mem空间>
    //变量重命名即为 int+name
    public HashMap<
            String,//真名
            Pair<Integer, Stack<Integer>>//重名次数；当前名
            > varMap = new HashMap<>();

    //重命名后的name -> Ptr
    public HashMap<String, Ptr> rename2mem = new HashMap<>();

    private String rename(String name) {
        Integer num;
        Pair<Integer, Stack<Integer>> pair;
        if (varMap.containsKey(name)) {
            pair = varMap.get(name);
            num = pair.getFirst();
            num += 1;
            pair.getSecond().push(num);
        } else {
            Stack<Integer> stack = new Stack<>();
            stack.push(1);
            pair = new Pair<>(1, stack);
            num = 1;
        }
        pair.setFirst(num);
        varMap.put(name, pair);
        return name + "." + num;
    }

    private void changeBlock(BasicBlock block) {
        currentBlock = block;
        if (currentScope instanceof GlobalScope) {
            globalInitFunc.currentBlock = currentBlock;
        }
        terminated = false;
    }

    private void enterScope(ASTNode node) {
        currentScope = node.scope;
        if (node.scope.getParent() != null) {
            node.scope.terminated = node.scope.getParent().terminated;
        }
    }

    private void enterScope(IfStmtNode node, boolean isFalseScope) {
        currentScope = node.falseScope;
    }

    //退出当前scope时，将所有新建的变量数目--
    //类的成员不作为新建变量
    private void exitScope() {
        if (currentScope instanceof ClassScope) {
            currentScope = currentScope.getParent();
            return;
        }
        for (Map.Entry<String, Type> entry : currentScope.name2type.entrySet()) {
            String key = entry.getKey();
            Pair<Integer, Stack<Integer>> pair = varMap.get(key);
            pair.getSecond().pop();
//            pair.setFirst(pair.getFirst() - 1);
            varMap.put(key, pair);
        }
        //就终结符来看，函数作用域同函数体作用域
        if (currentScope.getParent() instanceof FuncScope) {
            currentScope.getParent().terminated = currentScope.terminated;
        }
        currentScope = currentScope.getParent();
    }

    //作用域有提前终止符，该作用域后面的语句无用
    private void pushBack(Stmt stmt) {
        currentBlock.pushBack(stmt);
    }

    public IRBuilder(SymbolTable symbolTable) {
        irRoot = new IRRoot(symbolTable);
        irRoot.globalVarDefBlock = globalVarDefBlock;
        irRoot.globalVarInitFunction = globalInitFunc;
        malloc = irRoot.getFunc("_malloc");
        malloc_array = irRoot.getFunc("_malloc_array");
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
        enterScope(node);
        tmpCounter = globalInitFunc.tmpCounter;
        phiCounter = globalInitFunc.phiCounter;
        node.declarations.forEach(
                def -> def.accept(this)
        );
        exitScope();
        // 直接将globalVarInitFunction内联入main
        irRoot.mainFunc.allocaBlock = irRoot.mainFunc.entry;
        if (!irRoot.globalVarInitFunction.isEmpty()) {
            irRoot.globalVarInitFunction.currentBlock.pushBack(
                    new Jump(irRoot.mainFunc.entry.label)
            );
            //重命名globalVarInitFunction的basic block
            LinkedHashMap<String, BasicBlock> newBlockMap = new LinkedHashMap<>();
            newBlockMap.putAll(irRoot.globalVarInitFunction.blockMap);
            newBlockMap.putAll(irRoot.mainFunc.blockMap);
            irRoot.mainFunc.blockMap = newBlockMap;
            irRoot.mainFunc.entry = irRoot.globalVarInitFunction.entry;
        }
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
        enterScope(node);
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
        pushBack(
                new Jump(currentFunction.funcName + "_loop.end" + loopScope.label)
        );
        terminated = true;
        currentScope.terminated = true;
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
            pushBack(
                    new Jump(currentFunction.funcName + "_loop.inc" + loopScope.label)
            );
        } else if (loopScope.hasCond) {
            pushBack(
                    new Jump(currentFunction.funcName + "_loop.cond" + loopScope.label)
            );
        } else {
            pushBack(
                    new Jump(currentFunction.funcName + "_loop.body" + loopScope.label)
            );
        }
        terminated = true;
        currentScope.terminated = true;
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
        enterScope(node);
        Entity entity;
        //作为当前block的特殊标识符
        LoopScope loopScope = (LoopScope) currentScope;
        loopScope.label = funcBlockCounter++;
        //loop的组分
        BasicBlock condBlock, incBlock;
        BasicBlock bodyBlock = new BasicBlock(currentFunction.funcName + "_loop.body" + loopScope.label),
                endBlock = new BasicBlock(currentFunction.funcName + "_loop.end" + loopScope.label);
        //init加在currentBlock
        if (node.initializationStmt != null) {
            node.initializationStmt.accept(this);
        }
        BasicBlock start;
        //cond若存在，条件跳转，否则直接跳转到body
        if (loopScope.hasCond) {
            condBlock = new BasicBlock(currentFunction.funcName + "_loop.cond" + loopScope.label);
            start = condBlock;
            pushBack(
                    new Jump(condBlock)
            );
            changeBlock(condBlock);
            currentFunction.blockMap.put(currentBlock.label, currentBlock);
            operator = null;
            entity = toBool(getValue(node.condition.accept(this)));
            pushBack(
                    new Branch(entity, bodyBlock, endBlock)
            );
        } else {
            start = bodyBlock;
            pushBack(
                    new Jump(bodyBlock)
            );
        }
        //body若存在，访问，
        //至少存在跳到下一次循环的语句
        changeBlock(bodyBlock);
        currentFunction.blockMap.put(currentBlock.label, currentBlock);
        if (node.statement != null) {
            node.statement.accept(this);
        }
        if (loopScope.hasInc) {
            incBlock = new BasicBlock(currentFunction.funcName + "_loop.inc" + loopScope.label);
            pushBack(
                    new Jump(incBlock)
            );
            changeBlock(incBlock);
            currentFunction.blockMap.put(currentBlock.label, currentBlock);
            operator = null;
            node.step.accept(this);
        }
        pushBack(
                new Jump(start)
        );
        //for循环结束后，当前block为loop.end
        changeBlock(endBlock);
        currentFunction.blockMap.put(currentBlock.label, currentBlock);
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
     * TODO:构造函数新建变量和类成员变量（可以同名）
     *
     * @param node ConstructorDefStmtNode
     * @return null
     */
    @Override
    public Entity visit(ConstructorDefStmtNode node) {
        //清空所有函数构建中的辅助变量
        funcBlockCounter = 0;
        logicExprCounter = 0;
        tmpCounter = new Counter();
        phiCounter = new Counter();
        retCounter = new Counter();
        logicBlockMap = new HashMap<>();
        enterScope(node);
        getCurrentFunc(node.name);
        //添加隐含的this参数
        addThisParam();
        changeBlock(new BasicBlock(currentFunction.funcName + "_start"));
        currentFunction.entry = currentBlock;
        currentFunction.blockMap.put(currentBlock.label, currentBlock);
        node.suite.accept(this);
        if (currentBlock.tailStmt == null) {
            pushBack(
                    new Jump(currentFunction.ret)
            );
        }
        currentInitStmts.addAll(currentFunction.entry.statements);
        currentFunction.entry.statements = currentInitStmts;
        currentFunction.ret.pushBack(
                new Return()
        );
        exitScope();
        return null;
    }

    //添加函数参数
    //不访问结点，直接处理
    private void addParam(VarDefUnitNode node) {
        Entity entity = node.typeNode.accept(this);
        String name = rename(node.name);
        //构造局部变量，加入参数表(参数表中的)
        LocalTmpVar var = new LocalTmpVar(entity.type, node.name);
        currentFunction.parameterList.add(var);
        //构建var_def
        Alloca stmt = new Alloca(entity.type, name);
        currentInitStmts.add(stmt);
        currentInitStmts.add(
                new Store(var, stmt.result)
        );
        rename2mem.put(name, stmt.result);
    }

    //添加隐含的this参数
    private void addThisParam() {
        StructPtrType type = new StructPtrType(currentClass);
        LocalTmpVar var = new LocalTmpVar(type, currentFunction.funcName + "_this");
        currentFunction.parameterList.add(var);
        //构建var_def
        Alloca stmt = new Alloca(type, currentFunction.funcName +"_this1");
        currentInitStmts.add(stmt);
        currentInitStmts.add(
                new Store(var, stmt.result)
        );
        rename2mem.put("this1", stmt.result);
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
        tmpCounter = new Counter();
        phiCounter = new Counter();
        retCounter = new Counter();
        logicBlockMap = new HashMap<>();
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
        enterScope(node);
        changeBlock(new BasicBlock(currentFunction.funcName + "_start"));
        currentFunction.entry = currentBlock;
        currentFunction.blockMap.put(currentBlock.label, currentBlock);
        node.functionBody.accept(this);
        if (currentBlock.tailStmt == null) {
            pushBack(
                    new Jump(currentFunction.ret)
            );
        }
        if (currentFunction.retType instanceof VoidType) {
            currentFunction.ret.pushBack(
                    new Return()
            );
        } else {
            //labeled as useless
            LocalTmpVar tmp = new LocalTmpVar(currentFunction.retType, ++tmpCounter.cnt, currentFunction.funcName);
            currentFunction.ret.pushBack(
                    new Load(tmp, currentFunction.retVal)
            );
            currentFunction.ret.pushBack(
                    new Return(tmp)
            );
        }
        //合并currentInitStmts和_start
        currentInitStmts.addAll(currentFunction.entry.statements);
        currentFunction.entry.statements = currentInitStmts;
        exitScope();
        tmpCounter = globalInitFunc.tmpCounter;
        phiCounter = globalInitFunc.tmpCounter;
        return null;
    }

    /**
     * 根据函数名取出函数
     * 构建变量定义list，将其作为currentInitStmts
     *
     * @param funcName 转化过后的函数名
     */
    private void getCurrentFunc(String funcName) {
        currentFunction = irRoot.getFunc(funcName);
        currentInitStmts = new LinkedList<>();
        //如果有返回值，先给retVal分配空间
        if (!(currentFunction.retType instanceof VoidType)) {
            Alloca stmt = new Alloca(currentFunction.retType, currentFunction.funcName + "_retVal");
            currentInitStmts.add(stmt);
            currentFunction.retVal = stmt.result;
            //main有缺省的返回值
            if ("main".equals(funcName)) {
                currentInitStmts.add(
                        new Store(zero, stmt.result)
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
        BasicBlock trueStmtBlock = new BasicBlock(currentFunction.funcName + "_if.then" + label);
        BasicBlock falseStmtBlock = null;
        BasicBlock endBlock = new BasicBlock(currentFunction.funcName + "_if.end" + label);
        BasicBlock next = endBlock;
        if (node.falseStatement != null) {
            falseStmtBlock = new BasicBlock(currentFunction.funcName + "_if.else" + label);
            next = falseStmtBlock;
        }
        //cond：在上一个块里
        operator = null;
        Entity entity = toBool(getValue(node.condition.accept(this)));
        pushBack(
                new Branch(entity, trueStmtBlock, next)
        );
        changeBlock(trueStmtBlock);
        currentFunction.blockMap.put(currentBlock.label, currentBlock);
        if (node.trueStatement instanceof BlockStmtNode) {
            node.trueStatement.accept(this);
        } else {
            enterScope(node);
            node.trueStatement.accept(this);
            exitScope();
        }
        if (currentBlock.tailStmt == null) {
            pushBack(
                    new Jump(endBlock)
            );
        }
        //false
        if (node.falseStatement != null) {
            changeBlock(falseStmtBlock);
            currentFunction.blockMap.put(currentBlock.label, currentBlock);
            if (node.falseStatement instanceof BlockStmtNode) {
                node.falseStatement.accept(this);
            } else {
                enterScope(node, true);
                node.falseStatement.accept(this);
                exitScope();
            }
            if (currentBlock.tailStmt == null) {
                pushBack(
                        new Jump(endBlock)
                );
            }
        }
        changeBlock(endBlock);
        currentFunction.blockMap.put(currentBlock.label, currentBlock);
        return null;
    }

    /**
     * ReturnStmtNode
     * 函数的return
     * - 有无返回值
     * //TODO：优化，不借助retVal、提前退出函数
     * //TODO:同一个block中return后的部分失效，如何处理
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
            Entity val = getValue(
                    node.expression.accept(this)
            );
            if (val instanceof Null) {
                val.type = currentFunction.retType;
            }
            pushBack(
                    new Store(fromBool(val), currentFunction.retVal)
            );
        }
        pushBack(
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
        enterScope(node);
        Entity entity;
        //作为当前block的特殊标识符
        LoopScope loopScope = (LoopScope) currentScope;
        loopScope.label = funcBlockCounter++;
        BasicBlock condBlock = new BasicBlock(currentFunction.funcName + "_loop.cond" + loopScope.label);
        BasicBlock bodyBlock = new BasicBlock(currentFunction.funcName + "_loop.body" + loopScope.label);
        BasicBlock endBlock = new BasicBlock(currentFunction.funcName + "_loop.end" + loopScope.label);
        pushBack(
                new Jump(condBlock)
        );
        changeBlock(condBlock);
        currentFunction.blockMap.put(currentBlock.label, currentBlock);
        operator = null;
        entity = toBool(getValue(node.condition.accept(this)));
        pushBack(
                new Branch(entity, bodyBlock, endBlock)
        );
        changeBlock(bodyBlock);
        currentFunction.blockMap.put(currentBlock.label, currentBlock);
        if (node.statement != null) {
            node.statement.accept(this);
        }
        pushBack(
                new Jump(condBlock)
        );
        changeBlock(endBlock);
        currentFunction.blockMap.put(currentBlock.label, currentBlock);
        exitScope();
        return null;
    }

    /**
     * ArrayVisExprNode
     * 数组下标访问，层层嵌套
     * 对每一层[]调用getelementptr
     * TODO:返回值是指针，注意是否需要取值
     *
     * @param node ArrayVisExprNode
     * @return result
     */
    @Override
    public Entity visit(ArrayVisExprNode node) {
        //下标访问（必定为int）
        ArrayList<Storage> indexList = new ArrayList<>();
        node.indexList.forEach(
                index -> indexList.add(getValue(index.accept(this)))
        );
        //取出数组名(Ptr\LocalTmpVar(PtrType))
        Entity array = node.arrayName.accept(this);
        LocalTmpVar arrayName;
        ArrayType arrayType;
        //数组名（变量）
        if (array instanceof Ptr ptr) {
            arrayName = new LocalTmpVar(ptr.storage.type, ++tmpCounter.cnt, currentFunction.funcName);
            pushBack(
                    new Load(arrayName, ptr)
            );
            arrayType = (ArrayType) arrayName.type;
        }
        //临时变量，指向数组名的指针
        else if (array instanceof LocalTmpVar && array.type instanceof PtrType ptrType) {
            if (ptrType.type instanceof ArrayPtrType arrayPtr) {
                arrayName = new LocalTmpVar(arrayPtr.type, ++tmpCounter.cnt, currentFunction.funcName);
                pushBack(
                        new Load(arrayName, array)
                );
                arrayType = (ArrayType) arrayPtr.type;
            } else {
                arrayName = (LocalTmpVar) array;
                arrayType = (ArrayType) ptrType.type;
            }
        } else {
            throw new InternalException("unexpected array name");
        }
        ArrayType cur = arrayType;
        Entity idx;
        LocalTmpVar prev = arrayName, result = null;
        for (int i = 1; i <= indexList.size(); ++i) {
            idx = indexList.get(i - 1);
            //访问到基本元素
            if (arrayType.dimension == i) {
                result = new LocalTmpVar(new PtrType(arrayType.type), ++tmpCounter.cnt, currentFunction.funcName);
                pushBack(
                        new GetElementPtr(result, prev, idx)
                );
                //TODO:load?
                return result;
            }
            //仍然访问到数组
            else {
                cur = new ArrayType(cur.type, cur.dimension - 1);
                result = new LocalTmpVar(new PtrType(cur), ++tmpCounter.cnt, currentFunction.funcName);
                pushBack(
                        new GetElementPtr(result, prev, idx)
                );
                prev = new LocalTmpVar(cur, ++tmpCounter.cnt, currentFunction.funcName);
                pushBack(
                        new Load(prev, result)
                );
            }
        }
        if (result == null) {
            throw new InternalException("invalid array visit");
        }
        return result;
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
        Entity right = getValue(node.rhs.accept(this));
        if (right instanceof Null) {
            IRType leftType;
            if (left instanceof Ptr ptr) {
                leftType = ptr.type;
            } else {
                leftType = ((PtrType) left.type).type;
            }
            right.type = leftType;
        }
        pushBack(
                new Store(fromBool(right), left)
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
        LocalTmpVar result = new LocalTmpVar(lhs.type, ++tmpCounter.cnt, currentFunction.funcName);
        //str1+str2
        if (isString(lhs)) {
            Call stmt = new Call(irRoot.getFunc("_string_add"), result);
            stmt.parameterList.add(lhs);
            stmt.parameterList.add(rhs);
            pushBack(stmt);
            return result;
        }
        pushBack(
                new Binary(node.operator,
                        result,
                        lhs,
                        rhs
                )
        );
        return result;
    }

    private boolean isString(Entity entity) {
        return entity.type instanceof ArrayType arrayType
                && arrayType.type instanceof IntType type
                && type.typeName.equals(IntType.TypeName.CHAR);
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
        LocalTmpVar result = new LocalTmpVar(tmpBoolType, ++tmpCounter.cnt, currentFunction.funcName);
        if (isString(lhs)) {
            String functionName;
            switch (node.operator) {
                case Less -> functionName = "_string_less";
                case Greater -> functionName = "_string_greater";
                case LessEqual -> functionName = "_string_lessOrEqual";
                case GreaterEqual -> functionName = "_string_greaterOrEqual";
                case Equal -> functionName = "_string_equal";
                case NotEqual -> functionName = "_string_notEqual";
                default -> throw new InternalException("unexpected operator in Icmp instruction");
            }
            Call stmt = new Call(irRoot.getFunc(functionName), result);
            stmt.parameterList.add(lhs);
            stmt.parameterList.add(rhs);
            pushBack(stmt);
            return result;
        }
        pushBack(
                new Icmp(node.operator,
                        result,
                        lhs,
                        rhs
                )
        );
        return result;
    }

    //返回值
    private Storage getValue(Entity entity) {
        LocalTmpVar tmp;
        if (entity instanceof Ptr) {
            tmp = new LocalTmpVar(((Ptr) entity).storage.type, ++tmpCounter.cnt, currentFunction.funcName);
            pushBack(
                    new Load(tmp, entity)
            );
            return tmp;
        }
        //处理getElementPtr结果
        else if (entity.type instanceof PtrType ptrType) {
            //自定义类
            if (ptrType instanceof StructPtrType) {
                return (Storage) entity;
            }
            //指向ArrayPtrType的指针
            if (ptrType.type instanceof ArrayPtrType arrayPtr) {
                tmp = new LocalTmpVar(arrayPtr.type, ++tmpCounter.cnt, currentFunction.funcName);
                pushBack(
                        new Load(tmp, entity)
                );
                return tmp;
            }
            tmp = new LocalTmpVar(ptrType.type, ++tmpCounter.cnt, currentFunction.funcName);
            pushBack(
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
        //直接调用的函数
        if (!getMemberFunc) {
            currentVar.push(zero);
        }
        getMemberFunc = false;
        //值为null的参数
        ArrayList<Integer> nullList = new ArrayList<>();
        //参数表访问
        ArrayList<Storage> params = new ArrayList<>();
        for (int i = 0; i < node.parameterList.size(); ++i) {
            Storage param = getValue(node.parameterList.get(i).accept(this));
            if (param instanceof Null) {
                nullList.add(i);
            }
            params.add(param);
        }
        //得到函数名
        getFuncName = true;
        node.func.accept(this);
        getFuncName = false;
        Function function;
        LocalTmpVar result = null;
        Call stmt;
        Storage current = currentVar.pop();
        if (current instanceof Constant) {
            //隐含的成员方法调用
            if (addThis) {
                //先取this
                Storage this1 = getValue(rename2mem.get("this1"));
                params.add(0, this1);
                addThis = false;
                nullList.replaceAll(integer -> integer + 1);
            }
            //普通函数
            function = irRoot.getFunc(callFuncName);
            nullList.forEach(
                    index -> {
                        Storage nullParam = params.get(index);
                        nullParam.type = function.parameterList.get(index).type;
                    }
            );
            if (!(function.retType instanceof VoidType)) {
                ++tmpCounter.cnt;
            }
            result = new LocalTmpVar(function.retType, tmpCounter.cnt, currentFunction.funcName);
            stmt = new Call(function, result, params);
        } else {
            //this指针（指向结构体类型的局部变量）
            LocalTmpVar thisVar = new LocalTmpVar(current.type, ++tmpCounter.cnt, currentFunction.funcName);
            pushBack(
                    new GetElementPtr(thisVar, current, zero)
            );
            params.add(0, thisVar);
            nullList.replaceAll(integer -> integer + 1);
            //自定义类
            if (current.type instanceof StructPtrType structPtrType) {
                StructType classType = (StructType) structPtrType.type;
                function = irRoot.getFunc(classType.name + "." + callFuncName);
                nullList.forEach(
                        index -> {
                            Storage nullParam = params.get(index);
                            nullParam.type = function.parameterList.get(index).type;
                        }
                );
                if (!(function.retType instanceof VoidType)) {
                    result = new LocalTmpVar(function.retType, ++tmpCounter.cnt, currentFunction.funcName);
                    stmt = new Call(function, result, params);
                } else {
                    stmt = new Call(function);
                    stmt.parameterList = params;
                }
            } else {//内建类的方法
                function = irRoot.getFunc(callFuncName);
                result = new LocalTmpVar(function.retType, ++tmpCounter.cnt, currentFunction.funcName);
                stmt = new Call(function, result, params);
            }
        }
        pushBack(stmt);
        return result;
    }

    /**
     * LogicExprNode
     *
     * @param node && ||逻辑运算
     * @return result
     */
    @Override
    public Entity visit(LogicExprNode node) {
        ++phiCounter.cnt;//会出现phi
        int phiLabel = phiCounter.cnt;
        int label = logicExprCounter;//函数中出现的第几个逻辑表达式
        ++logicExprCounter;
        BasicBlock nextBlock = new BasicBlock(currentFunction.funcName + "_logic.next" + label);//右子树
        BasicBlock endBlock = new BasicBlock(currentFunction.funcName + "_logic.end" + label);//phi和后续（父亲）
        //根据自身的运算符放trueBlock，falseBlock
        ConstBool resultFromLeft;
        BasicBlock trueBlock, falseBlock;
        if (node.operator.equals(LogicExprNode.LogicOperator.AndAnd)) {
            trueBlock = nextBlock;
            falseBlock = endBlock;
            resultFromLeft = boolFalse;
        } else {
            trueBlock = endBlock;
            falseBlock = nextBlock;
            resultFromLeft = boolTrue;
        }
        //左子树
        Storage leftResult = toBool(getValue(node.lhs.accept(this)));
        pushBack(
                new Branch(leftResult, trueBlock, falseBlock,
                        currentFunction.funcName + phiLabel, ".left", resultFromLeft)
        );
        String labelFromLeft = currentBlock.label;
        //右子树都返回end
        changeBlock(nextBlock);
        currentFunction.blockMap.put(currentBlock.label, currentBlock);
        Storage resultFromRight = toBool(getValue(node.rhs.accept(this)));
        pushBack(
                new Jump(endBlock, currentFunction.funcName + phiLabel, ".right", resultFromRight)
        );
        String labelFromRight = currentBlock.label;
        changeBlock(endBlock);
        currentFunction.blockMap.put(currentBlock.label, currentBlock);
        //取值
        LocalTmpVar result = new LocalTmpVar(tmpBoolType, ++tmpCounter.cnt, currentFunction.funcName);
        pushBack(
                new Phi(result,
                        resultFromLeft, resultFromRight,
                        labelFromLeft, labelFromRight,
                        currentFunction.funcName + phiLabel)
        );
        currentFunction.phiResult.put(currentFunction.funcName + phiLabel, result);
        return result;
    }

    private Storage toBool(Entity entity) {
        LocalTmpVar tmp, toBool;
        if (entity instanceof ConstBool) {
            return (Storage) entity;
        }
        if (entity instanceof Ptr) {
            tmp = new LocalTmpVar(((Ptr) entity).storage.type, ++tmpCounter.cnt, currentFunction.funcName);
            pushBack(
                    new Load(tmp, entity)
            );
        } else {
            tmp = (LocalTmpVar) entity;
        }
        if (tmp.type instanceof IntType type && type.typeName.equals(IntType.TypeName.BOOL)) {
            toBool = new LocalTmpVar(tmpBoolType, ++tmpCounter.cnt, currentFunction.funcName);
            pushBack(
                    new Trunc(toBool, tmp)
            );
        } else {
            toBool = tmp;
        }
        return toBool;
    }

    //将i1局部临时变量转化为i8
    private Entity fromBool(Entity entity) {
        LocalTmpVar fromBool;
        if (entity.type instanceof IntType type && type.typeName.equals(IntType.TypeName.TMP_BOOL)) {
            fromBool = new LocalTmpVar(boolType, ++tmpCounter.cnt, currentFunction.funcName);
            pushBack(
                    new Zext(fromBool, (Storage) entity)
            );
            return fromBool;
        } else {
            return entity;
        }
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
        Storage entity = toBool(getValue(node.expression.accept(this)));
        LocalTmpVar result = new LocalTmpVar(tmpBoolType, ++tmpCounter.cnt, currentFunction.funcName);
        pushBack(
                new Binary(
                        BinaryExprNode.BinaryOperator.Xor,
                        result, entity, new ConstBool(true)
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
        currentVar.push(getValue(node.lhs.accept(this)));
        if (!(node.rhs instanceof FuncCallExprNode)) {
            getMemberVar = true;
        } else {
            getMemberFunc = true;
        }
        return node.rhs.accept(this);
    }

    /**
     * NewExprNode
     * 实例化对象\创建数组
     * 调用自己写的内置函数_malloc分配空间
     * - dimension总维数
     * - dimensions给出实际长度的几维
     *
     * @param node NewExprNode
     * @return ptr
     */
    @Override
    public Entity visit(NewExprNode node) {
        //实例化类对象
        if (node.dimension == 0) {
            StructPtrType ptrType = (StructPtrType) node.typeNode.accept(this).type;
            StructType type = (StructType) ptrType.type;
            LocalTmpVar ptr = new LocalTmpVar(new PtrType(ptrType), ++tmpCounter.cnt, currentFunction.funcName);
            Call callStmt = new Call(malloc, ptr);
            callStmt.parameterList.add(new ConstInt(((Integer) (type.getSize() / 8)).toString()));
            pushBack(callStmt);
            //指向结构体的指针
            LocalTmpVar tmpPtr = new LocalTmpVar(ptrType, ++tmpCounter.cnt, currentFunction.funcName);
            pushBack(
                    new GetElementPtr(tmpPtr, ptr, zero)
            );
            //有构造函数，调用构造函数初始化
            if (irRoot.funcDef.containsKey(type.name)) {
                Function function = irRoot.getFunc(type.name);
                Call stmt = new Call(function);
                stmt.parameterList.add(tmpPtr);
                pushBack(stmt);
            }
            return tmpPtr;
        }
        //实例化数组
        //分配到size给出的，余下的当成指针
        else {
            //数组类型，含基本元素类型和数组维度
            ArrayType type = (ArrayType) node.typeNode.accept(this).type;
            //先将dimensions中的元素取出来
            ArrayList<Storage> indexList = new ArrayList<>();
            for (int i = 0; i < node.dimensions.size(); ++i) {
                indexList.add(
                        getValue(node.dimensions.get(i).accept(this))
                );
            }
            //最外一层，指向数组的指针
            IRType currentEleType;
            if (node.dimension == 1) {
                currentEleType = type.type;
            } else {
                currentEleType = new ArrayType(type.type, node.dimension - 1);
            }
            //数组长度
            Storage size = indexList.get(0);
            //给当前层分配空间
            //指向当前数组的指针
            LocalTmpVar root = new LocalTmpVar(new PtrType(type), ++tmpCounter.cnt, currentFunction.funcName);
            Call callStmt = new Call(malloc_array, root);
            callStmt.parameterList.add(new ConstInt(((Integer) (currentEleType.getSize() / 8)).toString()));
            callStmt.parameterList.add(size);
            pushBack(callStmt);
            //当前数组（本质是一个指针）
            LocalTmpVar result = new LocalTmpVar(type, ++tmpCounter.cnt, currentFunction.funcName);
            pushBack(
                    new GetElementPtr(result, root, zero)
            );
            //高维数组
            if (indexList.size() > 1) {
                constructArray(indexList, node.dimension, 1, root);
            }
            return result;
        }
    }

    /**
     * 递归建立数组
     */
    private void constructArray(ArrayList<Storage> indexList,
                                int dimension,
                                int layer,
                                LocalTmpVar root) {
        //手写IR上for循环向下递归
        int label = funcBlockCounter++;
        BasicBlock condBlock = new BasicBlock(currentFunction.funcName + "_loop.cond" + label),
                incBlock = new BasicBlock(currentFunction.funcName + "_loop.inc" + label),
                bodyBlock = new BasicBlock(currentFunction.funcName + "_loop.body" + label),
                endBlock = new BasicBlock(currentFunction.funcName + "_loop.end" + label);
        //int i=0;
        LocalVar i_;//允许被通用的全局变量
        String i_name = currentFunction.funcName + ".i";
        if (rename2mem.containsKey(i_name)) {
            i_ = (LocalVar) rename2mem.get(i_name);
        } else {
            Alloca stmt = new Alloca(zero.type, i_name);
            pushBack(stmt);
            i_ = stmt.result;
            rename2mem.put(i_name, i_);
        }
        pushBack(
                new Store(zero, i_)
        );
        pushBack(
                new Jump(condBlock)
        );
        //i<size;
        changeBlock(condBlock);
        currentFunction.blockMap.put(currentBlock.label, currentBlock);
        Entity op1 = getValue(i_), op2 = getValue(indexList.get(layer - 1));
        LocalTmpVar cmpResult = new LocalTmpVar(tmpBoolType, ++tmpCounter.cnt, currentFunction.funcName);
        pushBack(
                new Icmp(CmpExprNode.CmpOperator.Less,
                        cmpResult,
                        op1,
                        op2)
        );
        pushBack(
                new Branch(cmpResult, bodyBlock, endBlock)
        );
        //循环体
        changeBlock(bodyBlock);
        currentFunction.blockMap.put(currentBlock.label, currentBlock);
        IRType type = ((PtrType) root.type).type;
        type = ((ArrayType) type).type;//基本元素类型
        //非基本元素
        if ((layer + 1) != dimension) {
            type = new ArrayType(type, dimension - layer - 1);
        }
        //给数组长度分配空间
        Storage size = indexList.get(layer);
        //给当前层分配空间
        //计算需要的空间
        LocalTmpVar index = new LocalTmpVar(intType, ++tmpCounter.cnt, currentFunction.funcName);
        pushBack(
                new Load(index, i_)
        );
        LocalTmpVar newRoot = new LocalTmpVar(new PtrType(type), ++tmpCounter.cnt, currentFunction.funcName);
        pushBack(
                new GetElementPtr(newRoot, root, index)
        );
        LocalTmpVar tmpRoot = new LocalTmpVar(new PtrType(newRoot.type), ++tmpCounter.cnt, currentFunction.funcName);
        Call callStmt = new Call(malloc_array, tmpRoot);
        callStmt.parameterList.add(new ConstInt(((Integer) (type.getSize() / 8)).toString()));
        callStmt.parameterList.add(size);
        pushBack(callStmt);
        LocalTmpVar result = new LocalTmpVar(newRoot.type, ++tmpCounter.cnt, currentFunction.funcName);
        pushBack(
                new GetElementPtr(result, tmpRoot, zero)
        );
        pushBack(
                new Store(result, newRoot)
        );
        //判断是否终止
        if ((1 + layer) != indexList.size()) {
            constructArray(indexList, dimension, layer + 1, newRoot);
        }
        pushBack(
                new Jump(incBlock)
        );
        //step
        changeBlock(incBlock);
        currentFunction.blockMap.put(currentBlock.label, currentBlock);
        Entity op = getValue(i_);
        LocalTmpVar addResult = new LocalTmpVar(intType, ++tmpCounter.cnt, currentFunction.funcName);
        pushBack(
                new Binary(BinaryExprNode.BinaryOperator.Plus,
                        addResult,
                        op,
                        new ConstInt("1"))
        );
        pushBack(
                new Store(addResult, i_)
        );
        pushBack(
                new Jump(condBlock)
        );
        changeBlock(endBlock);
        currentFunction.blockMap.put(currentBlock.label, currentBlock);
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
        //++\--需要存原始值作为返回值
        if (node.operator == PrefixExprNode.PrefixOperator.PlusPlus ||
                node.operator == PrefixExprNode.PrefixOperator.MinusMinus) {
            Storage tmp = getValue(entity);
            LocalTmpVar result = new LocalTmpVar(tmp.type, ++tmpCounter.cnt, currentFunction.funcName);
            Entity right = new ConstInt("1");
            BinaryExprNode.BinaryOperator operator =
                    node.operator == PrefixExprNode.PrefixOperator.PlusPlus ?
                            BinaryExprNode.BinaryOperator.Plus :
                            BinaryExprNode.BinaryOperator.Minus;
            //运算
            pushBack(
                    new Binary(operator, result, tmp, right)
            );
            //赋值给a
            pushBack(
                    new Store(result, entity)
            );
            return entity;//返回变量
        }
        Storage value = getValue(entity);
        LocalTmpVar result = new LocalTmpVar(value.type, ++tmpCounter.cnt, currentFunction.funcName);
        //-取相反数
        if (node.operator == PrefixExprNode.PrefixOperator.Minus) {
            Entity left = zero;
            pushBack(
                    new Binary(BinaryExprNode.BinaryOperator.Minus,
                            result, left, value)
            );
            return result;
        }
        //按位取反
        Entity right = new ConstInt("-1");
        pushBack(
                new Binary(BinaryExprNode.BinaryOperator.Xor,
                        result, value, right)
        );
        return result;
    }

    /**
     * PointerExprNode
     * this指针
     * 仅出现在类成员函数中
     * 一个类函数仅有一个this1
     *
     * @param node PointerExprNode
     * @return result
     */
    @Override
    public Entity visit(PointerExprNode node) {
        return rename2mem.get("this1");
    }

    /**
     * SuffixExprNode
     * 返回原先值
     * ++\--
     *
     * @param node SuffixExprNode
     * @return tmp
     */
    @Override
    public Entity visit(SuffixExprNode node) {
        Entity entity = node.expression.accept(this);
        LocalTmpVar tmp = (LocalTmpVar) getValue(entity);
        LocalTmpVar result = new LocalTmpVar(tmp.type, ++tmpCounter.cnt, currentFunction.funcName);
        Entity right = new ConstInt("1");
        BinaryExprNode.BinaryOperator operator =
                node.operator == SuffixExprNode.SuffixOperator.PlusPlus ?
                        BinaryExprNode.BinaryOperator.Plus :
                        BinaryExprNode.BinaryOperator.Minus;
        pushBack(
                new Binary(operator, result, tmp, right)
        );
        pushBack(
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
        Integer phiLabel = ++phiCounter.cnt;
        int label = funcBlockCounter++;
        BasicBlock trueStmtBlock = new BasicBlock(currentFunction.funcName + "_cond.true" + label);
        BasicBlock falseStmtBlock = new BasicBlock(currentFunction.funcName + "_cond.false" + label);
        BasicBlock endBlock = new BasicBlock(currentFunction.funcName + "_cond.end" + label);
        //cond：在上一个块里
        operator = null;
        Entity entity = toBool(getValue(node.condition.accept(this)));
        pushBack(
                new Branch(entity, trueStmtBlock, falseStmtBlock)
        );
        changeBlock(trueStmtBlock);
        currentFunction.blockMap.put(currentBlock.label, currentBlock);
        operator = null;
        Storage trueAns = getValue(node.trueExpr.accept(this));
        pushBack(
                new Jump(endBlock, currentFunction.funcName + String.valueOf(phiLabel), ".true", trueAns)
        );
        BasicBlock trueBlock = currentBlock;
        changeBlock(falseStmtBlock);
        currentFunction.blockMap.put(currentBlock.label, currentBlock);
        operator = null;
        Storage falseAns = getValue(node.falseExpr.accept(this));
        pushBack(
                new Jump(endBlock, currentFunction.funcName + String.valueOf(phiLabel), ".false", falseAns)
        );
        BasicBlock falseBlock = currentBlock;
        changeBlock(endBlock);
        currentFunction.blockMap.put(currentBlock.label, currentBlock);
        LocalTmpVar result;
        if (!(trueAns.type instanceof VoidType)) {
            result = new LocalTmpVar(trueAns.type, ++tmpCounter.cnt, currentFunction.funcName);
            pushBack(
                    new Phi(result, trueAns, falseAns,
                            trueBlock.label, falseBlock.label,
                            currentFunction.funcName + String.valueOf(phiLabel))
            );
            currentFunction.phiResult.put(currentFunction.funcName + String.valueOf(phiLabel), result);
        } else {
            //仅用于表示类型
            result = new LocalTmpVar(trueAns.type, tmpCounter.cnt, currentFunction.funcName);
        }
        return result;
    }

    /**
     * VarNameExprNode
     * 变量名localPtr、globalPtr
     * 从rename2mem找到重命名后的变量ptr返回
     * 不可以返回storage（可能是赋值语句的left）
     * 如果getFuncName 为寻找函数名
     *
     * @param node VarNameExprNode
     * @return ptr\result(ptrType)
     */
    @Override
    public Entity visit(VarNameExprNode node) {
        //函数名
        if (getFuncName) {
            Storage current = currentVar.peek();
            //非成员访问
            if (current instanceof Constant) {
                if (currentClass != null && currentClass.funcNameSet.contains(node.name)) {
                    callFuncName = currentClass.name + "." + node.name;
                    addThis = true;
                }
                //普通函数
                else {
                    callFuncName = node.name;
                }
                return null;
            }
            //类方法
            if (current.type instanceof StructPtrType) {
                callFuncName = node.name;
                return null;
            }
            if (current.type instanceof ArrayType) {
                if (isString(current)) {
                    callFuncName = "_string_" + node.name;
                    return null;
                }
                callFuncName = "_array_" + node.name;
                return null;
            }
            throw new InternalException("unexpected function call");
        }
        //类成员
        if (getMemberVar) {
            //current存了一个指向结构体的指针的局部临时变量
            Storage current = currentVar.pop();
            StructType structType = (StructType) ((StructPtrType) current.type).type;
            //结构体
            LocalTmpVar struct = new LocalTmpVar(new PtrType(structType), ++tmpCounter.cnt, currentFunction.funcName);
            pushBack(
                    new GetElementPtr(struct, current, zero)
            );
            Integer index = structType.members.get(node.name);
            //成员变量
            IRType type = structType.memberTypes.get(index);
            LocalTmpVar result = new LocalTmpVar(new PtrType(type), ++tmpCounter.cnt, currentFunction.funcName);
            pushBack(
                    new GetElementPtr(result, struct, new ConstInt(index.toString()))
            );
            getMemberVar = false;
            return result;
        }
        //变量名
        if (varMap.containsKey(node.name)) {
            //该变量在当前的重命名
            Pair<Integer, Stack<Integer>> pair = varMap.get(node.name);
            if (!pair.getSecond().empty()) {
                String rename = node.name + "." + pair.getSecond().peek();
                return rename2mem.get(rename);
            }
        }
        //类的成员变量直接访问
        //仅可能出现在类的成员函数中
        //相当于this.xx
        if (currentClass != null && currentClass.members.containsKey(node.name)) {
            //先取this
            Storage this1 = getValue(rename2mem.get("this1"));
            Integer index = currentClass.members.get(node.name);
            //成员变量
            IRType type = currentClass.memberTypes.get(index);
            LocalTmpVar result = new LocalTmpVar(new PtrType(type), ++tmpCounter.cnt, currentFunction.funcName);
            pushBack(
                    new GetElementPtr(result, this1, new ConstInt(index.toString()))
            );
            return result;
        }
        throw new InternalException("unexpected name");
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
        return new ConstBool(node.value);
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
        return new ConstInt(node.value);
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
     * 字符串常量被放在全局
     * 返回指向全局字符串常量（char数组）的指针
     *
     * @param node StrConstantExprNode
     * @return constString
     */
    @Override
    public Entity visit(StrConstantExprNode node) {
        GlobalVar str;
        if (constStringMap.containsKey(node.value)) {
            str = constStringMap.get(node.value);
        } else {
            ++constStrCounter;
            Global stmt = new Global(new ConstString(node.value), ".str." + constStrCounter);
            globalVarDefBlock.pushBack(stmt);
            constStringMap.put(node.value, stmt.result);
            str = stmt.result;
        }
        LocalTmpVar tmp = new LocalTmpVar(str.storage.type, ++tmpCounter.cnt, currentFunction.funcName);
        pushBack(
                new GetElementPtr(tmp, str, zero)
        );
        return tmp;
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
        enterScope(node);
        currentClass = (StructType) ((StructPtrType) irRoot.types.get(node.name)).type;
        StmtNode stmt;
        for (int i = 0; i < node.members.size(); ++i) {
            stmt = node.members.get(i);
            if (stmt instanceof FuncDefStmtNode) {
                stmt.accept(this);
            } else if (stmt instanceof ConstructorDefStmtNode) {
                stmt.accept(this);
            }
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
        if (node.type instanceof ast.type.IntType) {
            return zero;
        }
        if (node.type instanceof ast.type.BoolType) {
            return new Storage(boolType);
        }
        if (node.type instanceof ast.type.NullType) {
            return new Null();
        }
        if (node.type instanceof ast.type.StringType) {
            return new ConstString("");
        }
        if (node.type instanceof ast.type.ArrayType arrayType) {
            IRType irType = irRoot.type2irType(arrayType.eleType);
            if (irType instanceof StructType structType) {
                irType = new StructPtrType(structType);
            }
            return new Storage(
                    new ArrayType(irType, arrayType.dimensions)
            );
        }
        if (node.type instanceof ast.type.ClassType classType) {
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
            currentBlock = globalInitFunc.currentBlock;
            currentFunction = globalInitFunc;
            name = rename(node.name);
            //标明类型的空间，无初始值
            Storage initVar = new Storage(irType);
            //have init?
            if (node.initExpr != null) {
                //如果为字面量，返回constant,直接初始化
                //否则返回在初始化函数中用赋值语句初始化
                operator = null;
                entity = getValue(node.initExpr.accept(this));
                if (entity instanceof Constant constant) {
                    if (constant instanceof Null) {
                        constant.type = irType;
                    }
                    initVar = constant;
                }
            }
            //调用Global指令为全局变量分配空间
            Global stmt = new Global(initVar, name);
            globalVarDefBlock.pushBack(stmt);
            //将rename -> mem映射存入map
            rename2mem.put(name, stmt.result);
            //非字面量初始化
            //在全局的初始化函数中加入赋值语句
            if (entity != null && !(entity instanceof Constant)) {
                pushBack(
                        new Store(fromBool(entity), stmt.result)
                );
            }
        }
        //局部变量
        else {
            name = rename(node.name);
            //普通局部变量，Alloca分配空间
            Alloca stmt = new Alloca(irType, name);
            currentInitStmts.add(stmt);
            //将rename -> mem映射存入map
            rename2mem.put(name, stmt.result);
            //若有初始化语句，在走到该部分时用赋值语句
            if (node.initExpr != null) {
                operator = null;
                entity = getValue(node.initExpr.accept(this));
                if (entity instanceof Null) {
                    entity.type = irType;
                }
                pushBack(
                        new Store(fromBool(entity), stmt.result)
                );
            }
        }
        return null;
    }
}
