package midend.optimizer;

import ir.*;
import ir.entity.*;
import ir.entity.constant.*;
import ir.entity.var.*;
import ir.function.Function;
import ir.stmt.*;
import ir.stmt.instruction.*;
import ir.stmt.terminal.*;
import utility.Pair;
import utility.error.InternalException;

import java.util.*;

/**
 * @author F
 * 条件常量传播
 * - 消除部分死代码
 * - 更广的常量传播
 * -----------------------------------------------------------------
 * 动态promote 寻找“不动点”
 * promote层数严格受限，算法进行较快
 * -----------------------------------------------------------------
 */
public class CCP {
    IRRoot irRoot;

    public enum VarType {
        noExeDef,
        oneConstDef,
        multiExeDef
    }

    public enum BlockType {
        executable,
        unknown//不可达
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

    HashMap<LocalTmpVar, Constant> localTmpVar2Const = null;
    // 收集block信息
    // (currentBlockType,newly promoted)
    HashMap<String,
            Pair<BlockType,
                    Boolean
                    >
            > blockInfo = null;

    public CCP(IRRoot root) {
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
        Pair<BlockType, Boolean> pair = blockInfo.get(func.entry.label);
        pair.setFirst(BlockType.executable);
        pair.setSecond(true);
        // loop until workList cleared
        while ((!bbWorkList.isEmpty()) || (!varWorkList.isEmpty())) {
            // chose a bb from workList
            if (!bbWorkList.isEmpty()) {
                BasicBlock bb = bbWorkList.iterator().next();
                bbWorkList.remove(bb);
                Pair<BlockType, Boolean> bbInfo = blockInfo.get(bb.label);
                //executable successor of bb
                if (bbInfo.getFirst() == BlockType.executable) {
                    // executable def stmt in bb
                    for (Stmt stmt : bb.statements) {
                        if (stmt.hasDef()) {
                            propagateOnInst((Instruction) stmt);
                        }
                    }
                }
                // tailStmt
                if (bb.tailStmt instanceof Jump jump && jump.target == null) {
                    jump.target = func.blockMap.get(jump.targetName);
                }
                ArrayList<BasicBlock> executableSuccessor = getExecutableSuccessor(bb);
                // newly executable
                // its executable successor may have some update
                if (bbInfo.getSecond()) {
                    bbInfo.setSecond(false);
                    bbWorkList.addAll(executableSuccessor);
                }
            }
            // chose a var from workList
            if (!varWorkList.isEmpty()) {
                LocalTmpVar var = varWorkList.iterator().next();
                promoteLocalTmpVar(var);
                varWorkList.remove(var);
            }
        }
        // remove dead block
        removeDeadBlock(func);
        // replace var with constant and remove useless assignment
        removeDeadVarDef(func);
        // update control flow
        updateControlFlow(func);
    }

    /**
     * 收集函数中的block和localTmpVar的相关信息
     *
     * @param func function to be optimized
     */
    void informationCollect(Function func) {
        localTmpVarInfo = new HashMap<>();
        localTmpVar2Const = new HashMap<>();
        blockInfo = new HashMap<>();
        for (LocalTmpVar para : func.parameterList) {
            localTmpVarInfo.put(para, new Pair<>(VarType.noExeDef, new ArrayList<>()));
        }
        for (Map.Entry<String, BasicBlock> blockEntry : func.blockMap.entrySet()) {
            collectDefInBlock(blockEntry.getValue());
        }
        collectDefInBlock(func.ret);
        for (Map.Entry<String, BasicBlock> blockEntry : func.blockMap.entrySet()) {
            collectUseInBlock(blockEntry.getValue());
        }
        collectUseInBlock(func.ret);
    }

    void collectDefInBlock(BasicBlock block) {
        Entity def;
        for (Stmt stmt : block.statements) {
            if (stmt.hasDef()) {
                def = stmt.getDef();
                if (def instanceof LocalTmpVar localTmpVar) {
                    localTmpVarInfo.put(localTmpVar, new Pair<>(VarType.noExeDef, new ArrayList<>()));
                }
            }
        }
        // block
        blockInfo.put(block.label, new Pair<>(BlockType.unknown, false));
    }

