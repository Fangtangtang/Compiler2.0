package ir.irType;

/**
 * @author F
 * 指向数组（数组首地址）的指针
 * 用于struct的array成员
 */
public class ArrayPtrType extends PtrType {
    public ArrayPtrType(ArrayType arrayType) {
        super(arrayType);
    }
}
