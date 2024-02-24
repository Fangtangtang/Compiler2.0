package midend;

import ir.IRRoot;
import ir.function.Function;
import midend.optimizer.CFGBuilder;
import midend.optimizer.DomTreeBuilder;

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
        CFGBuilder cfgBuilder = new CFGBuilder(irRoot);
        cfgBuilder.build();
        DomTreeBuilder domTreeBuilder = new DomTreeBuilder(irRoot);
        domTreeBuilder.build();
    }
}
