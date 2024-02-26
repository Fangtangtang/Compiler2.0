package ir.entity.var;

import ir.entity.Storage;
import ir.irType.IRType;

/**
 * @author F
 * 局部临时变量，用于load、store
 * 任意类型
 */
public class LocalTmpVar extends Storage {
    public int index;

    public String funcName;

    public LocalTmpVar(IRType type, int num, String currentFuncName) {
        super(type);
        index = num;
        funcName = currentFuncName;
    }

    @Override
    public String toString() {
        return "%" + funcName + this.index;
    }

    @Override
    public String renamed(String rename) {
        return "%" + rename;
    }
}
