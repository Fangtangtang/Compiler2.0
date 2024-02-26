package ir.function;

import ir.BasicBlock;
import ir.irType.VoidType;
import utility.Counter;

/**
 * @author F
 * 全局变量初始化函数
 * 特殊函数，零散遍布全局
 * 没有return，可能有跳转（三目运算初始化）
 */
public class GlobalVarInitFunction extends Function {
    public BasicBlock currentBlock;

    public Counter tmpCounter = new Counter();
    public Counter phiCounter = new Counter();

    public GlobalVarInitFunction() {
        super(new VoidType(),
                "global-var-init",
                new BasicBlock("global-var-init_start")
        );
        currentBlock = this.entry;
    }

    public boolean isEmpty() {
        //至少有ret
        if (blockMap.size() > 1) {
            return false;
        }
        return currentBlock.statements.size() == 0;
    }
}
