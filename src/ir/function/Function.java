package ir.function;

import ir.BasicBlock;
import ir.entity.*;
import ir.entity.var.*;
import ir.irType.*;
import ir.stmt.instruction.Load;
import ir.stmt.terminal.Return;

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
    public BasicBlock ret;
    public LinkedHashMap<String, BasicBlock> blockMap = new LinkedHashMap<>();

    public Function(IRType retType,
                    String funcName) {
        this.ret = new BasicBlock(funcName + "_return");
        this.retType = retType;
        this.retVal = new LocalVar(new Storage(retType), "retVal");
        this.funcName = funcName;
        if (retType instanceof VoidType) {
            ret.pushBack(
                    new Return()
            );
        } else {
            LocalTmpVar tmp = new LocalTmpVar(retType);
            ret.pushBack(
                    new Load(tmp, this.retVal)
            );
            ret.pushBack(
                    new Return(tmp)
            );
        }

    }

    public Function(IRType retType,
                    String funcName,
                    BasicBlock entryBlock) {
        this.ret = new BasicBlock(funcName + "_return");
        this.retType = retType;
        this.retVal = new LocalVar(new Storage(retType), "retVal");
        this.funcName = funcName;
        this.entry = entryBlock;
        blockMap.put(entryBlock.label, entryBlock);
        if (retType instanceof VoidType) {
            ret.pushBack(
                    new Return()
            );
        } else {
            LocalTmpVar tmp = new LocalTmpVar(retType);
            ret.pushBack(
                    new Load(tmp, this.retVal)
            );
            ret.pushBack(
                    new Return(tmp)
            );
        }
    }
}
