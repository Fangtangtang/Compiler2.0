package ir.function;

import ir.BasicBlock;
import ir.entity.Entity;
import ir.entity.var.Ptr;
import ir.irType.*;

import java.util.*;

/**
 * @author F
 * 定义在全局的函数、类的函数、内建函数
 */
public class Function {
    //返回值类型
    public IRType retType;
    //函数名
    public String funcName;
    //参数表
    public ArrayList<Entity> parameterList = new ArrayList<>();
    //计数，确保block不重名
    public Integer cnt = 0;
    public BasicBlock entry = null;
    public HashMap<String, BasicBlock> blockMap = new HashMap<>();

    public Function(IRType retType,
                    String funcName) {
        this.retType = retType;
        this.funcName = funcName;
    }

    public Function(IRType retType,
                    String funcName,
                    BasicBlock entryBlock) {
        this.retType = retType;
        this.funcName = funcName;
        this.entry = entryBlock;
        blockMap.put(entryBlock.label, entryBlock);
    }
}
