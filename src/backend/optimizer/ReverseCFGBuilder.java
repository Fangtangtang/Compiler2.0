package backend.optimizer;

import asm.*;
import asm.section.Text;

import java.util.*;

/**
 * @author F
 * asm上反向CFGBuilder
 */
public class ReverseCFGBuilder {
    //所有自定义的函数
    Text text;

    public ReverseCFGBuilder(Text text) {
        this.text = text;
    }

    public void build() {
        text.functions.forEach(
                func -> {
                    func.constructGenealogy();
                    setReorderedBlockOnReverse(func);
                }
        );
    }

    private void setReorderedBlockOnReverse(Func func) {
        func.reorderedBlock=new ArrayList<>();
        postorder = new ArrayList<>();
        vis = new HashSet<>();
        dfs(func.retBlock);
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
                predecessor -> {
                    if (!vis.contains(predecessor.name)) {
                        dfs(predecessor);
                    }
                }
        );
        postorder.add(block);
    }
}
