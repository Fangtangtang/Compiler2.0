package ir.irType;

/**
 * @author F
 * IR类型抽象类
 */
public abstract class IRType {
    public Integer size = 0;

    public IRType() {
    }

    @Override
    public abstract String toString();
}
