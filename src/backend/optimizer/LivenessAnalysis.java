package backend.optimizer;

import asm.Block;
import asm.Func;
import asm.instruction.*;
import asm.operand.Register;

import java.util.*;

/**
 * @author F
 * asm上的活跃度分析
 * 一一对函数操作
 */
public class LivenessAnalysis {
    Func func;

    public LivenessAnalysis(Func func) {
        this.func = func;
        setDefUse();
    }

    void setDefUse() {
        Block block;
        for (int i = 0; i < func.funcBlocks.size(); i++) {
            block = func.funcBlocks.get(i);
            for (int j = 0; j < block.instructions.size(); j++) {
                setDefUseOnInst(block.instructions.get(j), block);
            }
        }
    }

    void setDefUseOnInst(ASMInstruction inst, Block block) {
        Register def = inst.getDef();
        if (def != null) {
            block.def.add(def);
        }
        ArrayList<Register> use = inst.getUse();
        if (use != null) {
            use.forEach(
                    usedReg -> block.use.add(usedReg)
            );
        }
    }

    public void analysisLiveOut() {
        //init
        func.funcBlocks.forEach(
                block -> block.liveOut = new HashSet<>()
        );
        //迭代至收敛
        boolean changed = true;
        while (changed) {
            changed = false;
            //最后一个basic block的liveOut为空
            //TODO:which to update
//            for (int i = 1; i < func.reorderedBlock.size(); i++) {
            for (int i = func.reorderedBlock.size() - 2; i >= 0; i--) {
                //updated with RPO
                Block currentBlock = func.reorderedBlock.get(i);
                HashSet<Register> tmp = new HashSet<>();
                Block successor;
                for (int j = 0; j < currentBlock.successorList.size(); j++) {
//                for (int j = currentBlock.successorList.size() - 2; j >= 0; --j) {
                    successor = currentBlock.successorList.get(j);
                    tmp.addAll(successor.use);
                    for (Register reg : successor.liveOut) {
                        if (!successor.def.contains(reg)) {
                            tmp.add(reg);
                        }
                    }
                }
                if (!tmp.equals(currentBlock.liveOut)) {
                    changed = true;
                    currentBlock.liveOut = tmp;
                }
            }
        }
    }
}
