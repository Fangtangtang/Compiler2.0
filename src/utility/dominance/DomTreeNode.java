package utility.dominance;

import ir.BasicBlock;

import java.util.ArrayList;

/**
 * @author F
 * dom tree的结点
 * 在basic block之外加了
 * - immediate dominator
 * - dominance frontier
 */
public class DomTreeNode {
    //对应的basic block
    public BasicBlock block;

    //immediate dominator
    public DomTreeNode iDom;

    //dominance frontier
    public ArrayList<DomTreeNode> domFrontier = new ArrayList<>();

    public DomTreeNode(BasicBlock block) {
        this.block = block;
    }
}
