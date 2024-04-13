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
        FunctionInlining functionInlining = new FunctionInlining(irRoot);
        functionInlining.execute();
        BasicBlockEliminator eliminator = new BasicBlockEliminator(irRoot);
        eliminator.execute();

        Global2Local global2Local = new Global2Local(irRoot);
        global2Local.execute();

        LocalTmpVarPropagation localTmpVarPropagation = new LocalTmpVarPropagation(irRoot);
        localTmpVarPropagation.execute();

//        ConditionalConstantPropagation conditionalConstantPropagation = new ConditionalConstantPropagation(irRoot);
//        conditionalConstantPropagation.execute();
    }
}
