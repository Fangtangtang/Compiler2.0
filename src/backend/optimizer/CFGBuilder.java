package backend.optimizer;

import asm.*;
import asm.instruction.*;
import asm.section.Text;
import utility.error.InternalException;

import java.util.*;

/**
 * @author F
 * asm上CFGBuilder
 */
public class CFGBuilder {
    //所有自定义的函数
    Text text;

    HashMap<String, Block> blockMap;

    public CFGBuilder(Text text) {
        this.text = text;
    }

    public void build() {
        text.functions.forEach(
                func -> {
                    buildOnFunction(func);
                    setReorderedBlockOnReverse(func);
                }
        );
    }

    private void buildOnFunction(Func func) {
        blockMap = new HashMap<>();
        func.funcBlocks.forEach(
                block -> blockMap.put(block.name, block)
        );
        for (int i = 0; i < func.funcBlocks.size(); i++) {
            Block block = func.funcBlocks.get(i);
            if (block.instructions.size() == 0) {
                continue;
            }
            boolean flag = false;
            for (var instruction : block.controlInstructions) {
                Block target = null;
                if (instruction instanceof JumpInst jumpInst) {
                    flag = true;
                    target = blockMap.get(jumpInst.desName);
                } else if (instruction instanceof BranchInst branchInst) {
                    target = blockMap.get(branchInst.desName);
                }
                if (target != null) {
                    block.successorList.add(target);
                    target.predecessorList.add(block);
                } else {
                    throw new InternalException("unexpected control inst");
                }
            }
            if (!flag && i + 1 < func.funcBlocks.size()) {
                Block target = func.funcBlocks.get(i + 1);
                block.successorList.add(target);
                target.predecessorList.add(block);
            }
        }
    }

    private void setReorderedBlockOnReverse(Func func) {
        postorder = new ArrayList<>();
        vis = new HashSet<>();
        dfs(func.funcBlocks.get(func.funcBlocks.size() - 1));
        int maxIndex = postorder.size() - 1;
        for (int index = maxIndex; index >= 0; --index) {
            Block block = postorder.get(index);
            func.reorderedBlock.add(block);
            block.reversePostorder = maxIndex - index;
        }
    }

    ArrayList<Block> postorder;
    HashSet<String> vis;

    private void dfs(Block block) {
        vis.add(block.name);
        block.predecessorList.forEach(
                pred -> {
                    if (!vis.contains(pred.name)) {
                        dfs(pred);
                    }
                }
        );
        postorder.add(block);
    }
}
