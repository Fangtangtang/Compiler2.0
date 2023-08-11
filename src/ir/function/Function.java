package ir.function;

import ir.BasicBlock;
import ir.entity.Entity;
import ir.entity.Storage;
import ir.entity.var.LocalVar;
import ir.entity.var.Ptr;
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
    //参数表
    public ArrayList<Entity> parameterList = new ArrayList<>();
    public LocalVar retVal;
    public BasicBlock entry = null;
    //每个函数以自己的return块结尾
    public BasicBlock ret = new BasicBlock("return");
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
