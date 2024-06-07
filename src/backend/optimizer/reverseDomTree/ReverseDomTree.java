package backend.optimizer.reverseDomTree;


import asm.Block;
import asm.Func;
import utility.error.InternalException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author F
 * reverse CFG的domTree
 * todo: ATTENTION to reverse
 */
public class ReverseDomTree {
    public ArrayList<ReverseDomTreeNode> reorderedNode;
    public int[] iDomArray;
    public HashMap<String, ReverseDomTreeNode> label2node = new HashMap<>();

    public ReverseDomTree(Func func) {
        setReorderedBlock(func);
        iDomArray = new int[reorderedNode.size()];
        buildReverseDomTree();
        for (int i = 0; i < reorderedNode.size(); ++i) {
            ReverseDomTreeNode father = reorderedNode.get(iDomArray[i]);
            ReverseDomTreeNode son = reorderedNode.get(i);
            son.iDom = father;
            //iDomArray[i] == i，iDom是自身
            if (iDomArray[i] != i) {
                father.successors.add(son);
            }
        }
        calDomFrontier();
    }

    private void setReorderedBlock(Func func) {
        reorderedNode = new ArrayList<>();
        for (Block block : func.reorderedBlock) {
            ReverseDomTreeNode node = new ReverseDomTreeNode(block);
            reorderedNode.add(node);
            label2node.put(block.name, node);
        }
    }

    private void buildReverseDomTree() {
        int size = reorderedNode.size();
        //初始化
        iDomArray[0] = 0;
        for (int i = 1; i < size; ++i) {
            iDomArray[i] = -1;
        }
        //迭代至收敛
        boolean changed = true;
        ReverseDomTreeNode currentNode;
        while (changed) {
            changed = false;
            for (int i = 1; i < size; ++i) {
                currentNode = reorderedNode.get(i);
                int successorSize = currentNode.block.successorList.size();
                if (successorSize == 0) {
                    throw new InternalException("unexpected rCFG: basic block has no successor");
                }
                int suc1, suc2;
                int newImmDom;
                int index = 0;
                while (index < successorSize
                        && iDomArray[currentNode.block.successorList.get(index).reversePostorder] == -1) {
                    ++index;
                }
                if (index == successorSize) {
                    throw new InternalException("unexpected rCFG: no successor updated");
                }
                newImmDom = suc1 = currentNode.block.successorList.get(index).reversePostorder;
                ++index;
                for (; index < successorSize; ++index) {
                    if (iDomArray[currentNode.block.successorList.get(index).reversePostorder] == -1) {
                        continue;
                    }
                    suc2 = currentNode.block.successorList.get(index).reversePostorder;
                    newImmDom = intersect(newImmDom, suc2);
                }
                if (iDomArray[i] != newImmDom) {
                    iDomArray[i] = newImmDom;
                    changed = true;
                }
            }
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

    private void calDomFrontier() {
        for (int i = 0; i < reorderedNode.size(); ++i) {
            ReverseDomTreeNode node = reorderedNode.get(i);
            if (node.block.successorList.size() >= 2) {
                for (int index = 0; index < node.block.successorList.size(); ++index) {
                    Block successor = node.block.successorList.get(index);
                    ReverseDomTreeNode current = reorderedNode.get(successor.reversePostorder);
                    while (current.block.reversePostorder != iDomArray[i]) {
                        current.domFrontier.add(node);
                        current = reorderedNode.get(iDomArray[current.block.reversePostorder]);
                    }
                }
            }
        }
    }
}
