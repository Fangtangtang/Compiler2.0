package midend.optimizer;

import ir.*;
import ir.entity.*;
import ir.entity.var.GlobalVar;
import ir.function.Function;
import ir.stmt.Stmt;
import ir.stmt.instruction.Call;
import utility.Pair;

import java.util.*;

/**
 * @author F
 * 死代码消除，简单版本
 */
public class DeadCodeEliminator {
    IRRoot irRoot;

    public DeadCodeEliminator(IRRoot root) {
        irRoot = root;
    }

    public void execute() {
        analysisFunc();
        for (Map.Entry<String, Function> funcEntry : irRoot.funcDef.entrySet()) {
            Function func = funcEntry.getValue();
            //普通local function
            if (func.entry != null) {
                eliminateOnFunc(func);
                removeDeadStmt(func);
            }
        }
    }

    void analysisFunc() {
        for (Map.Entry<String, Function> funcEntry : irRoot.funcDef.entrySet()) {
            Function func = funcEntry.getValue();
            if (func.entry != null) {
                func.calleeMap = new HashMap<>();
                for (Map.Entry<String, BasicBlock> bbEntry : func.blockMap.entrySet()) {
                    collectInBlock(func, bbEntry.getValue());
                }
                collectInBlock(func, func.ret);
            }
        }
        for (Map.Entry<String, Function> funcEntry : irRoot.funcDef.entrySet()) {
            Function func = funcEntry.getValue();
            //普通local function
            if (func.entry != null) {
                func.effectual = false;
                if (!(func.calleeMap.isEmpty() ||
                        (func.calleeMap.size() == 1 && func.calleeMap.containsKey(func)))) {
                    func.effectual = true;
                    continue;
                }
                for (Map.Entry<String, BasicBlock> bbEntry : func.blockMap.entrySet()) {
                    BasicBlock block = bbEntry.getValue();
                    for (Stmt stmt : block.statements) {
                        ArrayList<Entity> useList = stmt.getUse();
                        if (useList != null) {
                            for (Entity entity : useList) {
                                if (entity instanceof GlobalVar) {
                                    func.effectual = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (func.effectual) {
                        break;
                    }
                }
                if (!func.effectual) {
                    for (Stmt stmt : func.ret.statements) {
                        ArrayList<Entity> useList = stmt.getUse();
                        if (useList != null) {
                            for (Entity entity : useList) {
                                if (entity instanceof GlobalVar) {
                                    func.effectual = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    void collectInBlock(Function func, BasicBlock block) {
        for (Stmt stmt : block.statements) {
            if (stmt instanceof Call callStmt &&
                    callStmt.function.entry != null) {
                if (func.calleeMap.containsKey(callStmt.function)) {
                    func.calleeMap.put(
                            callStmt.function,
                            func.calleeMap.get(callStmt.function) + 1
                    );
                } else {
                    func.calleeMap.put(callStmt.function, 1);
                }
            }
        }
    }

    void eliminateOnFunc(Function func) {
        // used times, defStmt
        HashMap<String, Pair<Integer, Stmt>> workList = new HashMap<>();
        // collect def
        for (Map.Entry<String, BasicBlock> bbEntry : func.blockMap.entrySet()) {
            BasicBlock block = bbEntry.getValue();
            for (Stmt stmt : block.statements) {
                collectDefOnStmt(workList, stmt);
            }
        }
        for (Stmt stmt : func.ret.statements) {
            collectDefOnStmt(workList, stmt);
        }
        // collect use
        for (Map.Entry<String, BasicBlock> bbEntry : func.blockMap.entrySet()) {
            BasicBlock block = bbEntry.getValue();
            for (Stmt stmt : block.statements) {
                collectUseOnStmt(workList, stmt);
            }
            collectUseOnStmt(workList, block.tailStmt);
        }
        for (Stmt stmt : func.ret.statements) {
            collectUseOnStmt(workList, stmt);
        }
        collectUseOnStmt(workList, func.ret.tailStmt);
        while (true) {
            boolean update = false;
            String unused = null;
            Stmt defStmt = null;
            for (Map.Entry<String, Pair<Integer, Stmt>> pairEntry : workList.entrySet()) {
                if (pairEntry.getValue().getFirst() == 0) {
                    unused = pairEntry.getKey();
                    defStmt = pairEntry.getValue().getSecond();
                    update = true;
                    break;
                }
            }
            if (update) {
                defStmt.isDead = true;
                ArrayList<Entity> useList = defStmt.getUse();
                if (useList != null) {
                    for (Entity entity : useList) {
                        if (entity != null) {
                            String name = entity.toString();
                            if (workList.containsKey(name)) {
                                workList.get(name).setFirst(workList.get(name).getFirst() - 1);
                            }
                        }
                    }
                }
                workList.remove(unused);
            } else {
                break;
            }
        }
    }

    private void collectDefOnStmt(HashMap<String, Pair<Integer, Stmt>> workList, Stmt stmt) {
        // 无效call
        if (stmt instanceof Call call && (!call.hasDef()) && !call.function.effectual) {
            call.isDead = true;
        }
        Entity def = stmt.getDef();
        if (def != null) {
            workList.put(def.toString(), new Pair<>(0, stmt));
        }
    }

    private void collectUseOnStmt(HashMap<String, Pair<Integer, Stmt>> workList, Stmt stmt) {
        ArrayList<Entity> useList = stmt.getUse();
        if (useList != null) {
            for (Entity entity : useList) {
                if (entity != null) {
                    String name = entity.toString();
                    if (workList.containsKey(name)) {
                        workList.get(name).setFirst(workList.get(name).getFirst() + 1);
                    }
                }
            }
        }
    }

    void removeDeadStmt(Function func) {
        for (Map.Entry<String, BasicBlock> bbEntry : func.blockMap.entrySet()) {
            BasicBlock block = bbEntry.getValue();
            Iterator<Stmt> iterator = block.statements.iterator();
            while (iterator.hasNext()) {
                Stmt stmt = iterator.next();
                if (stmt.isDead) {
                    if (stmt instanceof Call call && !call.function.effectual) {
                        iterator.remove();
                    }
                }
            }
        }
        Iterator<Stmt> iterator = func.ret.statements.iterator();
        while (iterator.hasNext()) {
            Stmt stmt = iterator.next();
            if (stmt.isDead) {
                if (stmt instanceof Call call && !call.function.effectual) {
                    iterator.remove();
                }
            }
        }
    }
}
