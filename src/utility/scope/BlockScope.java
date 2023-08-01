package utility.scope;

/**
 * @author F
 * {}显式、if、else隐式的作用域块
 * 记录所在的func和loop
 */
public class BlockScope extends Scope {
    public FuncScope parentFuncScope = null;
    public LoopScope parentLoopScope = null;

    public ClassScope parentClassScope = null;

    public BlockScope(Scope parent,
                      FuncScope parentFuncScope,
                      LoopScope parentLoopScope,
                      ClassScope parentClassScope) {
        super(parent);
        this.parentFuncScope = parentFuncScope;
        this.parentLoopScope = parentLoopScope;
        this.parentClassScope = parentClassScope;
    }
}
