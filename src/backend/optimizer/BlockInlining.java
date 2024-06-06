package backend.optimizer;

import asm.Block;
import asm.Func;
import asm.instruction.ASMInstruction;
import asm.instruction.BranchInst;
import asm.instruction.JumpInst;
import asm.section.Text;

import java.util.*;

/**
 * @author F
 * BlockInlining
 * <p>
 * 若Block仅有一个后继，直接将后继内联
 * （在asm上做这个免除ir上phi和用于标记的控制流的复杂处理）
 */
public class BlockInlining {
    Text text;

    public BlockInlining(Text text) {
        this.text = text;
    }

    public void execute() {
        text.functions.forEach(
                func -> {
                    inliningOnFunc(func);
                    eliminateDeadBlock(func);
                }
        );
    }

    private void inliningOnFunc(Func func) {
        // construct graph
        func.constructGenealogy();
        HashSet<Block> workList = new HashSet<>();
        HashSet<String> vis = new HashSet<>();
        workList.add(func.entryBlock);
        while (!workList.isEmpty()) {
            Block block = workList.iterator().next();
            workList.remove(block);
            vis.add(block.name);
            // one successor
            if (block.successorList.size() == 1) {
                Block successor = block.successorList.get(0);
                if (successor == func.retBlock) {
                    continue;
                }
                // todo:避免代码过分膨胀(可否放松？)
                if (successor.predecessorList.size() > 1) {
                    if (!vis.contains(successor.name)) {
                        workList.add(successor);
                    }
                    continue;
                }
                ListIterator<ASMInstruction> iterator =
                        block.instructions.listIterator(block.instructions.size());
                // 从尾部向前遍历链表
                while (iterator.hasPrevious()) {
                    ASMInstruction currentInst = iterator.previous();
                    if (!(currentInst instanceof BranchInst ||
                            currentInst instanceof JumpInst)) {
                        iterator.next();
                        break;
                    } else {
                        iterator.remove();
                    }
                }
                block.instructions.addAll(successor.instructions);
                block.controlInstructions = successor.controlInstructions;
                block.successorList = successor.successorList;
                workList.add(block);
            } else {
                for (Block bb : block.successorList) {
                    if (!vis.contains(bb.name)) {
                        workList.add(bb);
                    }
                }
            }
        }
    }

    /**
     * [def] dead block
     * - unreachable
     *
     * @param func asm function
     */
    private void eliminateDeadBlock(Func func) {
        HashSet<Block> aliveBlocks = new HashSet<>();
        Queue<Block> queue = new LinkedList<>();
        queue.add(func.entryBlock);
        aliveBlocks.add(func.entryBlock);
        while (!queue.isEmpty()) {
            Block block = queue.remove();
            aliveBlocks.add(block);
            for (Block successor : block.successorList) {
                if (!aliveBlocks.contains(successor)) {
                    queue.add(successor);
                }
            }
        }
        func.funcBlocks = new ArrayList<>();
        func.funcBlocks.add(func.entryBlock);
        aliveBlocks.remove(func.entryBlock);
        func.funcBlocks.addAll(aliveBlocks);
    }

}
