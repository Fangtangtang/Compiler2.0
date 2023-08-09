package ir.entity.register;

import ir.entity.Entity;
import ir.irType.IRType;

/**
 * @author F
 * 派生自Entity的Register
 * 寄存器分配，避免重名（使用index区分寄存器）
 */
public class Register extends Entity {
    //总寄存器数目
    private static int cnt = -1;
    //该寄存器下标
    private final int regIndex;
    private final String identity;

    public Register(IRType type,
                    String identity) {
        super(type);
        ++cnt;
        this.regIndex = cnt;
        this.identity = identity;
    }

    @Override
    public String toString() {
        return "register " + regIndex + ": " + identity + " " + type.toString();
    }
}
