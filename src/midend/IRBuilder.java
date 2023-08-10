//package midend;
//
//import ast.*;
//import ast.expr.*;
//import ast.expr.ConstantExprNode.*;
//import ast.other.*;
//import ast.stmt.*;
//import ir.entity.*;
//import ir.entity.constant.*;
//import ir.*;
//import ir.entity.var.*;
//import ir.function.Function;
//import ir.irType.*;
//import ir.stmt.instruction.*;
//import ir.stmt.terminal.*;
//import utility.*;
//import utility.scope.*;
//import utility.type.Type;
//
//import java.util.HashMap;
//import java.util.Map;
//
//
///**
// * @author F
// * 遍历AST构建IR
// * - 维护Scope用来处理重名问题
// * - 最终应该维护好block
// */
//public class IRBuilder implements ASTVisitor<Entity> {
//    private IRRoot irRoot;
//
//    //当前作用域
//    //IR上不需要新建Scope，直接使用ASTNode存下来的scope
//    Scope currentScope = null;
//    Function currentFunction = null;
//    //当前的类
//    StructType currentClass = null;
//    //当前块
//    BasicBlock currentBlock = null;
//
//    //全局变量的初始化块；负责变量的空间申请
//    public Pair<BasicBlock, BasicBlock> globalInitBlock =
//            new Pair<>(new BasicBlock("global_var_def"), new BasicBlock("global_var_init"));
//
//    //当前的初始化块,
//    private BasicBlock currentInitBlock;
//
//    //变量名 -> <覆盖层次,对应mem空间>
//    //变量重命名即为 int+name
//    public HashMap<String, Integer> varMap = new HashMap<>();
//
//    //重命名后的name -> Ptr
//    public HashMap<String, Ptr> rename2mem = new HashMap<>();
//
//    private String rename(String name) {
//        Integer num;
//        if (varMap.containsKey(name)) {
//            num = varMap.get(name);
//            num += 1;
//        } else {
//            num = 1;
//        }
//        varMap.put(name, num);
//        return num + name;
//    }
//
//    //退出当前scope时，将所有新建的变量数目--
//    private void exitScope() {
//        Integer num;
//        for (Map.Entry<String, Type> entry : currentScope.name2type.entrySet()) {
//            String key = entry.getKey();
//            num = varMap.get(key);
//            num -= 1;
//            varMap.put(key, num);
//        }
//        currentScope = currentScope.getParent();
//    }
//
//    public IRBuilder(SymbolTable symbolTable) {
//        irRoot = new IRRoot(symbolTable);
//        irRoot.globalVarDefBlock = globalInitBlock.getFirst();
//        irRoot.globalVarInitBlock = globalInitBlock.getSecond();
//    }
//
//    /**
//     * RootNode
//     * 访问AST根结点
//     * 进入program，构建GlobalScope为当前作用域
//     * 访问root的所有子结点
//     * - 将所有的全局变量加到IRRoot下
//     * - 构建全局变量的初始化函数
//     * - 找到main，将所有全局变量的初始化函数加到main的entry
//     *
//     * @param node RootNode
//     * @return null
//     */
//    @Override
//    public Entity visit(RootNode node) {
//        currentScope = node.scope;
//        node.declarations.forEach(
//                def -> def.accept(this)
//        );
//        exitScope();
//        return null;
//    }
//
//    /**
//     * BlockStmtNode
//     * 用{}显式表示为作用域块
//     * - scope
//     * 进入时构建BlockScope
//     * （将亲代的funcScope、loopScope加入）
//     * 进而访问子结点
//     * 退出返回到上层作用域
//     * - block
//     * 无操作
//     *
//     * @param node BlockStmtNode
//     * @return null
//     */
//    @Override
//    public Entity visit(BlockStmtNode node) {
//        currentScope = node.scope;
//        node.statements.forEach(
//                stmt -> stmt.accept(this)
//        );
//        exitScope();
//        return null;
//    }
//
//    /**
//     * BreakStmtNode
//     * 从LoopScope中找到loop在该函数中的label
//     * 无条件跳转到end块
//     *
//     * @param node BreakStmtNode
//     * @return null
//     */
//    @Override
//    public Entity visit(BreakStmtNode node) {
//        LoopScope loopScope = currentScope.getParentLoopScope();
//        currentBlock.pushBack(
//                new Jump(currentFunction.blockMap.get("loop.end" + loopScope.label))
//        );
//        return null;
//    }
//
//    @Override
//    public Entity visit(ConstructorDefStmtNode node) {
//
//    }
//
//    /**
//     * ContinueStmtNode
//     * 直接跳转到inc
//     *
//     * @param node ContinueStmtNode
//     * @return null
//     */
//    @Override
//    public Entity visit(ContinueStmtNode node) {
//        LoopScope loopScope = currentScope.getParentLoopScope();
//        if (loopScope.hasInc) {
//            currentBlock.pushBack(
//                    new Jump(currentFunction.blockMap.get("loop.inc" + loopScope.label))
//            );
//        } else if (loopScope.hasCond) {
//            currentBlock.pushBack(
//                    new Jump(currentFunction.blockMap.get("loop.cond" + loopScope.label))
//            );
//        } else {
//            currentBlock.pushBack(
//                    new Jump(currentFunction.blockMap.get("loop.body" + loopScope.label))
//            );
//        }
//        return null;
//    }
//
//    /**
//     * ExprStmtNode
//     *
//     * @param node ExprStmtNode
//     * @return null
//     */
//    @Override
//    public Entity visit(ExprStmtNode node) {
//        node.exprList.forEach(expr -> expr.accept(this));
//        return null;
//    }
//
//    /**
//     * ForStmtNode
//     * 进入新LoopScope
//     * 先initializationStmt，和当前block同一个
//     * loop.cond、loop.body、loop.inc、loop.end四个block依次创建加入func（注意block名）
//     * loop.body,loop.end保证存在
//     * 中间可能穿插别的block
//     *
//     * @param node ForStmtNode
//     * @return null
//     */
//    @Override
//    public Entity visit(ForStmtNode node) {
//        currentScope = node.scope;
//        Entity entity = null;
//        //作为当前block的特殊标识符
//        LoopScope loopScope = (LoopScope) currentScope;
//        loopScope.label = currentFunction.cnt++;
//        //loop的组分
//        BasicBlock condBlock = null, incBlock = null;
//        BasicBlock bodyBlock = new BasicBlock("loop.body" + loopScope.label),
//                endBlock = new BasicBlock("loop.end" + loopScope.label);
//        //init加在currentBlock
//        if (node.initializationStmt != null) {
//            node.initializationStmt.accept(this);
//        }
//        BasicBlock start;
//        //cond若存在，条件跳转，否则直接跳转到body
//        if (loopScope.hasCond) {
//            condBlock = new BasicBlock("loop.cond" + loopScope.label);
//            start = condBlock;
//            currentBlock.pushBack(
//                    new Jump(condBlock)
//            );
//            currentBlock = condBlock;
//            entity = node.condition.accept(this);
//            currentBlock.pushBack(
//                    new Branch(entity, bodyBlock, endBlock)
//            );
//        } else {
//            start = bodyBlock;
//            currentBlock.pushBack(
//                    new Jump(bodyBlock)
//            );
//        }
//        //body若存在，访问，
//        //至少存在跳到下一次循环的语句
//        currentBlock = bodyBlock;
//        if (node.statement != null) {
//            node.statement.accept(this);
//        }
//        if (loopScope.hasInc) {
//            incBlock = new BasicBlock("loop.inc" + loopScope.label);
//            currentBlock.pushBack(
//                    new Jump(incBlock)
//            );
//            currentBlock = incBlock;
//            node.step.accept(this);
//        }
//        currentBlock.pushBack(
//                new Jump(start)
//        );
//        //for循环结束后，当前block为loop.end
//        currentBlock = endBlock;
//        exitScope();
//        return null;
//    }
//
//    /**
//     * FuncDefStmtNode
//     * 函数定义
//     * 新建currentFunc，出函数定义块，置为null
//     * - 全局函数 funcName
//     * - 自定义类函数 className.funcName
//     * 所有函数形成新的作用域
//     * 函数的第一个block为所有局部变量的alloca
//     * 入参：
//     * int和bool类型，函数参数采用值传递；
//     * 其他类型，函数参数采用引用传递。（指针指向同一块内存空间）
//     * 引用本身采用值传递（如果在函数里改变了指针指向，不影响函数外）
//     * TODO:参数赋值（加一个参数初始化函数）
//     * TODO:入参、虚拟寄存器？ call的时候做什么
//     *
//     * @param node FuncDefStmtNode
//     * @return null
//     */
//    @Override
//    public Entity visit(FuncDefStmtNode node) {
//        Entity entity = node.returnType.accept(this);
//        ClassScope classScope = null;
//        //参数复制、局部变量定义
//        currentInitBlock = new BasicBlock("var_def");
//        //全局函数
//        if (currentScope instanceof GlobalScope) {
//            currentFunction = irRoot.getFunc(node.name);
//        }
//        //类成员函数
//        else {
//            classScope = (ClassScope) currentScope;
//            currentFunction = irRoot.getFunc(classScope.classType.name + "." + node.name);
//            //变量this.addr，为一个指向this指针的指针
//            currentInitBlock.pushBack(new Alloca(new PtrType(), "this.addr"));
//        }
//        //进入函数的第一个块为变量、参数初始化
//        currentFunction.entry=currentInitBlock;
//        //入参（参数表为VarDefUnitNode数组）
//        if (node.parameterList != null) {
//            node.parameterList.varDefUnitNodes.forEach(
//                    var -> currentInitBlock.pushBack(
//                            new Alloca(var.typeNode.accept(this).type, var.name + ".addr")
//                    )
//            );
//        }
//        //函数作用域
//        currentScope = node.scope;
//
//    }
//
//    /**
//     * IfStmtNode
//     * 条件跳转
//     * if.then,if.else,if.end
//     * 可能没有else
//     *
//     * @param node IfStmtNode
//     * @return null
//     */
//    @Override
//    public Entity visit(IfStmtNode node) {
//        int label = currentFunction.cnt++;
//        BasicBlock trueStmtBlock = new BasicBlock("if.then" + label);
//        BasicBlock falseStmtBlock = null;
//        BasicBlock endBlock = new BasicBlock("if.end" + label);
//        BasicBlock next = endBlock;
//        if (node.falseStatement != null) {
//            falseStmtBlock = new BasicBlock("if.else" + label);
//            next = falseStmtBlock;
//        }
//        //cond：在上一个块里
//        Entity entity = node.condition.accept(this);
//        currentBlock.pushBack(
//                new Branch(entity, trueStmtBlock, next)
//        );
//        currentBlock = trueStmtBlock;
//        node.trueStatement.accept(this);
//        currentBlock.pushBack(
//                new Jump(endBlock)
//        );
//        if (node.falseStatement != null) {
//            currentBlock = falseStmtBlock;
//            node.falseStatement.accept(this);
//            currentBlock.pushBack(
//                    new Jump(endBlock)
//            );
//        }
//        currentBlock = endBlock;
//        return null;
//    }
//
//    @Override
//    public Entity visit(ReturnStmtNode node) {
//
//    }
//
//    @Override
//    public Entity visit(VarDefStmtNode node) {
//
//    }
//
//    /**
//     * WhileStmtNode
//     * while循环作用域
//     * loop.cond,loop.body,loop.end
//     *
//     * @param node WhileStmtNode
//     * @return null
//     */
//    @Override
//    public Entity visit(WhileStmtNode node) {
//        currentScope = node.scope;
//        Entity entity = null;
//        //作为当前block的特殊标识符
//        LoopScope loopScope = (LoopScope) currentScope;
//        loopScope.label = currentFunction.cnt++;
//        BasicBlock condBlock = new BasicBlock("loop.cond" + loopScope.label);
//        BasicBlock bodyBlock = new BasicBlock("loop.body" + loopScope.label);
//        BasicBlock endBlock = new BasicBlock("loop.end" + loopScope.label);
//        currentBlock.pushBack(
//                new Jump(condBlock)
//        );
//        currentBlock = condBlock;
//        entity = node.condition.accept(this);
//        currentBlock.pushBack(
//                new Branch(entity, bodyBlock, endBlock)
//        );
//        currentBlock = bodyBlock;
//        if (node.statement != null) {
//            node.statement.accept(this);
//        }
//        currentBlock.pushBack(
//                new Jump(condBlock)
//        );
//        currentBlock = endBlock;
//        return null;
//    }
//
//    @Override
//    public Entity visit(ArrayVisExprNode node) {
//
//    }
//
//    /**
//     * AssignExprNode
//     * 赋值语句，左边为IR指针类
//     * 如果右边为Ptr，先load
//     * +----------------------------
//     * |    %0 = load i32, ptr %a, align 4
//     * |    store i32 %0, ptr %b,
//     * +---------------------------
//     *
//     * @param node AssignExprNode
//     * @return null
//     */
//    @Override
//    public Entity visit(AssignExprNode node) {
//        Entity left = node.lhs.accept(this);
//        Entity right = node.rhs.accept(this);
//        if (right instanceof Ptr) {
//            LocalTmpVar tmp = new LocalTmpVar(((Ptr) right).storage.type);
//            currentBlock.pushBack(
//                    new Load(tmp, (Ptr) right)
//            );
//            right = tmp;
//        }
//        currentBlock.pushBack(
//                new Store(right, left)
//        );
//        return null;
//    }
//
//    /**
//     * BinaryExprNode
//     * 二元算数计算
//     * 转化为binary指令
//     * 字面量\localTmpVar直接运算，
//     * 变量先load到localTmpVar  | %0 = load i32, ptr |
//     * 运算结果放到localTmpVar
//     *
//     * @param node BinaryExprNode
//     * @return result(localTmpVar)
//     */
//    @Override
//    public Entity visit(BinaryExprNode node) {
//        Entity left = node.lhs.accept(this);
//        Entity right = node.rhs.accept(this);
//        //处理ptr
//        if (left instanceof Ptr) {
//            LocalTmpVar tmp = new LocalTmpVar(((Ptr) left).storage.type);
//            currentBlock.pushBack(
//                    new Load(tmp, (Ptr) left)
//            );
//            left = tmp;
//        }
//        if (right instanceof Ptr) {
//            LocalTmpVar tmp = new LocalTmpVar(((Ptr) right).storage.type);
//            currentBlock.pushBack(
//                    new Load(tmp, (Ptr) right)
//            );
//            right = tmp;
//        }
//        //IR运算语句
//        LocalTmpVar result = new LocalTmpVar(left.type);
//        currentBlock.pushBack(
//                new Binary(node.operator,
//                        result,
//                        left,
//                        right
//                )
//        );
//        return result;
//    }
//
//    /**
//     * CmpExprNode
//     * 二元比较表达式
//     * 转化为icmp指令
//     * 字面量\localTmpVar直接运算，
//     * 变量先load到localTmpVar  | %0 = load i32, ptr |
//     * 运算结果放到localTmpVar
//     *
//     * @param node CmpExprNode
//     * @return result
//     */
//    @Override
//    public Entity visit(CmpExprNode node) {
//        Entity left = node.lhs.accept(this);
//        Entity right = node.rhs.accept(this);
//        //处理ptr
//        if (left instanceof Ptr) {
//            LocalTmpVar tmp = new LocalTmpVar(((Ptr) left).storage.type);
//            currentBlock.pushBack(
//                    new Load(tmp, (Ptr) left)
//            );
//            left = tmp;
//        }
//        if (right instanceof Ptr) {
//            LocalTmpVar tmp = new LocalTmpVar(((Ptr) right).storage.type);
//            currentBlock.pushBack(
//                    new Load(tmp, (Ptr) right)
//            );
//            right = tmp;
//        }
//        //IR比较语句
//        LocalTmpVar result = new LocalTmpVar(new IntType(IntType.TypeName.BOOL));
//        currentBlock.pushBack(
//                new Icmp(node.operator,
//                        result,
//                        left,
//                        right
//                )
//        );
//        return result;
//    }
//
//    @Override
//    public Entity visit(FuncCallExprNode node) {
//
//    }
//
//    @Override
//    public Entity visit(LogicExprNode node) {
//
//    }
//
//    @Override
//    public Entity visit(LogicPrefixExprNode node) {
//
//    }
//
//    @Override
//    public Entity visit(MemberVisExprNode node) {
//
//    }
//
//    @Override
//    public Entity visit(NewExprNode node) {
//
//    }
//
//    @Override
//    public Entity visit(PrefixExprNode node) {
//
//    }
//
//    @Override
//    public Entity visit(PointerExprNode node) {
//
//    }
//
//    @Override
//    public Entity visit(SuffixExprNode node) {
//
//    }
//
//    @Override
//    public Entity visit(TernaryExprNode node) {
//
//    }
//
//    /**
//     * VarNameExprNode
//     * 变量名localPtr、globalPtr
//     * 从rename2mem找到重命名后的变量ptr返回
//     * 不可以返回storage（可能是赋值语句的left）
//     * TODO:类成员、函数
//     *
//     * @param node VarNameExprNode
//     * @return ptr
//     */
//    @Override
//    public Entity visit(VarNameExprNode node) {
//
//        //该变量在当前的重命名
//        String name = varMap.get(node.name) + node.name;
//        return rename2mem.get(name);
//    }
//
//    /**
//     * BoolConstantExprNode
//     * bool类型的常量
//     *
//     * @param node BoolConstantExprNode
//     * @return constBool
//     */
//    @Override
//    public Entity visit(BoolConstantExprNode node) {
//        return new ConstBool(((BoolConstantExprNode) node).value);
//    }
//
//    /**
//     * IntConstantExprNode
//     * 整型常量
//     *
//     * @param node IntConstantExprNode
//     * @return constInt
//     */
//    @Override
//    public Entity visit(IntConstantExprNode node) {
//        return new ConstInt(((IntConstantExprNode) node).value);
//    }
//
//    /**
//     * NullConstantExprNode
//     * 空指针
//     *
//     * @param node NullConstantExprNode
//     * @return Null
//     */
//    @Override
//    public Entity visit(NullConstantExprNode node) {
//        return new Null();
//    }
//
//    /**
//     * StrConstantExprNode
//     *
//     * @param node StrConstantExprNode
//     * @return constString
//     */
//    @Override
//    public Entity visit(StrConstantExprNode node) {
//        return new ConstString(((StrConstantExprNode) node).value);
//    }
//
//    @Override
//    public Entity visit(ClassDefNode node) {
//
//    }
//
//    @Override
//    public Entity visit(InitNode node) {
//
//    }
//
//    @Override
//    public Entity visit(TypeNode node) {
//
//    }
//
//    /**
//     * VarDefUnitNode变量定义
//     * 开空间+可能有初始化,
//     * - 全局变量，加到IRRoot下
//     * - 局部变量:若被常量初始化，加入init
//     *
//     * @param node VarDefUnitNode
//     */
//    @Override
//    public Entity visit(VarDefUnitNode node) {
//        String name;
//        //entity为该类的0或null常量
//        Constant constant = (Constant) node.typeNode.accept(this);
//        Entity entity = null;
//        //global var
//        if (currentScope instanceof GlobalScope) {
//            name = rename(node.name);
//            //have init?
//            if (node.initExpr != null) {
//                //如果为字面量，返回constant,直接初始化
//                //否则返回在初始化函数中用赋值语句初始化
//                entity = node.initExpr.accept(this);
//                if (entity instanceof Constant) {
//                    constant = (Constant) entity;
//                }
//            }
//            //调用Global指令为全局变量分配空间
//            Global stmt = new Global(constant, name);
//            globalInitBlock.getFirst().pushBack(stmt);
//            //将rename -> mem映射存入map
//            rename2mem.put(name, stmt.result);
//            //非字面量初始化
//            //在全局的初始化函数中加入赋值语句
//            if (entity != null && !(entity instanceof Constant)) {
//                globalInitBlock.getSecond().pushBack(
//                        new Store(entity, stmt.result)
//                );
//            }
//        }
//        //类成员变量
//        //IRRoot中structType只有类名，在这里加入成员
//        //字面量初始化的在初始化函数中alloca赋值
//        //其余用赋值函数
//        else if (currentScope instanceof ClassScope) {
//            //TODO:类内变量，加到类的初始化函数里
//        } else {
//            name = rename(node.name);
//            //普通局部变量，Alloca分配空间
//            Alloca stmt = new Alloca(constant.type, name);
//            currentInitBlock.pushBack(stmt);
//            //将rename -> mem映射存入map
//            rename2mem.put(name, stmt.result);
//            //若有初始化语句，在走到该部分时用赋值语句
//            if (node.initExpr != null) {
//                node.initExpr.accept(this);
//            }
//        }
//        return null;
//    }
//}
