package utility.dominance;

import ir.BasicBlock;

import java.util.ArrayList;
import java.util.HashSet;

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
    public ArrayList<DomTreeNode> successors;
    //dominance frontier
    public ArrayList<DomTreeNode> domFrontier = new ArrayList<>();

    //在当前结点必须phi的变量名
    public HashSet<String> phiFuncDef = new HashSet<>();

    public DomTreeNode(BasicBlock block) {
        this.block = block;
    }
}
