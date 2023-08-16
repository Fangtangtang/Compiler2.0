package ir.irType;

import utility.error.*;

/**
 * @author F
 * IR的integer
 * i8：bool实际为i1，使用i8方便取数和对齐
 * i8：char
 * i32：int
 */
public class IntType extends IRType {

    public enum TypeName {
        BOOL, CHAR, INT,
        TMP_BOOL    //作为localTmpVar的bool型，经历过trunc
    }

    public TypeName typeName;

    public IntType(TypeName typeName) {
        this.typeName = typeName;
    }

    @Override
    public String toString() {
        String name;
        if (typeName.equals(TypeName.BOOL)) {
            size = 8;
            name = "bool";
        } else if (typeName.equals(TypeName.CHAR)) {
            size = 8;
            name = "char";
        } else if (typeName.equals(TypeName.INT)) {
            size = 32;
            name = "int";
        } else if (typeName.equals(TypeName.TMP_BOOL)) {
            size = 1;
            name = "bool";
        } else {
            throw new InternalException("unexpected IR int");
        }
        return "i" + size;
    }
}
