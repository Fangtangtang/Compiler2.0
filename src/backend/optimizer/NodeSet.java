package backend.optimizer;

import backend.optimizer.interferenceGraph.*;

import java.util.HashSet;

/**
 * @author F
 * 冲突图结点集合
 */
public class NodeSet {
    //预着色结点集合
    public HashSet<PrecoloredNode> precolored = new HashSet<>();
    //临时，未被着色、未被处理
    public HashSet<UncoloredNode> initial = new HashSet<>();

    //workList
    //低度数、传送无关
    public HashSet<UncoloredNode> simplifyWorkList = new HashSet<>();
    //低度数、传送相关
    public HashSet<UncoloredNode> freezeWorkList = new HashSet<>();
    //高度数
    public HashSet<UncoloredNode> spillWorkList = new HashSet<>();

    //将在本轮中spill
    public HashSet<UncoloredNode> spilledNodes = new HashSet<>();
    //已合并结点(u <- v : add v to set)
    public HashSet<UncoloredNode> coalescedNodes = new HashSet<>();


}
