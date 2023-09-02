package midend.optimizer;

import ir.BasicBlock;
import ir.entity.*;
import ir.entity.constant.*;
import ir.entity.var.LocalTmpVar;
import ir.entity.var.Ptr;
import ir.function.Function;
import ir.stmt.Stmt;
import ir.stmt.instruction.Alloca;
import ir.stmt.instruction.Global;
import ir.stmt.terminal.Return;
import utility.Pair;
import utility.dominance.DomTree;

import java.util.*;

/**
 * @author F
 * promote memory to register
 * TODO:更多操作
 */
public class Mem2Reg {
    //在多于一个BB中被use的变量名
    HashSet<String> globalName;
    //变量名 -> 所有def该变量的BB
    HashMap<String, HashSet<String>> blocksSets;
    //blockLabel -> 所有def在block的变量名
    HashMap<String, HashSet<String>> defInBlockSets;

    public void execute(Function function) {
        findGlobalNames(function.blockMap);
        insertPhiFunction(function.domTree);
        rename(function.domTree);
    }

    String getVarName(Entity entity) {
        //非变量
        if (entity instanceof Constant) {
            return null;
        }
        return entity.toString();
    }

    void addVarDefInBlock(String varName, String blockLabel) {
        if (blocksSets.containsKey(varName)) {
            blocksSets.get(varName).add(blockLabel);
            return;
        }
        HashSet<String> set = new HashSet<>();
        set.add(blockLabel);
        blocksSets.put(varName, set);
    }

    void findGlobalNames(LinkedHashMap<String, BasicBlock> blockMap) {
        globalName = new HashSet<>();
        blocksSets = new HashMap<>();
        defInBlockSets = new HashMap<>();
        //for each block
        for (Map.Entry<String, BasicBlock> pair : blockMap.entrySet()) {
            HashSet<String> defInBlock = new HashSet<>();//在BB中被定义的
            BasicBlock block = pair.getValue();
            defInBlockSets.put(block.label, defInBlock);
            Stmt stmt;
            Entity varDef;
            ArrayList<Entity> varUse;
            //for each stmt
            for (int i = 0; i < block.statements.size(); ++i) {
                stmt = block.statements.get(i);
                if (stmt instanceof Global ||
                        stmt instanceof Alloca ||
                        stmt instanceof Return) {
                    continue;
                }
                varDef = stmt.getDef();
                varUse = stmt.getUse();
                //def
                if (varDef != null) {
                    String name = getVarName(varDef);
                    defInBlock.add(name);
                    addVarDefInBlock(name, block.label);
                }
                //use
                for (Entity entity : varUse) {
                    String name = getVarName(entity);
                    if (name != null) {
                        if (!defInBlock.contains(name)) {
                            globalName.add(name);
                        }
                    }
                }
            }
        }
    }

    void insertPhiFunction(DomTree domTree) {
        HashSet<String> workList;
        HashSet<String> defInBlock;
        for (String name : globalName) {
            workList = blocksSets.get(name);

        }
    }

    void rename(DomTree domTree) {

    }
}
