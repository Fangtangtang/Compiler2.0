package utility.live;

import ir.BasicBlock;
import ir.entity.SSAEntity;
import ir.function.Function;
import ir.stmt.Stmt;
import ir.stmt.instruction.Phi;
import utility.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author F
 * 活跃度分析
 * - 收集合并global live range
 * - 收集Basic Block的useVar和defVar
 */
public class LiveVarAnalysis {
    public LiveVarAnalysis(Function function) {
        for (Map.Entry<String, BasicBlock> pair : function.blockMap.entrySet()) {
            BasicBlock block = pair.getValue();
            getLiveRange(block);
            getDefUse(block);
        }
    }

    void getLiveRange(BasicBlock block) {
        for (Map.Entry<
                String, Pair<SSAEntity, Pair<String[], SSAEntity[]>>
                > pair : block.phiMap.entrySet()) {
            Pair<SSAEntity, Pair<String[], SSAEntity[]>> phiPair = pair.getValue();
            SSAEntity result = phiPair.getFirst();
            SSAEntity[] selections = phiPair.getSecond().getSecond();
            for (SSAEntity selection : selections) {
                result.lr.union(selection.lr);
            }
        }
        for (int i = 0; i < block.statements.size(); ++i) {
            if (block.statements.get(i) instanceof Phi phiStmt) {
                phiStmt.ssaResult.lr.union(phiStmt.ssaAns1.lr);
                phiStmt.ssaResult.lr.union(phiStmt.ssaAns2.lr);
            }
        }
    }

    void getOnStmt(Stmt stmt, BasicBlock block) {
        SSAEntity varDef = stmt.getSSADef();
        ArrayList<SSAEntity> varUse = stmt.getSSAUse();
        //def
        if (varDef != null && varDef.lr != null) {
            GlobalLiveRange lr = varDef.lr.find();
            block.def.put(lr.rename, lr);
        }
        //use
        if (varUse != null) {
            for (SSAEntity entity : varUse) {
                GlobalLiveRange lr = entity.lr.find();
                block.use.put(lr.rename, lr);
            }
        }
    }

    void getDefUse(BasicBlock block) {
        for (int i = 0; i < block.statements.size(); ++i) {
            getOnStmt(block.statements.get(i), block);
        }
        getOnStmt(block.tailStmt, block);
    }

    public void liveOutAnalysis(Function function) {
        boolean changed = true;
        while (changed) {
            changed = false;
            for (int i = 1; i < function.reorderedBlock.size(); i++) {
                //updated with RPO
                BasicBlock current = function.reorderedBlock.get(i).block;
                HashMap<String, GlobalLiveRange> tmp = new HashMap<>();
                BasicBlock successor;
                for (int k = 0; k < current.successorList.size(); k++) {
                    successor = current.successorList.get(k);
                    for (Map.Entry<String, GlobalLiveRange> liveOutVar : current.liveOut.entrySet()) {
                        if (!current.def.containsKey(liveOutVar.getKey())) {
                            tmp.put(liveOutVar.getKey(), liveOutVar.getValue());
                        }
                    }
                    tmp.putAll(current.use);
                }
                if (!tmp.equals(current.liveOut)) {
                    changed = true;
                    current.liveOut = tmp;
                }
            }
        }
    }

}
