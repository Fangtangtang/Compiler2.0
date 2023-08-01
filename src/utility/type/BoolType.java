package utility.type;

/**
 * @author F
 * 内置bool类型
 */
public class BoolType extends Type{
    public BoolType(){
        this.typeName=Types.BOOL;
    }

    @Override
    public String toString() {
        return "bool";
    }

    @Override
    public boolean equals(Type other) {
        return (other instanceof BoolType);
    }
}
