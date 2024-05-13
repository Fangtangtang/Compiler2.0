package midend.optimizer;

import ir.*;
import ir.entity.constant.ConstBool;
import ir.entity.constant.Constant;
import ir.function.Function;
import ir.stmt.*;
import ir.stmt.instruction.*;
import ir.stmt.terminal.*;

import java.util.*;

/**
 * @author F
 * 将BB合并
 */
public class BasicBlockEliminator {
    IRRoot irRoot;

    public BasicBlockEliminator(IRRoot root) {
        irRoot = root;
    }

    public void simplifyBlock() {
        for (Map.Entry<String, Function> funcEntry : irRoot.funcDef.entrySet()) {
            Function func = funcEntry.getValue();
            //普通local function
            if (func.entry != null) {
                simplifyBlockOnFunc(func);
            }
        }
    }

    void simplifyBlockOnFunc(Function func) {
        // collect PhiStmts
        ArrayList<Phi> phiStmts = new ArrayList<>();
        // collect prev
        for (Map.Entry<String, BasicBlock> blockEntry : func.blockMap.entrySet()) {
            BasicBlock block = blockEntry.getValue();
            for (Stmt stmt : block.statements) {
                if (stmt instanceof Phi phi) {
                    phiStmts.add(phi);
                }
            }
//           TODO:block映射出错
            if (block.tailStmt instanceof Jump jump) {
                jump.target.prevBasicBlocks.add(block.label);
                block.subsBasicBlocks.add(jump.targetName);
            } else if (block.tailStmt instanceof Branch branch) {
                branch.trueBranch.prevBasicBlocks.add(block.label);
                branch.falseBranch.prevBasicBlocks.add(block.label);
                block.subsBasicBlocks.add(branch.trueBranch.label);
                block.subsBasicBlocks.add(branch.falseBranch.label);
            }
        }
        ArrayList<BasicBlock> blocks = new ArrayList<>();
        HashMap<String, String> blockMap = new HashMap<>();
        for (Map.Entry<String, BasicBlock> blockEntry : func.blockMap.entrySet()) {
            BasicBlock block = blockEntry.getValue();
            if (block.prevBasicBlocks.size() == 1) {
                blocks.add(block);
            }
        }
        for (BasicBlock block : blocks) {
            String prevLabel = block.prevBasicBlocks.get(0);
            while (blockMap.containsKey(prevLabel)) {
                prevLabel = blockMap.get(prevLabel);
            }
            BasicBlock prev = func.blockMap.get(prevLabel);
            if (prev.subsBasicBlocks.size() == 1) {
                prev.statements.addAll(block.statements);
                prev.tailStmt = block.tailStmt;
                blockMap.put(block.label, prevLabel);
                func.blockMap.remove(block.label);
                prev.subsBasicBlocks = block.subsBasicBlocks;
            }
        }
        // rename label in phi
        for (Phi phiStmt : phiStmts) {
            phiStmt.remapLabelS2S(blockMap);
        }
    }

    public void simplifyCtlFlow() {
        for (Map.Entry<String, Function> funcEntry : irRoot.funcDef.entrySet()) {
            Function func = funcEntry.getValue();
            //普通local function
            if (func.entry != null) {
//                simplifyCtlFlowOnFunc2(func);
                simplifyCtlFlowOnFunc(func);
            }
        }
    }

    void simplifyCtlFlowOnFunc(Function func) {
        HashSet<BasicBlock> workList = new HashSet<>();
        HashSet<String> deadBlock = new HashSet<>();
        HashSet<String> labelInPhi = new HashSet<>();
        for (Map.Entry<String, BasicBlock> blockEntry : func.blockMap.entrySet()) {
            BasicBlock block = blockEntry.getValue();
            workList.add(block);
            for (Stmt stmt : block.statements) {
                if (stmt instanceof Phi phi) {
                    HashSet<String> tmp = new HashSet<>();
                    tmp.addAll(phi.label1);
                    tmp.addAll(phi.label2);
                    // size<=1 fake phi
                    if (tmp.size() > 1) {
                        labelInPhi.addAll(tmp);
                    }
                }
            }
        }
        while (!workList.isEmpty()) {
            BasicBlock block = workList.iterator().next();
            if (block.tailStmt instanceof Branch branch) {
                boolean replaced = false;
                if (branch.trueBranch.statements.size() == 0 &&
                        branch.trueBranch.tailStmt instanceof Jump jump &&
                        !labelInPhi.contains(branch.trueBranchName)) {
                    replaced = true;
                    if (jump.target == null) {
                        jump.target = func.blockMap.get(jump.targetName);
                    }
                    deadBlock.add(branch.trueBranch.label);
                    workList.remove(branch.trueBranch);
                    branch.trueBranch = jump.target;
                    branch.trueBranchName = jump.targetName;
                }
                if (branch.falseBranch.statements.size() == 0 &&
                        branch.falseBranch.tailStmt instanceof Jump jump &&
                        !labelInPhi.contains(branch.falseBranchName)) {
                    replaced = true;
                    if (jump.target == null) {
                        jump.target = func.blockMap.get(jump.targetName);
                    }
                    deadBlock.add(branch.falseBranch.label);
                    workList.remove(branch.falseBranch);
                    branch.falseBranch = jump.target;
                    branch.falseBranchName = jump.targetName;
                }
                if (replaced) {
                    continue;
                }
            }
            if (block.tailStmt instanceof Jump jump) {
                if (jump.target == null) {
                    jump.target = func.blockMap.get(jump.targetName);
                }
                BasicBlock to = jump.target;
                if (to.statements.size() == 0 &&
                        !labelInPhi.contains(jump.targetName) &&
                        !(to.tailStmt instanceof Return)) {
                    workList.remove(to);
                    block.tailStmt = to.tailStmt;
                    deadBlock.add(to.label);
                    continue;
                }
            }
            workList.remove(block);
        }
        for (String dead : deadBlock) {
            func.blockMap.remove(dead);
        }
    }

