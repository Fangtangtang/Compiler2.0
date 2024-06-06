package backend.optimizer;

import asm.Func;
import asm.section.Text;

/**
 * @author F
 * BlockInlining
 * <p>
 * 若Block仅有一个后继，直接将后继内联
 * （在asm上做这个免除ir上phi和用于标记的控制流的复杂处理）
 */
public class BlockInlining {
    Text text;

    public BlockInlining(Text text) {
        this.text = text;
    }

    public void execute() {
        text.functions.forEach(
                this::inliningOnFunc
        );
    }

    private void inliningOnFunc(Func func) {

    }

    /**
     * [def] dead block
     * - no inst
     * - unreachable
     * @param func asm function
     */
    private void eliminateDeadBlock(Func func) {

    }
}
