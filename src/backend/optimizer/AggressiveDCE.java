package backend.optimizer;

import asm.Block;
import asm.Func;
import asm.instruction.ASMInstruction;
import asm.instruction.BranchInst;
import asm.instruction.JumpInst;
import asm.operand.Register;
import backend.optimizer.reverseDomTree.ReverseDomTree;
import backend.optimizer.reverseDomTree.ReverseDomTreeNode;
import utility.error.InternalException;

import java.util.*;

/**
 * @author F
 * Aggressive Dead Code Elimination
 */
public class AggressiveDCE {
    Func func;

    HashSet<ASMInstruction> liveInst;

    HashMap<String, Block> liveBlock;

    HashMap<Register, ASMInstruction> def2Inst;
    HashMap<ASMInstruction, Block> inst2Block;
    HashSet<ASMInstruction> workList;

    ReverseDomTree domTree;

    public AggressiveDCE(Func func) {
        this.func = func;
    }

    public void execute() {
        domTree = new ReverseDomTree(func);
        initWorkList();
        // 迭代工作表至收敛
        while (!workList.isEmpty()) {
            ASMInstruction inst = workList.iterator().next();
            workList.remove(inst);
            liveInst.add(inst);
            Block instBlock = inst2Block.get(inst);
            if (!liveBlock.containsKey(instBlock.name)) {
                liveBlock.put(instBlock.name, instBlock);
                // 反图中instBlock反向支配的也活跃 DF(edge in CDG)
                ReverseDomTreeNode node = domTree.label2node.get(instBlock.name);
                for (ReverseDomTreeNode predecessor : node.domFrontier) {
                    for (ASMInstruction instruction :
                            predecessor.block.controlInstructions) {
                        if (!liveInst.contains(instruction)) {
                            workList.add(instruction);
                        }
                    }
                }
            }
            ArrayList<Register> use = inst.getUse();
            // use变量也为live，对这些变量定值的也为live
            if (use != null) {
                for (Register usedReg : use) {
                    ASMInstruction defInst = def2Inst.get(usedReg);
                    if (!liveInst.contains(defInst)) {
                        workList.add(defInst);
                    }
                }
            }
        }
        // 仅保留活跃指令
        eliminate();
    }

    /**
     * 初始工作表，存放一些天然live的
     * (Note: dead block eliminated before this)
     * - Store
     * - func Call
     * - func Ret
     */
    void initWorkList() {
        liveInst = new HashSet<>();
        liveBlock = new HashMap<>();
        workList = new HashSet<>();
        def2Inst = new HashMap<>();
        inst2Block = new HashMap<>();
        liveBlock.put(func.retBlock.name, func.retBlock);
        ReverseDomTreeNode node = domTree.label2node.get(func.retBlock.name);
        for (ReverseDomTreeNode predecessor : node.domFrontier) {
            for (ASMInstruction instruction :
                    predecessor.block.controlInstructions) {
                if (!liveInst.contains(instruction)) {
                    workList.add(instruction);
                }
            }
        }
        for (Block block : func.funcBlocks) {
            for (ASMInstruction inst : block.instructions) {
                inst2Block.put(inst, block);
                Register def = inst.getDef();
                if (def != null) {
                    if (def2Inst.containsKey(def)){
                        throw new InternalException("[ADCE]: not ssa!!");
                    }
                    def2Inst.put(def, inst);
                }
                if (inst.isAliveByNature()) {
                    workList.add(inst);
                }
            }
        }
    }

    /**
     * 构建新的blockList
     */
    void eliminate() {
        ArrayList<Block> newBlockList = new ArrayList<>();
        HashSet<String> to = new HashSet<>();
        // live block
        for (Map.Entry<String, Block> entry : liveBlock.entrySet()) {
            Block block = entry.getValue();
            // leave live inst
            LinkedList<ASMInstruction> newInstList = new LinkedList<>();
            ArrayList<ASMInstruction> newCtlInstList = new ArrayList<>();
            for (ASMInstruction inst : block.instructions) {
                if (inst instanceof JumpInst jumpInst) {
                    String dest = jumpInst.desName;
                    ReverseDomTreeNode destNode = domTree.label2node.get(dest);
                    while (!liveBlock.containsKey(destNode.block.name)) {
                        destNode = destNode.iDom;
                    }
                    jumpInst.desName = destNode.block.name;
                    to.add(jumpInst.desName);
                    newInstList.add(jumpInst);
                    newCtlInstList.add(jumpInst);
                } else if (inst instanceof BranchInst branchInst) {
                    String dest = branchInst.desName;
                    ReverseDomTreeNode destNode = domTree.label2node.get(dest);
                    while (!liveBlock.containsKey(destNode.block.name)) {
                        destNode = destNode.iDom;
                    }
                    branchInst.desName = destNode.block.name;
                    to.add(branchInst.desName);
                    newInstList.add(branchInst);
                    newCtlInstList.add(branchInst);
                } else if (liveInst.contains(inst)) {
                    newInstList.add(inst);
                }
            }
            block.instructions = newInstList;
            block.controlInstructions = newCtlInstList;
            newBlockList.add(block);
        }
        for (Map.Entry<String, Block> entry : liveBlock.entrySet()) {
            if (!to.contains(entry.getKey())) {
                func.entry = entry.getValue();
            }
        }
        func.funcBlocks = newBlockList;
    }
}
