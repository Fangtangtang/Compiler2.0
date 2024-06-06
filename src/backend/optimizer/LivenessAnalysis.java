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
    HashSet<Register> globalReg;

    public LivenessAnalysis(Func func) {
        this.func = func;
        setDefUse();
    }

    void setDefUse() {
        globalReg = new HashSet<>();
        Block block;
        for (int i = 0; i < func.funcBlocks.size(); i++) {
            block = func.funcBlocks.get(i);
            block.use = new HashSet<>();
            block.def = new HashSet<>();
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
            block.use.addAll(use);
            for (var usedReg : use) {
                if (!block.def.contains(usedReg)) {
                    globalReg.add(usedReg);
                }
            }
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
            for (int i = 1; i < func.reorderedBlock.size(); i++) {
                //updated with RPO
                Block currentBlock = func.reorderedBlock.get(i);
                HashSet<Register> tmp = new HashSet<>();
                Block successor;
                for (int j = 0; j < currentBlock.successorList.size(); j++) {
                    successor = currentBlock.successorList.get(j);
                    for (var usedReg : successor.use) {
                        if (globalReg.contains(usedReg)) {
                            tmp.add(usedReg);
                        }
                    }
                    for (Register reg : successor.liveOut) {
                        if (!successor.def.contains(reg) && globalReg.contains(reg)) {
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
