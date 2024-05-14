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

    public void execute() {
        FunctionInliningAdv functionInlining = new FunctionInliningAdv(irRoot);
        functionInlining.execute();
        BasicBlockEliminator eliminator = new BasicBlockEliminator(irRoot);
        eliminator.simplifyBlock();

        Global2Local global2Local = new Global2Local(irRoot);
        global2Local.execute();

        // todo =============================================
        CFGBuilder cfgBuilder = new CFGBuilder(irRoot);
        cfgBuilder.build();
        DomTreeBuilder domTreeBuilder = new DomTreeBuilder(irRoot);
        domTreeBuilder.build();
        Mem2Reg mem2Reg = new Mem2Reg();
        for (Map.Entry<String, Function> entry : irRoot.funcDef.entrySet()) {
            mem2Reg.execute(entry.getValue());
        }
        // todo =============================================

        LocalTmpVarPropagation localTmpVarPropagation = new LocalTmpVarPropagation(irRoot);
        localTmpVarPropagation.execute();


        eliminator.simplifyCtlFlow();

        CCP ccp = new CCP(irRoot);
        ccp.execute();

        DeadCodeEliminator codeEliminator = new DeadCodeEliminator(irRoot);
        codeEliminator.execute();

        eliminator.simplifyBlock();
        eliminator.simplifyCtlFlow();
    }
}
