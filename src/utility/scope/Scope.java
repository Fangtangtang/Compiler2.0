package utility.scope;

import utility.Position;
import utility.SymbolTable;
import utility.error.SemanticException;
import utility.type.ArrayType;
import utility.type.Type;

import java.util.HashMap;

/**
 * @author F
 * 作用域抽象类
 * 所有的Scope共用一个在symbol collect阶段收集的symbol table
 * name2type:Identifier -> Type
 * 所有作用域：用双亲表示法表示的树，Scope为结点类
 */
public abstract class Scope {
    public static SymbolTable symbolTable;
    private final Scope parent;
    public HashMap<String, Type> name2type = new HashMap<>();

    public Scope(Scope parent) {
        this.parent = parent;
    }

    public Scope getParent(){
        return parent;
    }
    public void addIdentifier(String name, Type type, Position pos) {
        if (name2type.containsKey(name)) {
            throw new SemanticException(pos, "multiple definition of " + name);
        }
        Type eleType = symbolTable.getSymbol(type.toString(), pos);
        //特殊处理数组类型变量
        if (type instanceof ArrayType) {
            ((ArrayType) type).clarifyEleType(eleType);
            name2type.put(name, type);
        } else {
            name2type.put(name, eleType);
        }
    }

    public boolean containsIdentifier(String name) {
        if (name2type.containsKey(name)) {
            return true;
        }
        if (parent != null) {
            return parent.containsIdentifier(name);
        }
        return false;
    }

    public Type getType(String name) {
        if (name2type.containsKey(name)) {
            return name2type.get(name);
        }
        if (parent != null) {
            return parent.getType(name);
        }
        return null;
    }
}
