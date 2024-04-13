package ir.entity.var;

import ir.entity.Storage;
import ir.irType.IRType;

import java.util.Objects;

/**
 * @author F
 * 局部临时变量，用于load、store
 * 任意类型
 */
public class LocalTmpVar extends Storage {
    public int index = -1;

    public String funcName;
    public String identity;

    //在当前BB中的最新赋值（实际仅一次赋值）
    public Storage valueInBasicBlock = null;

    @Override
    public int hashCode() {
        return Objects.hash(identity);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        LocalTmpVar other = (LocalTmpVar) obj;
        return Objects.equals(identity, other.identity);
    }

    public LocalTmpVar(IRType type, int num, String currentFuncName) {
        super(type);
        identity = currentFuncName + num;
    }

    public LocalTmpVar(IRType type, String identity) {
        super(type);
        this.identity = identity;
    }

    @Override
    public String toString() {
        return "%" + identity;
    }

    @Override
    public String renamed(String rename) {
        return "%" + rename;
    }
}
