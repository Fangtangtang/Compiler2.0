package midend;

import ir.BasicBlock;
import ir.IRRoot;
import ir.function.Function;
import ir.stmt.terminal.Branch;
import ir.stmt.terminal.Jump;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author F
 * 构建每个函数的CFG
 */
public class CFGBuilder {
    IRRoot irRoot;

    public CFGBuilder(IRRoot root) {
        this.irRoot = root;
        ArrayList<String> rmList = new ArrayList<>();
        //将内建函数除名
        for (Map.Entry<String, Function> entry : root.funcDef.entrySet()) {
            String funcName = entry.getKey();
            Function function = entry.getValue();
            if (function.entry == null) {
                rmList.add(funcName);
            }
        }
        rmList.forEach(
                funcName -> root.funcDef.remove(funcName)
        );
    }

    /**
     * 构建CFG
     */
    public void build() {
        for (Map.Entry<String, Function> entry : irRoot.funcDef.entrySet()) {
            Function function = entry.getValue();
            buildOnFunction(function);
        }
    }

    private void buildOnFunction(Function function) {
        //对每一个basic block添加前驱后继
        for (Map.Entry<String, BasicBlock> entry : function.blockMap.entrySet()) {
            BasicBlock block = entry.getValue();
            if (block.tailStmt instanceof Jump jump) {
                block.successorList.add(jump.targetName);
                if (jump.target == null) {
                    jump.target = function.blockMap.get(jump.targetName);
                }
                jump.target.predecessorList.add(block.label);
            } else if (block.tailStmt instanceof Branch branch) {
                block.successorList.add(branch.trueBranch.label);
                block.successorList.add(branch.falseBranch.label);
                branch.trueBranch.predecessorList.add(block.label);
                branch.falseBranch.predecessorList.add(block.label);
            }
        }
    }
}
