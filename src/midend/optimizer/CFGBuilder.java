package midend.optimizer;

import ir.BasicBlock;
import ir.IRRoot;
import ir.function.Function;
import ir.stmt.terminal.Branch;
import ir.stmt.terminal.Jump;
import utility.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @author F
 * IR上CFGBuilder
 * 构建每个函数的CFG
 */
public class CFGBuilder {
    IRRoot irRoot;

    public CFGBuilder(IRRoot root) {
        this.irRoot = root;
        ArrayList<String> rmList = new ArrayList<>();
        //将内建函数提出
        for (Map.Entry<String, Function> entry : root.funcDef.entrySet()) {
            String funcName = entry.getKey();
            Function function = entry.getValue();
            if (function.entry == null) {
                rmList.add(funcName);
                root.builtinFuncDef.put(funcName, function);
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
        // eliminate some dead block here
        // bb -> <predecessorList , successorList>
        HashMap<String, Pair<ArrayList<String>, ArrayList<String>>> familyTree = new HashMap<>();
        for (Map.Entry<String, BasicBlock> entry : function.blockMap.entrySet()) {
            BasicBlock block = entry.getValue();
            if (!familyTree.containsKey(block.label)) {
                familyTree.put(block.label, new Pair<>(new ArrayList<>(), new ArrayList<>()));
            }
            if (block.tailStmt instanceof Jump jump) {
                if (jump.target == null) {
                    jump.target = function.blockMap.get(jump.targetName);
                }
                if (!familyTree.containsKey(jump.target.label)) {
                    familyTree.put(jump.target.label, new Pair<>(new ArrayList<>(), new ArrayList<>()));
                }
                familyTree.get(block.label).getSecond().add(jump.target.label);
                familyTree.get(jump.target.label).getFirst().add(block.label);
            } else if (block.tailStmt instanceof Branch branch) {
                familyTree.get(block.label).getSecond().add(branch.trueBranch.label);
                familyTree.get(block.label).getSecond().add(branch.falseBranch.label);
                if (!familyTree.containsKey(branch.trueBranch.label)) {
                    familyTree.put(branch.trueBranch.label, new Pair<>(new ArrayList<>(), new ArrayList<>()));
                }
                if (!familyTree.containsKey(branch.falseBranch.label)) {
                    familyTree.put(branch.falseBranch.label, new Pair<>(new ArrayList<>(), new ArrayList<>()));
                }
                familyTree.get(branch.trueBranch.label).getFirst().add(block.label);
                familyTree.get(branch.falseBranch.label).getFirst().add(block.label);
            }
        }
        HashSet<String> eliminateWorkList = new HashSet<>();
        for (Map.Entry<String, Pair<ArrayList<String>, ArrayList<String>>> entry : familyTree.entrySet()) {
            if (entry.getValue().getFirst().size() == 0) {
                eliminateWorkList.add(entry.getKey());
            }
        }
        eliminateWorkList.remove(function.entry.label);
        while (!eliminateWorkList.isEmpty()) {
            String blockName = eliminateWorkList.iterator().next();
            eliminateWorkList.remove(blockName);
            function.blockMap.remove(blockName);
            for (String successor : familyTree.get(blockName).getSecond()) {
                ArrayList<String> list = familyTree.get(successor).getFirst();
                ArrayList<String> newList = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    if (!blockName.equals(list.get(i))) {
                        newList.add(list.get(i));
                    }
                }
                if (newList.size()==0){
                    eliminateWorkList.add(successor);
                }else {
                    familyTree.get(successor).setFirst(newList);
                }
            }
        }
        //对每一个basic block添加前驱后继
        for (Map.Entry<String, BasicBlock> entry : function.blockMap.entrySet()) {
            BasicBlock block = entry.getValue();
            if (block.tailStmt instanceof Jump jump) {
                block.successorList.add(jump.target);
                jump.target.predecessorList.add(block);
            } else if (block.tailStmt instanceof Branch branch) {
                block.successorList.add(branch.trueBranch);
                block.successorList.add(branch.falseBranch);
                branch.trueBranch.predecessorList.add(block);
                branch.falseBranch.predecessorList.add(block);
            }
        }
    }
}
