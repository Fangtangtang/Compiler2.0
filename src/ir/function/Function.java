package ir.function;

import ir.BasicBlock;
import ir.entity.*;
import ir.entity.var.*;
import ir.irType.*;

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
    public ArrayList<LocalVar> parameterList = new ArrayList<>();
    public LocalVar retVal;
    public BasicBlock entry = null;
    //每个函数以自己的return块结尾
    public BasicBlock ret = new BasicBlock("return");
    //存放一些需要使用名字索引的block
    public HashMap<String, BasicBlock> blockMap = new HashMap<>();

    public Function(String funcName){
        blockMap.put("return",ret);
        this.funcName=funcName;
    }

    public Function(IRType retType,
                    String funcName) {
        blockMap.put("return",ret);
        this.retType=retType;
        this.funcName = funcName;
    }

    public Function(IRType retType,
                    String funcName,
                    BasicBlock entryBlock) {
        blockMap.put("return",ret);
        this.retType=retType;
        this.funcName = funcName;
        this.entry = entryBlock;
        blockMap.put(entryBlock.label, entryBlock);
    }
}
