package midend.optimizer;

import ir.*;
import ir.entity.*;
import ir.entity.var.*;
import ir.function.Function;
import ir.stmt.*;
import utility.Pair;

import java.util.*;

/**
 * @author F
 * 条件常量传播
 * - 消除部分死代码
 * - 更广的常量传播
 */
public class ConditionalConstantPropagation {
    IRRoot irRoot;

    public enum VarType {
        noExeDef,
        oneExeDef,
        multiExeDef
    }

    public enum BlockType {
        executable,
        unknown //不可达
    }

    HashSet<LocalTmpVar> varWorkList = null;
    HashSet<BasicBlock> bbWorkList = null;

    // 收集localTmpVar信息
    // (currentVarType,useList)
    HashMap<LocalTmpVar,
            Pair<VarType,
                    ArrayList<Stmt>
                    >
            > localTmpVarInfo = null;

    // 收集block信息
    // (currentBlockType,newly promoted)
    HashMap<BasicBlock,
            Pair<BlockType,
                    Boolean
                    >
            > blockInfo = null;

    public ConditionalConstantPropagation(IRRoot root) {
        irRoot = root;
    }

    public void execute() {
        for (Map.Entry<String, Function> funcEntry : irRoot.funcDef.entrySet()) {
            Function func = funcEntry.getValue();
            //普通local function
            if (func.entry != null) {
                propagateOnFunc(func);
            }
        }
    }

    void propagateOnFunc(Function func) {
        informationCollect(func);
        varWorkList = new HashSet<>();
        bbWorkList = new HashSet<>();
        //initiate
        bbWorkList.add(func.entry);
        Pair<BlockType, Boolean> pair = blockInfo.get(func.entry);
        pair.setFirst(BlockType.executable);
        pair.setSecond(true);
        // loop until workList cleared
        while ((!bbWorkList.isEmpty()) || (!varWorkList.isEmpty())) {
            if (!bbWorkList.isEmpty()) {
                BasicBlock bb = bbWorkList.iterator().next();
                //TODO
            }
            if (!varWorkList.isEmpty()) {
                //TODO
            }
        }
    }

    /**
     * 收集函数中的block和localTmpVar的相关信息
     *
     * @param func function to be optimized
     */
    void informationCollect(Function func) {
        localTmpVarInfo = new HashMap<>();
        blockInfo = new HashMap<>();
        Entity def;
        for (Map.Entry<String, BasicBlock> blockEntry : func.blockMap.entrySet()) {
            BasicBlock block = blockEntry.getValue();
            // localTmpVar
            for (Stmt stmt : block.statements) {
                if (stmt.hasDef()) {
                    def = stmt.getDef();
                    if (def instanceof LocalTmpVar localTmpVar) {
                        localTmpVarInfo.put(localTmpVar, new Pair<>(VarType.noExeDef, new ArrayList<>()));
                    }
                }
            }
            // block
            blockInfo.put(block, new Pair<>(BlockType.unknown, false));
        }
        ArrayList<Entity> use;
        for (Map.Entry<String, BasicBlock> blockEntry : func.blockMap.entrySet()) {
            BasicBlock block = blockEntry.getValue();
            for (Stmt stmt : block.statements) {
                // only def are needed
                if (stmt.hasDef()) {
                    use = stmt.getUse();
                    if (use != null) {
                        for (Entity usedEntity : use) {
                            if (usedEntity instanceof LocalTmpVar) {
                                localTmpVarInfo.get(usedEntity).getSecond().add(stmt);
                            }
                        }
                    }
                }
            }
        }
    }
}
