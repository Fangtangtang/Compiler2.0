package ast.type;

/**
 * @author F
 * 内置int类型
 */
public class IntType extends Type{
    public IntType(){
        this.typeName=Types.INT;
    }

    @Override
    public String toString() {
        return "int";
    }

    @Override
    public boolean equals(Type other) {
        return (other instanceof IntType);
    }
}
