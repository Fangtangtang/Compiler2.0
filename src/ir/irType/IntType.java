package ir.irType;

import utility.error.*;

/**
 * @author F
 * IR的integer
 * i8：bool
 * i8：char
 * i32：int
 */
public class IntType extends IRType {

    public enum TypeName {
        BOOL, CHAR, INT
    }

    TypeName typeName;

    public IntType(TypeName typeName) {
        this.typeName = typeName;
    }

    @Override
    public String toString() {
        int size;
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
        } else {
            throw new InternalException("unexpected IR int");
        }
        return "i" + size + "(" + name + ")";
    }
}
