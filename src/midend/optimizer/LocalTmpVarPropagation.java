package midend.optimizer;

import ir.*;
import ir.entity.*;
import ir.entity.constant.Constant;
import ir.entity.var.LocalTmpVar;
import ir.entity.var.Ptr;
import ir.function.Function;
import ir.stmt.Stmt;
import ir.stmt.instruction.Instruction;
import ir.stmt.instruction.Load;
import ir.stmt.instruction.Store;

import java.util.*;

/**
 * @author F
 * ir上LocalTmpVar仅def一次，use不超过一次
 * 将LocalTmpVar传播，仅def一次，但可以use多次
 * - 减少对LocalVar和GlobalVar的使用，部分store型成为死代码，为常量传播和死代码消除准备
 * - 减轻后端图染色压力
 */
public class LocalTmpVarPropagation {
    IRRoot irRoot;

    public LocalTmpVarPropagation(IRRoot root) {
        this.irRoot = root;
    }

    /**
     * 执行优化
     * --------------------------------
     * 对每个local函数的每个BB做一次传播
     */
    public void execute() {
        for (Map.Entry<String, Function> entry : irRoot.funcDef.entrySet()) {
            Function func = entry.getValue();
            for (Map.Entry<String, BasicBlock> blockEntry : func.blockMap.entrySet()) {
                BasicBlock block = blockEntry.getValue();
                executeOnBasicBlock(block);
            }
        }
    }

    /**
     * LocalTmpVar存活于单个BB
     * BB内顺序执行，保证传播正确性
     */
    public void executeOnBasicBlock(BasicBlock block) {
        //记录在BB中被赋值的变量，出BB时要将这些变量的valueInBasicBlock清空
        HashSet<Ptr> defs = new HashSet<>();
        ListIterator<Stmt> iterator = block.statements.listIterator();
        while (iterator.hasNext()) {
            Stmt stmt = iterator.next();
            stmt.propagateLocalTmpVar();
            Constant constResult = null;
            if (stmt instanceof Instruction instruction) {
                constResult = instruction.getConstResult();
            }
            //给全局、局部变量赋值
            if (stmt instanceof Store storeStmt) {
                if (storeStmt.pointer instanceof Ptr ptr) {
                    ptr.valueInBasicBlock = (Storage) storeStmt.value;
                    defs.add(ptr);
                }
            }
            //将指向空间的值load
            if (stmt instanceof Load loadStmt) {
                if (loadStmt.pointer instanceof Ptr ptr) {
                    if (loadStmt.result instanceof Ptr ptr1) {
                        ptr1.valueInBasicBlock = ptr.valueInBasicBlock;
                        defs.add(ptr1);
                    } else if (loadStmt.result instanceof LocalTmpVar tmpVar) {
                        tmpVar.valueInBasicBlock = ptr.valueInBasicBlock;
                        if (tmpVar.valueInBasicBlock != null) {
                            iterator.remove();
                        }
                    }
                }
            }
            Entity result = stmt.getDef();
            //给localTmpVar赋常量
            if (constResult != null && result != null) {
                if (result instanceof Ptr ptr) {
                    ptr.valueInBasicBlock = constResult;
                    iterator.remove();
                    iterator.add(new Store(constResult, ptr));
                    defs.add(ptr);
                } else if (result instanceof LocalTmpVar tmpVar) {
                    tmpVar.valueInBasicBlock = constResult;
                    iterator.remove();
                }
            }
        }
        //出BB将valueInBasicBlock赋值全部清空
        for (Ptr ptr : defs) {
            ptr.valueInBasicBlock = null;
        }
    }


}
