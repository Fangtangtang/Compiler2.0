package midend.optimizer;

import ir.*;
import ir.entity.*;
import ir.function.Function;

import java.util.*;

/**
 * @author F
 * ir上LocalTmpVar仅def一次，use不超过一次
 * 将LocalTmpVar传播，仅def一次，但可以use多次
 * - 减少对LocalVar和GlobalVar的使用，部分store型成为死代码，为常量传播和死代码消除准备
 * - 减轻后端图染色压力
 */
public class LocalTmpVarPropagation {
    IRRoot irRoot;

    public LocalTmpVarPropagation(IRRoot root) {
        this.irRoot = root;
    }

    /**
     * 执行优化
     * --------------------------------
     * 对每个local函数的每个BB做一次传播
     */
    public void execute() {
        for (Map.Entry<String, Function> entry : irRoot.funcDef.entrySet()) {
            Function func = entry.getValue();
            for (Map.Entry<String, BasicBlock> blockEntry : func.blockMap.entrySet()) {
                BasicBlock block = blockEntry.getValue();
                executeOnBasicBlock(block);
            }
        }
    }

    /**
     * LocalTmpVar存活于单个BB
     * BB内顺序执行，保证传播正确性
     */
    public void executeOnBasicBlock(BasicBlock block) {
        // 如果有对Ptr的赋值，记在valueInBasicBlock内
        //如果有Ptr的值的使用（load），且valueInBasicBlock不为null，则可以将使用替换
    }


}
