package backend;

import asm.PhysicalRegMap;
import asm.section.Text;
import backend.optimizer.CFGBuilder;
import backend.optimizer.GraphColoring;

/**
 * @author F
 * ASM上优化
 */
public class ASMOptimizer {
    public Text text;
    PhysicalRegMap regMap;

    public ASMOptimizer(Text text, PhysicalRegMap regMap) {
        this.text = text;
        this.regMap = regMap;
    }

    public void execute() {
        CFGBuilder cfgBuilder = new CFGBuilder(text);
        cfgBuilder.build();
        text.functions.forEach(
                func -> {
                    GraphColoring graphColoring = new GraphColoring(func, regMap);
                    graphColoring.execute();
                }
        );
    }
}
