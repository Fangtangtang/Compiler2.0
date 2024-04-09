package midend.optimizer;

import ir.*;
import ir.entity.Storage;
import ir.entity.var.LocalTmpVar;
import ir.function.Function;

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
        noDef,
        oneDef,
        multiDef
    }

    public enum BlockType {
        executable,
        unknown //不可达
    }

    HashSet<LocalTmpVar> varWorkList = null;
    HashSet<BasicBlock> bbWorkList = null;

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
        varWorkList = new HashSet<>();
        bbWorkList = new HashSet<>();
        //initiate
        bbWorkList.add(func.entry);
        // loop until workList cleared
        while ((!bbWorkList.isEmpty()) && (!varWorkList.isEmpty())) {
            BasicBlock bb = bbWorkList.iterator().next();
            if (func.entry.blockType == BlockType.unknown) {
                func.entry.blockType = BlockType.executable;

            }

        }
    }
}
