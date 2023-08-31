package utility;

import utility.error.SemanticException;
import ast.type.*;

import java.util.HashMap;

/**
 * @author F
 * 符号表，保存所有允许前向引用的
 * - 内置类、函数
 * - 自定义类以其所有成员
 * - 自定义函数
 * 在Symbol Collect阶段被收集完全
 */
public class SymbolTable {
    //存储符号名到类型的映射
    public HashMap<String, Type> symbolTable = new HashMap<>();

    /**
     * 添加内置类
     * void int bool string null
     */
    private void addBuildInClass() {
        symbolTable.put("void", new VoidType());
        symbolTable.put("int", new IntType());
        symbolTable.put("bool", new BoolType());
        symbolTable.put("string", new StringType());
        symbolTable.put("null", new NullType());
    }

    //构造时调用私有函数，将内置的类和函数加入symbolTable
    //内置类型的内建函数写在type构造函数中，add到members里
    private void addBuildInFunc() {
        StringType.addBuildInFunc();
        ArrayType.addBuildInFunc();
        //void print(string str);
        FunctionType printFunc = new FunctionType(new VoidType());
        printFunc.parameters.add(
                new ParameterUnit(new StringType(), "str")
        );
        symbolTable.put("print", printFunc);

        //void println(string str);
        FunctionType printlnFunc = new FunctionType(new VoidType());
        printlnFunc.parameters.add(
                new ParameterUnit(new StringType(), "str")
        );
        symbolTable.put("println", printlnFunc);

        //void printInt(int n);
        FunctionType printIntFunc = new FunctionType(new VoidType());
        printIntFunc.parameters.add(
                new ParameterUnit(new IntType(), "n")
        );
        symbolTable.put("printInt", printIntFunc);

        //void printlnInt(int n);
        FunctionType printlnIntFunc = new FunctionType(new VoidType());
        printlnIntFunc.parameters.add(
                new ParameterUnit(new IntType(), "n")
        );
        symbolTable.put("printlnInt", printlnIntFunc);

        //string getString();
        FunctionType getStringFunc = new FunctionType(new StringType());
        symbolTable.put("getString", getStringFunc);

        //int getInt();
        FunctionType getIntFunc = new FunctionType(new IntType());
        symbolTable.put("getInt", getIntFunc);

        //string toString(int i);
        FunctionType toStringFunc = new FunctionType(new StringType());
        toStringFunc.parameters.add(
                new ParameterUnit(new IntType(), "i")
        );
        symbolTable.put("toString", toStringFunc);
    }

    public SymbolTable() {
        addBuildInClass();
        addBuildInFunc();
    }

    public void addSymbol(String name, Type type, Position pos) {
        if (symbolTable.containsKey(name)) {
            throw new SemanticException(pos, "multiple definition of " + name);
        }
        symbolTable.put(name, type);
    }

    public void addClassType(String name, ClassType classType, Position pos) {
        if (!symbolTable.containsKey(name)) {
            throw new SemanticException(pos, String.format("class %s doesn't exist", name));
        }
        if (symbolTable.get(name) instanceof ClassType) {
            symbolTable.put(name, classType);
        } else {
            throw new SemanticException(pos, String.format("%s isn't class", name));
        }
    }

    public void checkMain(Position pos) {
        if (!symbolTable.containsKey("main")) {
            throw new SemanticException(pos, "main function doesn't exist");
        }
        FunctionType mainFunc = (FunctionType) symbolTable.get("main");
        if (!(mainFunc.returnType instanceof IntType) || mainFunc.parameters.size() != 0) {
            throw new SemanticException(pos, "invalid main function");
        }
    }

    public boolean haveSymbol(String name) {
        return symbolTable.containsKey(name);
    }

    public Type getSymbol(String name) {
        return symbolTable.get(name);
    }

    public Type getSymbol(String name, Position pos) {
        if (symbolTable.containsKey(name)) {
            return symbolTable.get(name);
        }
        throw new SemanticException(pos, String.format("type %s doesn't exist", name));
    }

    public void print() {
        for (String key : symbolTable.keySet()) {
            System.out.println(key + ": " + symbolTable.get(key));
        }
    }
}
