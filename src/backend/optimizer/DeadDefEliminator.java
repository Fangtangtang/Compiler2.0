package backend.optimizer;

import asm.Block;
import asm.Func;
import asm.instruction.ASMInstruction;
import asm.instruction.BranchInst;
import asm.instruction.JumpInst;
import asm.instruction.MoveInst;
import backend.optimizer.interferenceGraph.Colors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.ListIterator;

/**
 * @author F
 * 主要在BB范围内，消除一些没有use的def
 * 注：此时已经全是physical register
 */
public class DeadDefEliminator {
    Func func;

    public DeadDefEliminator(Func func) {
        this.func = func;
    }

    public void execute() {
        livenessAnalysis();
        for (Block block : func.funcBlocks) {
            eliminateInBlock(block);
        }
    }

    // 反向记录block的活跃变量
    private void livenessAnalysis() {
        // todo
    }

    /**
     * 删去没有use的def
     * - 在被使用前已经被覆写
     * - todo...
     *
     * @param block func.block
     */
    private void eliminateInBlock(Block block) {
        Colors usedColors = new Colors();
        ListIterator<ASMInstruction> iterator =
                block.instructions.listIterator(block.instructions.size());
        // 从尾部向前遍历链表
        while (iterator.hasPrevious()) {
            ASMInstruction currentInst = iterator.previous();
            Colors.Color def = ASMInstruction.getDefPhysical(currentInst);
            if (def != null) {
                if (!usedColors.available.contains(def)) {
                    iterator.remove();
                    continue;
                } else {
                    usedColors.available.remove(def);
                }
            }
            ArrayList<Colors.Color> use = ASMInstruction.getUsePhysical(currentInst);
            usedColors.available.addAll(use);
        }
    }
}
