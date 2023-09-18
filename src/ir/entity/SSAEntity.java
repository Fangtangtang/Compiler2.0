package ir.entity;

import utility.live.GlobalLiveRange;

/**
 * @author F
 * SSA将原来的一个变量拆成多个
 * - constant等，没有LR
 * - 真正变量，带有LR，可能被重命名
 * |     真重命名
 * |     与原先同名
 */
public class SSAEntity {
    public Entity origin;
    public GlobalLiveRange lr = null;

    public SSAEntity(Entity origin) {
        this.origin = origin;
    }

    public SSAEntity(Entity origin,
                     GlobalLiveRange lr) {
        this.origin = origin;
        this.lr = lr;
    }

    @Override
    public String toString() {
        if (lr != null) {
            return origin.renamed(lr.rename);
        }
        return origin.toString();
    }
}
