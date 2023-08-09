package ir.function;

import ir.BasicBlock;
import ir.irType.VoidType;

/**
 * @author F
 * main函数，运行的主体
 * （在main前或刚进入main时运行全局变量的初始化函数）
 */
public class MainFunc extends Function {
    public MainFunc(BasicBlock entryBlock) {
        super(new VoidType(), "main", entryBlock);
    }
}
