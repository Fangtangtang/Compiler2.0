package backend.optimizer.reverseDomTree;

import asm.Block;

import java.util.ArrayList;

/**
 * @author F
 * reverse CFG的domTree上结点
 */
public class ReverseDomTreeNode {
    public Block block;

    //immediate dominator
    public ReverseDomTreeNode iDom;
    public ArrayList<ReverseDomTreeNode> successors = new ArrayList<>();
    //dominance frontier
    public ArrayList<ReverseDomTreeNode> domFrontier = new ArrayList<>();

    public ReverseDomTreeNode(Block block) {
        this.block = block;
    }

}
