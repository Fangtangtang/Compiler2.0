package ir.irType;

/**
 * @author F
 * IR类型抽象类
 */
public abstract class IRType {
    public Integer size = 0;

    public IRType() {
    }

    public int getSize() {
        return size;
    }

    @Override
    public abstract String toString();
}
