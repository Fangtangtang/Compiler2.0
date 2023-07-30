package utility;

import utility.type.*;

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
     * TODO:null type?
     */
    private void addBuildInClass(){
        symbolTable.put("void",new VoidType());
        symbolTable.put("int",new IntType());
        symbolTable.put("bool",new BoolType());
        symbolTable.put("string",new StringType());
        symbolTable.put("null",new NullType());
    }

    //构造时调用私有函数，将内置的类和函数加入symbolTable
    SymbolTable(){

    }
}
