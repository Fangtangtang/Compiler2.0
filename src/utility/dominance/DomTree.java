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
    //按照RPO序排列的basic block
    public ArrayList<DomTreeNode> reorderedBlock = new ArrayList<>();
    public int[] iDomArray;

    public DomTree(Function function) {
        //计算RPO
        setReorderedBlock(function.entry);
        //计算iDomArray，构建Dom Tree
        iDomArray = new int[reorderedBlock.size()];
        buildDomTree();
        for (int i = 0; i < reorderedBlock.size(); ++i) {
            reorderedBlock.get(i).iDom = reorderedBlock.get(iDomArray[i]);
        }
        //计算df
        calDomFrontier();
    }

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
            reorderedBlock.add(
                    new DomTreeNode(block)
            );
            block.reversePostorder = maxIndex - index;
        }
    }

    //获得b1、b2在当前dom tree上的lca
    //b1、b2：当前结点的两个前驱的RPO
    private int intersect(int b1, int b2) {
        while (b1 != b2) {
            while (b1 < b2) {
                b1 = iDomArray[b1];
            }
            while (b2 < b1) {
                b2 = iDomArray[b2];
            }
        }
        return b1;
    }

    private void buildDomTree() {
        int size = reorderedBlock.size();
        //初始化
        iDomArray[0] = 0;
//        if (size == 1) {
//            return;
//        }
        for (int i = 1; i < size; ++i) {
            iDomArray[i] = -1;
        }
        boolean[] fullyUpdated = new boolean[size];
        fullyUpdated[0] = true;
        for (int i = 1; i < size; ++i) {
            fullyUpdated[i] = false;
        }
        //迭代至收敛
        boolean changed = true;
        DomTreeNode currentNode;
        while (changed) {
            changed = false;
            for (int i = 1; i < size; ++i) {
                //不需要在迭代中更新
                if (fullyUpdated[i]) {
                    continue;
                }
                currentNode = reorderedBlock.get(i);
                int predecessorSize = currentNode.block.predecessorList.size();
                if (predecessorSize == 0) {
                    throw new InternalException("unexpected CFG: basic block has no predecessor");
                }
                int pred1, pred2;
                int newImmDom;
                int index = 0;
                while (index < predecessorSize
                        && iDomArray[currentNode.block.predecessorList.get(index).reversePostorder] == -1) {
                    ++index;
                }
                if (index == predecessorSize) {
                    throw new InternalException("unexpected CFG: no predecessor updated");
                }
                //第一个被更新过的前驱
                newImmDom = pred1 = index;
                fullyUpdated[i] = fullyUpdated[index];
                ++index;
                for (; index < predecessorSize; ++index) {
                    if (iDomArray[currentNode.block.predecessorList.get(index).reversePostorder] == -1) {
                        continue;
                    }
                    pred2 = index;
                    fullyUpdated[i] = fullyUpdated[i] && fullyUpdated[index];
                    newImmDom = intersect(pred1, pred2);
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
