package ir.function;

import ir.BasicBlock;
import ir.entity.register.*;
import ir.irType.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * @author F
 * 定义在全局的函数、类的函数、内建函数
 */
public class Function {
    //返回值类型
    public IRType retType;
    //返回值存放位置
    public Register retReg = null;
    //函数名
    public String funcName;
    //参数表
    public ArrayList<Register> parameterList = new ArrayList<>();

    public BasicBlock entry;
    public LinkedHashMap<String, BasicBlock> blockMap = new LinkedHashMap<>();

    public Function(IRType retType,
                    String funcName,
                    BasicBlock entryBlock) {
        this.retType = retType;
        this.funcName = funcName;
        this.entry = entryBlock;
        blockMap.put(entryBlock.label, entryBlock);
    }
}
