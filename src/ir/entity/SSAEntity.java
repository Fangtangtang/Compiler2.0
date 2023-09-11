package ir.entity;

import utility.GlobalLiveRange;

/**
 * @author F
 * SSA将原来的一个变量拆成多个
 */
public class SSAEntity {
    Entity origin;
    public GlobalLiveRange lr = null;
}
