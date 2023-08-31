package utility;

import ir.BasicBlock;

import java.util.ArrayList;

/**
 * @author F
 * 由CFG构造的支配树
 * 对basic block信息的补充
 */
public class DomTree {
    //按照RPO序排列的basic block
    public ArrayList<BasicBlock> reorderedBlock = new ArrayList<>();
    public ArrayList<Integer> iDom = new ArrayList<>();
}