    // todo
    void simplifyCtlFlowOnFunc2(Function func) {
//        HashSet<BasicBlock> workList = new HashSet<>();
//        HashSet<String> deadBlock = new HashSet<>();
        HashSet<String> labelInPhi = new HashSet<>();
        HashMap<String, String> ctlFlowLabelMap = new HashMap<>();
        HashMap<String, String> reverseCtlFlowLabelMap = new HashMap<>();
        for (Map.Entry<String, BasicBlock> blockEntry : func.blockMap.entrySet()) {
            BasicBlock block = blockEntry.getValue();
//            workList.add(block);
            for (Stmt stmt : block.statements) {
                if (stmt instanceof Phi phi) {
                    HashSet<String> tmp = new HashSet<>();
                    tmp.addAll(phi.label1);
                    tmp.addAll(phi.label2);
                    // size<=1 fake phi
                    if (tmp.size() > 1) {
                        labelInPhi.addAll(tmp);
                    }
                }
            }
            if (block.statements.size() == 0) {
                if (block.tailStmt instanceof Jump jump) {
                    ctlFlowLabelMap.put(block.label, jump.targetName);
                    reverseCtlFlowLabelMap.put(jump.targetName, block.label);
                } else if (block.tailStmt instanceof Branch branch) {
                    if (branch.condition instanceof ConstBool constBool) {
                        if (constBool.value) {
                            ctlFlowLabelMap.put(block.label, branch.trueBranchName);
                            reverseCtlFlowLabelMap.put(branch.trueBranchName, block.label);
                        } else {
                            ctlFlowLabelMap.put(block.label, branch.falseBranchName);
                            reverseCtlFlowLabelMap.put(branch.falseBranchName, block.label);
                        }
                    }
                }
            }
        }
        for (Map.Entry<String, BasicBlock> blockEntry : func.blockMap.entrySet()) {
            BasicBlock block = blockEntry.getValue();
            for (Stmt stmt : block.statements) {
                if (stmt instanceof Phi phi) {
                    phi.remapLabelS2M(reverseCtlFlowLabelMap);
                }
            }
            if (block.tailStmt instanceof Jump jump) {
                String label = jump.targetName;
                while (ctlFlowLabelMap.containsKey(label)) {
                    label = ctlFlowLabelMap.get(label);
                }
                if (!label.equals(jump.targetName)) {
                    jump.targetName = label;
                    if (label.equals(func.ret.label)) {
                        jump.target = func.ret;
                    } else {
                        jump.target = func.blockMap.get(label);
                    }
                }
            } else if (block.tailStmt instanceof Branch branch) {
                String label = branch.trueBranchName;
                while (ctlFlowLabelMap.containsKey(label)) {
                    label = ctlFlowLabelMap.get(label);
                }
                if (!label.equals(branch.trueBranchName)) {
                    branch.trueBranchName = label;
                    if (label.equals(func.ret.label)) {
                        branch.trueBranch = func.ret;
                    } else {
                        branch.trueBranch = func.blockMap.get(label);
                    }
                }
                label = branch.falseBranchName;
                while (ctlFlowLabelMap.containsKey(label)) {
                    label = ctlFlowLabelMap.get(label);
                }
                if (!label.equals(branch.falseBranchName)) {
                    branch.falseBranchName = label;
                    if (label.equals(func.ret.label)) {
                        branch.falseBranch = func.ret;
                    } else {
                        branch.falseBranch = func.blockMap.get(label);
                    }
                }
            }
        }
    }

}
