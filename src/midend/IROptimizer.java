package midend;

import ir.IRRoot;
import ir.function.Function;
import midend.optimizer.*;

import java.util.Map;

/**
 * @author F
 * 整合IR上的优化操作
 */
public class IROptimizer {
    public IRRoot irRoot;

    public IROptimizer(IRRoot irRoot) {
        this.irRoot = irRoot;
    }

    // TODO：
    // 激进死代码消除
    public void execute() {
        FunctionInliningAdv functionInlining = new FunctionInliningAdv(irRoot);
        functionInlining.execute();

        Global2Local global2Local = new Global2Local(irRoot);
        global2Local.execute();

        // mem2reg
        CFGBuilder cfgBuilder = new CFGBuilder(irRoot);
        cfgBuilder.build();
        DomTreeBuilder domTreeBuilder = new DomTreeBuilder(irRoot);
        domTreeBuilder.build();
        Mem2Reg mem2Reg = new Mem2Reg();
        for (Map.Entry<String, Function> entry : irRoot.funcDef.entrySet()) {
            Function func = entry.getValue();
            //普通local function
            if (func.entry != null) {
                mem2Reg.execute(func);
            }
        }

        CCP ccp = new CCP(irRoot);
        ccp.execute();

        LocalTmpVarPropagation localTmpVarPropagation = new LocalTmpVarPropagation(irRoot);
        localTmpVarPropagation.execute();

        DeadCodeEliminator codeEliminator = new DeadCodeEliminator(irRoot);
        codeEliminator.execute();

    }
}
