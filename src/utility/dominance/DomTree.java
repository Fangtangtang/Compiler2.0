package utility.dominance;

import ir.BasicBlock;
import ir.function.Function;
import utility.error.InternalException;

import java.util.*;


/**
 * @author F
 * 由CFG构造的支配树
 * 对basic block信息的补充
 */
public class DomTree {
    public ArrayList<DomTreeNode> reorderedBlock;
    public int[] iDomArray;

    public DomTree(Function function) {
        reorderedBlock = function.reorderedBlock;
        //计算RPO
        setReorderedBlock(function.entry);
        //计算iDomArray(iDom RPO order)，构建Dom Tree
        iDomArray = new int[reorderedBlock.size()];
        buildDomTree();
        for (int i = 0; i < reorderedBlock.size(); ++i) {
            DomTreeNode father = reorderedBlock.get(iDomArray[i]);
            DomTreeNode son = reorderedBlock.get(i);
            son.iDom = father;
            //iDomArray[i] == i，iDom是自身
            if (iDomArray[i] != i) {
                father.successors.add(son);
            }
        }
        //计算df
        calDomFrontier();
    }

    // 计算 post order
    HashSet<String> vis = new HashSet<>();
    ArrayList<BasicBlock> postorder = new ArrayList<>();

    private void dfs(BasicBlock block) {
        vis.add(block.label);
        //先遍历未遍历到的儿子
        block.successorList.forEach(
                successor -> {
                    if (!vis.contains(successor.label)) {
                        dfs(successor);
                    }
                }
        );
        postorder.add(block);
    }

    /**
     * 先后序遍历、再逆序遍历
     * 获得reverse postorder
     *
     * @param entryBlock CFG 入口结点
     */
    private void setReorderedBlock(BasicBlock entryBlock) {
        dfs(entryBlock);
        int maxIndex = postorder.size() - 1;
        for (int index = maxIndex; index >= 0; --index) {
            BasicBlock block = postorder.get(index);
            DomTreeNode node = new DomTreeNode(block);
            reorderedBlock.add(node);
            block.reversePostorder = maxIndex - index;
        }
    }

    //获得b1、b2在当前dom tree上的lca
    //b1、b2：当前结点的两个前驱的RPO
    private int intersect(int b1, int b2) {
        while (b1 != b2) {
            while (b1 > b2) {
                b1 = iDomArray[b1];
            }
            while (b2 > b1) {
                b2 = iDomArray[b2];
            }
        }
        return b1;
    }

    // todo:incorrect
    private void buildDomTree() {
        int size = reorderedBlock.size();
        //初始化
        iDomArray[0] = 0;
        for (int i = 1; i < size; ++i) {
            iDomArray[i] = -1;
        }
        //迭代至收敛
        boolean changed = true;
        DomTreeNode currentNode;
        while (changed) {
            changed = false;
            for (int i = 1; i < size; ++i) {
                currentNode = reorderedBlock.get(i);
                int predecessorSize = currentNode.block.predecessorList.size();
                if (predecessorSize == 0) {
                    throw new InternalException("unexpected CFG: basic block has no predecessor");
                }
                int pred1, pred2;
                int newImmDom;
                int index = 0;// index of the first processed predecessor in predecessorList
                //the first processed predecessor
                while (index < predecessorSize
                        && iDomArray[currentNode.block.predecessorList.get(index).reversePostorder] == -1) {
                    ++index;
                }
                if (index == predecessorSize) {
                    throw new InternalException("unexpected CFG: no predecessor updated");
                }
                newImmDom = pred1 = currentNode.block.predecessorList.get(index).reversePostorder;
                ++index;
                for (; index < predecessorSize; ++index) {
                    if (iDomArray[currentNode.block.predecessorList.get(index).reversePostorder] == -1) {
                        continue;
                    }
                    pred2 = currentNode.block.predecessorList.get(index).reversePostorder;
                    newImmDom = intersect(newImmDom, pred2);
                }
                if (iDomArray[i] != newImmDom) {
                    iDomArray[i] = newImmDom;
                    changed = true;
                }
            }
        }
    }

    private void calDomFrontier() {
        for (int i = 0; i < reorderedBlock.size(); ++i) {
            DomTreeNode node = reorderedBlock.get(i);
            if (node.block.predecessorList.size() >= 2) {
                for (int index = 0; index < node.block.predecessorList.size(); ++index) {
                    BasicBlock predecessor = node.block.predecessorList.get(index);
                    DomTreeNode current = reorderedBlock.get(predecessor.reversePostorder);
                    while (current.block.reversePostorder != iDomArray[i]) {
                        current.domFrontier.add(node);
                        current = reorderedBlock.get(iDomArray[current.block.reversePostorder]);
                    }
                }
            }
        }
    }
}
