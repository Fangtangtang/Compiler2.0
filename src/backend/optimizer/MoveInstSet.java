package backend.optimizer;

import asm.instruction.*;

import java.util.HashSet;

/**
 * @author F
 * 传送指令集合
 */
public class MoveInstSet {
    //已经尝试合并的move
    //已经合并的
    public HashSet<MoveInst> coalescedMoves = new HashSet<>();
    //source和target冲突
    public HashSet<MoveInst> constrainedMoves = new HashSet<>();

    //尚未尝试合并的move
    //冻结，不再考虑合并的move
    public HashSet<MoveInst> frozenMoves = new HashSet<>();
    //可能合并
    public HashSet<MoveInst> workListMoves = new HashSet<>();
    //尚未做好合并准备
    public HashSet<MoveInst> activeMoves = new HashSet<>();


}
