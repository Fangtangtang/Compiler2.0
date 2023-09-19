package backend.optimizer;

import asm.*;
import asm.instruction.*;
import asm.section.Text;

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
                    setReorderedBlock(func);
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
            ASMInstruction instruction = block.instructions.get(
                    block.instructions.size() - 1
            );
            Block target = null;
            if (instruction instanceof JumpInst jumpInst) {
                target = blockMap.get(jumpInst.desName);
            } else if (instruction instanceof BranchInst branchInst) {
                target = blockMap.get(branchInst.desName);
            } else {
                if (i + 1 < func.funcBlocks.size()) {
                    target = func.funcBlocks.get(i + 1);
                }
            }
            if (target != null) {
                block.successorList.add(target);
                target.predecessorList.add(block);
            }
        }
    }

    private void setReorderedBlock(Func func) {
        postorder = new ArrayList<>();
        vis = new HashSet<>();
        dfs(func.funcBlocks.get(0));
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
        block.successorList.forEach(
                successor -> {
                    if (!vis.contains(successor.name)) {
                        dfs(successor);
                    }
                }
        );
        postorder.add(block);
    }
}
