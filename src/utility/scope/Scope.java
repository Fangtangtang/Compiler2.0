package utility.scope;

import utility.Position;
import utility.SymbolTable;
import utility.error.SemanticException;
import utility.type.ArrayType;
import utility.type.ClassType;
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

    public Scope getParent() {
        return parent;
    }

    //变量名和类名（SymbolTable中）可以重复
    public void addIdentifier(String name, Type type, Position pos) {
        //当前作用域内已定义
        if (name2type.containsKey(name)) {
            throw new SemanticException(pos, "multiple definition of " + name);
        }
        //不可与函数重名，可与类重名
        if (Scope.symbolTable.haveSymbol(name)
                && !(Scope.symbolTable.getSymbol(name) instanceof ClassType)) {
            throw new SemanticException(pos, name + " is function name");
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

//    public boolean containsIdentifier(String name) {
//        if (name2type.containsKey(name)) {
//            return true;
//        }
//        if (parent != null) {
//            return parent.containsIdentifier(name);
//        }
//        return false;
//    }

    public Type getType(String name) {
        //特殊作用域，从类成员中找
        if (this instanceof ClassScope) {
            if (((ClassScope) this).classType.classMembers.containsKey(name)) {
                return ((ClassScope) this).classType.classMembers.get(name);
            }
        } else {
            //先在作用域内找
            if (name2type.containsKey(name)) {
                return name2type.get(name);
            }
            //全局作用域，再在symbol table找
            if (this instanceof GlobalScope) {
                if (Scope.symbolTable.haveSymbol(name)) {
                    return Scope.symbolTable.getSymbol(name);
                }
            } else {
                return parent.getType(name);
            }
        }
        return null;
    }
}
