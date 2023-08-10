package ir.entity.var;

import ir.entity.Storage;
import ir.irType.IRType;

/**
 * @author F
 * 局部临时变量，用于load、store
 * 任意类型
 */
public class LocalTmpVar extends Storage {
    public LocalTmpVar(IRType type) {
        super(type);
    }
}
