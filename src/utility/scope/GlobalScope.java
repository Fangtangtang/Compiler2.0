package utility.scope;

/**
 * @author F
 * 全局作用域，
 * program作用域的根，parent=null
 * 按顺序add所有全局变量
 */
public class GlobalScope extends Scope {
    public GlobalScope() {
        super(null);
    }
    public GlobalScope(String str){
        super(null,"");
    }
}