    void collectUseInBlock(BasicBlock block) {
        ArrayList<Entity> use;
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

    ArrayList<BasicBlock> getExecutableSuccessor(BasicBlock bb) {
        ArrayList<BasicBlock> executableSuccessor = new ArrayList<>();
        if (bb.tailStmt instanceof Jump jump) {
            updateBlockInfo(jump.target);
            executableSuccessor.add(jump.target);
        } else if (bb.tailStmt instanceof Branch branch) {
            boolean onlyOneExecutable = true;
            boolean cond = false;
            if (branch.condition instanceof Constant constant) {
                if (constant instanceof ConstBool constBool) {
                    cond = constBool.value;
                } else {
                    throw new InternalException("[CCP]:condition in branch should be bool?");
                }
            } else if (branch.condition instanceof LocalTmpVar tmpVar &&
                    localTmpVarInfo.get(tmpVar).getFirst() == VarType.oneConstDef) {
                cond = ((ConstBool) localTmpVar2Const.get(tmpVar)).value;
            } else {
                onlyOneExecutable = false;
            }
            if (onlyOneExecutable) {
                if (cond) {
                    updateBlockInfo(branch.trueBranch);
                    executableSuccessor.add(branch.trueBranch);
                } else {
                    updateBlockInfo(branch.falseBranch);
                    executableSuccessor.add(branch.falseBranch);
                }
            } else {
                updateBlockInfo(branch.trueBranch);
                executableSuccessor.add(branch.trueBranch);
                updateBlockInfo(branch.falseBranch);
                executableSuccessor.add(branch.falseBranch);
            }
        }
        return executableSuccessor;
    }

    void updateBlockInfo(BasicBlock block) {
        Pair<BlockType, Boolean> targetInfo = blockInfo.get(block.label);
        if (targetInfo.getFirst() == BlockType.unknown) {
            targetInfo.setFirst(BlockType.executable);
            targetInfo.setSecond(true);
        }
    }

    /**
     * 一个var的更新可能导致别的var的更新
     *
     * @param tmpVar 更新过的var
     */
    void promoteLocalTmpVar(LocalTmpVar tmpVar) {
        ArrayList<Stmt> useList = localTmpVarInfo.get(tmpVar).getSecond();
        for (Stmt inst : useList) {
            propagateOnInst((Instruction) inst);
        }
    }

    /**
     * 处理可执行的def语句
     *
     * @param instruction stmt that has def
     */
    void propagateOnInst(Instruction instruction) {
        if (instruction instanceof Binary binary) {
            propagateOnBinary(binary);
        } else if (instruction instanceof Icmp icmp) {
            propagateOnIcmp(icmp);
        } else if (instruction instanceof Phi phi) {
            propagateOnPhi(phi);
        } else if (instruction instanceof Trunc trunc) {
            propagateOnTrunc(trunc);
        } else if (instruction instanceof Zext zext) {
            propagateOnZext(zext);
        } else {
            if (instruction.getDef() instanceof LocalTmpVar tmpVar) {
                promoteToMulti(tmpVar);
            }
        }
    }

    void propagateOnBinary(Binary binaryInst) {
        Pair<Constant, VarType> op1Info = entity2Constant(binaryInst.op1);
        Pair<Constant, VarType> op2Info = entity2Constant(binaryInst.op2);
        VarType op1Type = op1Info.getSecond();
        VarType op2Type = op2Info.getSecond();
        if (op1Type == VarType.multiExeDef ||
                op2Type == VarType.multiExeDef) {
            if (binaryInst.getDef() instanceof LocalTmpVar tmpVar) {
                promoteToMulti(tmpVar);
            }
        } else if (op1Type == VarType.oneConstDef &&
                op2Type == VarType.oneConstDef &&
                binaryInst.getDef() instanceof LocalTmpVar tmpVar) {
            Constant constant = Binary.calConstResult(
                    op1Info.getFirst(),
                    op2Info.getFirst(),
                    binaryInst.operator
            );
            assignConstToVar(tmpVar, constant);
        }
    }

    void propagateOnIcmp(Icmp icmpInst) {
        Pair<Constant, VarType> op1Info = entity2Constant(icmpInst.op1);
        Pair<Constant, VarType> op2Info = entity2Constant(icmpInst.op2);
        VarType op1Type = op1Info.getSecond();
        VarType op2Type = op2Info.getSecond();
        if (op1Type == VarType.multiExeDef ||
                op2Type == VarType.multiExeDef) {
            if (icmpInst.getDef() instanceof LocalTmpVar tmpVar) {
                promoteToMulti(tmpVar);
            }
        } else if (op1Type == VarType.oneConstDef &&
                op2Type == VarType.oneConstDef &&
                icmpInst.getDef() instanceof LocalTmpVar tmpVar) {
            Constant constant = Icmp.calConstResult(
                    op1Info.getFirst(),
                    op2Info.getFirst(),
                    icmpInst.cond
            );
            assignConstToVar(tmpVar, constant);
        }
    }

    void propagateOnPhi(Phi phiInst) {
        Pair<Constant, VarType> ans1Info = entity2Constant(phiInst.ans1);
        Pair<Constant, VarType> ans2Info = entity2Constant(phiInst.ans2);
        VarType ans1Type = ans1Info.getSecond();
        VarType ans2Type = ans2Info.getSecond();
        BlockType label1bbType = blockInfo.get(phiInst.label1.get(0)).getFirst();
        BlockType label2bbType = blockInfo.get(phiInst.label2.get(0)).getFirst();
        if (phiInst.getDef() instanceof LocalTmpVar tmpVar) {
            if ((ans1Type == VarType.multiExeDef &&
                    label1bbType == BlockType.executable)
                    ||
                    (ans2Type == VarType.multiExeDef &&
                            label2bbType == BlockType.executable)) {
                promoteToMulti(tmpVar);
            } else {
                int constCnt = 0;
                Constant constant = null;
                if (ans1Type == VarType.oneConstDef &&
                        label1bbType == BlockType.executable) {
                    constCnt += 1;
                    constant = ans1Info.getFirst();
                }
                if (ans2Type == VarType.oneConstDef &&
                        label2bbType == BlockType.executable) {
                    Constant constant2 = ans2Info.getFirst();
                    if (constant == null || Constant.equalInValue(constant, constant2)) {
                        constCnt += 1;
                        constant = constant2;
                    }
                }
                if (constCnt == 1) {
                    assignConstToVar(tmpVar, constant);
                } else if (constCnt == 2) {
                    promoteToMulti(tmpVar);
                }
            }
        }

    }

    void propagateOnTrunc(Trunc truncInst) {
        Pair<Constant, VarType> valueInfo = entity2Constant(truncInst.value);
        VarType valueType = valueInfo.getSecond();
        if (valueType == VarType.multiExeDef) {
            if (truncInst.getDef() instanceof LocalTmpVar tmpVar) {
                promoteToMulti(tmpVar);
            }
        } else if (valueType == VarType.oneConstDef &&
                truncInst.getDef() instanceof LocalTmpVar tmpVar) {
            assignConstToVar(tmpVar, valueInfo.getFirst());
        }
    }

    void propagateOnZext(Zext zextInst) {
        Pair<Constant, VarType> valueInfo = entity2Constant(zextInst.value);
        VarType valueType = valueInfo.getSecond();
        if (valueType == VarType.multiExeDef) {
            if (zextInst.getDef() instanceof LocalTmpVar tmpVar) {
                promoteToMulti(tmpVar);
            }
        } else if (valueType == VarType.oneConstDef &&
                zextInst.getDef() instanceof LocalTmpVar tmpVar) {
            assignConstToVar(tmpVar, valueInfo.getFirst());
        }
    }

    Pair<Constant, VarType> entity2Constant(Entity entity) {
        if (entity instanceof Constant constant) {
            return new Pair<>(constant, VarType.oneConstDef);
        }
        if (entity instanceof Ptr) {
            return new Pair<>(null, VarType.multiExeDef);
        }
        if (entity instanceof LocalTmpVar tmpVar) {
            if (localTmpVarInfo.containsKey(tmpVar)) {
                switch (localTmpVarInfo.get(tmpVar).getFirst()) {
                    case noExeDef -> {
                        return new Pair<>(null, VarType.noExeDef);
                    }
                    case oneConstDef -> {
                        return new Pair<>(
                                localTmpVar2Const.get(tmpVar),
                                VarType.oneConstDef
                        );
                    }
                    case multiExeDef -> {
                        return new Pair<>(null, VarType.multiExeDef);
                    }
                }
            }
        }
        throw new InternalException("[CCP]:unexpected entity type");
    }

    void promoteToMulti(LocalTmpVar tmpVar) {
        Pair<VarType, ArrayList<Stmt>> tmpInfo = localTmpVarInfo.get(tmpVar);
        if (tmpInfo.getFirst() != VarType.multiExeDef) {
            varWorkList.add(tmpVar);
            tmpInfo.setFirst(VarType.multiExeDef);
        }
    }

    void assignConstToVar(LocalTmpVar tmpVar, Constant constant) {
        Pair<VarType, ArrayList<Stmt>> tmpInfo = localTmpVarInfo.get(tmpVar);
        switch (tmpInfo.getFirst()) {
            case noExeDef -> {
                tmpInfo.setFirst(VarType.oneConstDef);
                localTmpVar2Const.put(tmpVar, constant);
                varWorkList.add(tmpVar);
            }
            case oneConstDef -> {
                // promote to multi
                tmpInfo.setFirst(VarType.multiExeDef);
                localTmpVar2Const.remove(tmpVar);
                // add to workList
                varWorkList.add(tmpVar);
            }
            case multiExeDef -> throw new InternalException("[CCP]:level of local tmp var should increase only");
        }
    }

    void removeDeadBlock(Function func) {
        LinkedHashMap<String, BasicBlock> newBlockMap = new LinkedHashMap<>();
        for (Map.Entry<String, BasicBlock> blockEntry : func.blockMap.entrySet()) {
            String blockName = blockEntry.getKey();
            if (blockInfo.get(blockName).getFirst() == BlockType.executable) {
                newBlockMap.put(blockName, blockEntry.getValue());
            }
        }
        func.blockMap = newBlockMap;
    }

    void removeDeadVarDef(Function func) {
        // clear use list and recollect
        for (Map.Entry<LocalTmpVar, Pair<VarType, ArrayList<Stmt>>> entry : localTmpVarInfo.entrySet()) {
            entry.getValue().setSecond(new ArrayList<>());
        }
        HashMap<LocalTmpVar, Pair<BasicBlock, Stmt>> varDef = new HashMap<>();
        for (LocalTmpVar para : func.parameterList) {
            varDef.put(
                    para,
                    new Pair<>(null, null)
            );
        }
        for (Map.Entry<String, BasicBlock> blockEntry : func.blockMap.entrySet()) {
            recollectUseDefInBlock(blockEntry.getValue(), varDef);
        }
        recollectUseDefInBlock(func.ret, varDef);
        for (Map.Entry<LocalTmpVar, Pair<VarType, ArrayList<Stmt>>> entry : localTmpVarInfo.entrySet()) {
            LocalTmpVar var = entry.getKey();
            Pair<VarType, ArrayList<Stmt>> varInfo = entry.getValue();
            // replace constant
            if (varInfo.getFirst() == VarType.oneConstDef) {
                ArrayList<Stmt> useList = varInfo.getSecond();
                for (Stmt stmt : useList) {
                    stmt.replaceUse(localTmpVar2Const);
                }
                // remove def
                Pair<BasicBlock, Stmt> defStmtInfo = varDef.get(var);
                // not param
                if (defStmtInfo.getFirst() != null) {
                    defStmtInfo.getFirst().statements.remove(defStmtInfo.getSecond());
                }
            }
            // remove not used
            else if (varInfo.getSecond().size() == 0 && varDef.containsKey(var)) {
                Pair<BasicBlock, Stmt> defStmtInfo = varDef.get(var);
                Stmt stmt = defStmtInfo.getSecond();
                if (stmt instanceof Call callStmt) {
                    callStmt.result = null;
                } else {
                    defStmtInfo.getFirst().statements.remove(defStmtInfo.getSecond());
                }
            }
        }
    }

    void recollectUseDefInBlock(BasicBlock block, HashMap<LocalTmpVar, Pair<BasicBlock, Stmt>> varDef) {
        Entity def;
        ArrayList<Entity> use;
        for (Stmt stmt : block.statements) {
            if (stmt.hasDef()) {
                def = stmt.getDef();
                if (def instanceof LocalTmpVar localTmpVar) {
                    varDef.put(
                            localTmpVar,
                            new Pair<>(block, stmt)
                    );
                }
            }
            use = stmt.getUse();
            if (use != null) {
                for (Entity usedEntity : use) {
                    if (usedEntity instanceof LocalTmpVar) {
                        localTmpVarInfo.get(usedEntity).getSecond().add(stmt);
                    }
                }
            }
        }
        use = block.tailStmt.getUse();
        if (use != null) {
            for (Entity usedEntity : use) {
                if (usedEntity instanceof LocalTmpVar) {
                    localTmpVarInfo.get(usedEntity).getSecond().add(block.tailStmt);
                }
            }
        }
    }

    /**
     * if branch is determined
     * use jump
     *
     * @param func function
     */
    void updateControlFlow(Function func) {
        for (Map.Entry<String, BasicBlock> entry : func.blockMap.entrySet()) {
            BasicBlock block = entry.getValue();
            if (block.tailStmt instanceof Branch branch) {
                boolean convertToJump = true;
                boolean cond = false;
                if (branch.condition instanceof Constant constant) {
                    if (constant instanceof ConstBool constBool) {
                        cond = constBool.value;
                    } else {
                        throw new InternalException("[CCP]:condition in branch should be bool?");
                    }
                } else {
                    convertToJump = false;
                }
                if (convertToJump) {
                    Jump newStmt;
                    if (cond) {
                        newStmt = new Jump(branch.trueBranch,
                                branch.index,
                                branch.phiLabel,
                                branch.result
                        );
                    }
                    // jump to the false block
                    else {
                        newStmt = new Jump(branch.falseBranch,
                                branch.index,
                                branch.phiLabel,
                                branch.result
                        );
                    }
                    block.tailStmt = newStmt;
                }
            }
        }
    }
}
