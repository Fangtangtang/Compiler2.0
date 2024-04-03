package midend.optimizer;

import ir.BasicBlock;
import ir.IRRoot;
import ir.function.Function;
import ir.stmt.terminal.Branch;
import ir.stmt.terminal.Jump;
import utility.error.InternalException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @author F
 * 将BB合并
 */
public class BasicBlockEliminator {
    IRRoot irRoot;

    public BasicBlockEliminator(IRRoot root) {
        irRoot = root;
    }

    public void execute() {
        for (Map.Entry<String, Function> funcEntry : irRoot.funcDef.entrySet()) {
            Function func = funcEntry.getValue();
            //普通local function
            if (func.entry != null) {
                eliminateOnFunc(func);
            }
        }
    }

    void eliminateOnFunc(Function func) {
        // collect prev
        for (Map.Entry<String, BasicBlock> blockEntry : func.blockMap.entrySet()) {
            BasicBlock block = blockEntry.getValue();
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
    }
}
