package ir.function;

import ir.BasicBlock;
import ir.entity.Storage;
import ir.entity.constant.ConstInt;
import ir.entity.var.LocalTmpVar;
import ir.entity.var.LocalVar;
import ir.irType.IntType;
import ir.irType.VoidType;
import ir.stmt.instruction.Load;
import ir.stmt.terminal.Return;

/**
 * @author F
 * main函数，运行的主体
 * 默认返回值0，在进入main时，为默认返回值开空间
 * （在main前或刚进入main时运行全局变量的初始化函数）
 */
public class MainFunc extends Function {

    public MainFunc() {
        super(new IntType(IntType.TypeName.INT), "main");
        this.retVal.storage = new ConstInt("0");
    }

    public MainFunc(BasicBlock entryBlock) {
        super(new IntType(IntType.TypeName.INT), "main", entryBlock);
    }
}
