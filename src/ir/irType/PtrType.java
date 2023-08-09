package ir.irType;

/**
 * @author F
 * IR指针ptr
 * type指针指向对象的类型
 */
public class PtrType extends IRType {
    public IRType type;

    public PtrType(IRType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type.toString() + "*";
    }
}
