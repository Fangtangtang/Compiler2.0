package midend.optimizer;

import ir.IRRoot;
import ir.function.Function;
import utility.dominance.DomTree;

import java.util.Map;

/**
 * @author F
 * 对每个函数由CFG构建DomTree
 */
public class DomTreeBuilder {
    IRRoot irRoot;

    public DomTreeBuilder(IRRoot root) {
        this.irRoot = root;
    }

    public void build() {
        for (Map.Entry<String, Function> entry : irRoot.funcDef.entrySet()) {
            Function function = entry.getValue();
            function.domTree = new DomTree(function);
        }
    }

}
