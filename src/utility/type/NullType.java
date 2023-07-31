package utility.type;

/**
 * @author F
 * null
 */
public class NullType extends Type{
    public NullType(){
        this.typeName=Types.NULL;
    }

    @Override
    public String toString() {
        return "null";
    }
}
