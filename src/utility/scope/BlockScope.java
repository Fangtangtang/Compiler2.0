package utility.scope;

/**
 * @author F
 * {}显式、if、else隐式的作用域块
 */
public class BlockScope extends Scope {
    public BlockScope(Scope parent) {
        super(parent);
    }
}
