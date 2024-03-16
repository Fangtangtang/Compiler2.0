package ir.function;

import ir.BasicBlock;
import ir.IRVisitor;
import ir.entity.*;
import ir.entity.var.*;
import ir.irType.*;
import utility.dominance.DomTree;
import utility.dominance.DomTreeNode;

import java.io.PrintStream;
import java.util.*;

/**
 * @author F
 * 定义在全局的函数、类的函数、内建函数
 */
public class Function {
    //返回类型
    public IRType retType;
    //函数名
    public String funcName;
    //参数表，无法在函数中直接使用的量
    //var_def块中alloca新局部变量，将这些store
    public ArrayList<LocalTmpVar> parameterList = new ArrayList<>();
    public LocalVar retVal;
    //进入函数的第一个basic block
    public BasicBlock entry = null;
    //每个函数以自己的return块结尾
    public BasicBlock ret;
    public LinkedHashMap<String, BasicBlock> blockMap = new LinkedHashMap<>();
    //按照RPO序排列的basic block
    public ArrayList<DomTreeNode> reorderedBlock = new ArrayList<>();
    public DomTree domTree;
    public HashMap<String, Storage> phiResult = new HashMap<>();

    public HashMap<Function, Integer> calleeMap = null;

    public HashSet<String> inlinedToCaller = new HashSet<>();

    public Function(IRType retType,
                    String funcName) {
        this.ret = new BasicBlock(funcName + "_return");
        this.retType = retType;
        this.retVal = new LocalVar(new Storage(retType), funcName+"_retVal");
        this.funcName = funcName;
    }

    //用于global_var_init
    //没有返回值，没有return
    public Function(IRType retType,
                    String funcName,
                    BasicBlock entryBlock) {
        this.retType = retType;
        this.funcName = funcName;
        this.entry = entryBlock;
        blockMap.put(entryBlock.label, entryBlock);
    }

    public String printParameterList() {
        StringBuilder str = new StringBuilder("(");
        if (parameterList.size() > 0) {
            str.append(parameterList.get(0).type).append(" ").append(parameterList.get(0));
        }
        for (int i = 1; i < parameterList.size(); ++i) {
            str.append(", ").append(parameterList.get(i).type).append(" ")
                    .append(parameterList.get(i).toString());
        }
        str.append(")");
        return str.toString();
    }

    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }
}
