package midend.optimizer;

import ir.*;
import ir.entity.Storage;
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
        // init
        func.clearGenealogy();
        // collect PhiStmts
        ArrayList<DualPhi> dualPhiStmts = new ArrayList<>();
        ArrayList<DomPhi> domPhiStmts = new ArrayList<>();
        // collect prev
        for (Map.Entry<String, BasicBlock> blockEntry : func.blockMap.entrySet()) {
            BasicBlock block = blockEntry.getValue();
            for (Stmt stmt : block.statements) {
                if (stmt instanceof DualPhi dualPhi) {
                    dualPhiStmts.add(dualPhi);
                } else if (stmt instanceof DomPhi domPhi) {
                    domPhiStmts.add(domPhi);
                }
            }
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
        for (DualPhi dualPhiStmt : dualPhiStmts) {
            dualPhiStmt.remapLabel(blockMap);
        }
        for (DomPhi domPhiStmt : domPhiStmts) {
            domPhiStmt.remapLabel(blockMap);
        }
    }

    public void simplifyCtlFlow() {
        for (Map.Entry<String, Function> funcEntry : irRoot.funcDef.entrySet()) {
            Function func = funcEntry.getValue();
            //普通local function
            if (func.entry != null) {
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
                if (stmt instanceof DualPhi dualPhi) {
                    HashSet<String> tmp = new HashSet<>();
                    tmp.add(dualPhi.label1);
                    tmp.add(dualPhi.label2);
                    // size<=1 fake dualPhi
                    if (tmp.size() > 1) {
                        labelInPhi.addAll(tmp);
                    }
                }
                if (stmt instanceof DomPhi domPhi) {
                    HashSet<String> tmp = new HashSet<>();
                    for (Map.Entry<String, Storage> entry : domPhi.phiList.entrySet()) {
                        tmp.add(entry.getKey());
                    }
                    if (tmp.size() > 1) {
                        labelInPhi.addAll(tmp);
                    }
                }
            }
        }
        BasicBlock retBlock = func.ret;
        workList.add(retBlock);
        for (Stmt stmt : retBlock.statements) {
            if (stmt instanceof DualPhi dualPhi) {
                HashSet<String> tmp = new HashSet<>();
                tmp.add(dualPhi.label1);
                tmp.add(dualPhi.label2);
                // size<=1 fake dualPhi
                if (tmp.size() > 1) {
                    labelInPhi.addAll(tmp);
                }
            }
            if (stmt instanceof DomPhi domPhi) {
                HashSet<String> tmp = new HashSet<>();
                for (Map.Entry<String, Storage> entry : domPhi.phiList.entrySet()) {
                    tmp.add(entry.getKey());
                }
                if (tmp.size() > 1) {
                    labelInPhi.addAll(tmp);
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

}
