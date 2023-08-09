package utility.scope;

import ir.entity.Storage;
import utility.*;
import utility.error.SemanticException;
import utility.type.*;

import java.util.HashMap;

/**
 * @author F
 * 作用域抽象类
 * 所有的Scope共用一个在symbol collect阶段收集的symbol table
 * name2type:Identifier ->  Type
 * nameMap:name ->  rename
 * name2mem:Identifier  ->  Storage（内存空间）
 * 所有作用域：用双亲表示法表示的树，Scope为结点类
 */
public abstract class Scope {
    public static SymbolTable symbolTable;
    private final Scope parent;
    public HashMap<String, Type> name2type = new HashMap<>();
    public HashMap<String, String> nameMap = new HashMap<>();
    public HashMap<String, Storage> name2mem = new HashMap<>();

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

    public Type getType(String name) {
        //特殊作用域，从类成员中找
        if (this instanceof ClassScope) {
            if (((ClassScope) this).classType.classMembers.containsKey(name)) {
                return ((ClassScope) this).classType.classMembers.get(name);
            }
        } else {
            //在作用域内找
            if (name2type.containsKey(name)) {
                return name2type.get(name);
            }
        }
        //全局作用域，再在symbol table找
        if (this instanceof GlobalScope) {
            if (Scope.symbolTable.haveSymbol(name)) {
                return Scope.symbolTable.getSymbol(name);
            }
        } else {
            return parent.getType(name);
        }
        return null;
    }

    public ClassScope getParentClassScope() {
        if (this instanceof BlockScope) {
            return ((BlockScope) this).parentClassScope;
        }
        if (this instanceof LoopScope) {
            return ((LoopScope) this).parentClassScope;
        }
        if (this instanceof FuncScope) {
            return ((FuncScope) this).parentClassScope;
        }
        if (this instanceof ClassScope) {
            return (ClassScope) this;
        }
        return null;
    }

    public FuncScope getParentFuncScope() {
        if (this instanceof FuncScope) {
            return (FuncScope) this;
        }
        if (this instanceof BlockScope) {
            return ((BlockScope) this).parentFuncScope;
        }
        if (this instanceof LoopScope) {
            return ((LoopScope) this).parentFuncScope;
        }
        return null;
    }

    public LoopScope getParentLoopScope() {
        if (this instanceof BlockScope) {
            return ((BlockScope) this).parentLoopScope;
        }
        if (this instanceof LoopScope) {
            return (LoopScope) this;
        }
        return null;
    }
}
