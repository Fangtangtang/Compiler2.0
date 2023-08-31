package ast.type;

/**
 * @author F
 * 内置void类型
 */
public class VoidType extends Type {
    public VoidType() {
        this.typeName = Types.VOID;
    }

    @Override
    public String toString() {
        return "void";
    }

    @Override
    public boolean equals(Type other) {
        return (other instanceof VoidType || other instanceof NullType);
    }
}
