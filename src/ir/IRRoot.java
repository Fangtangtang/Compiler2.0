package ir;


import ir.function.*;
import ir.irType.*;
import ir.irType.IntType;
import ir.irType.NullType;
import ir.irType.VoidType;
import utility.SymbolTable;
import utility.error.InternalException;
import utility.type.*;

import java.util.*;


/**
 * @author F
 * IR的根，相当于全局
 * 在IRBuilder工作，先扫一遍全局和symbol table
 * 将全局的类、函数、变量（原本已经收集于symbol table）收集在IRRoot下
 */
public class IRRoot {
    // 类
    // 包括自定义的类和内置类，转为类型名称到IRType的映射
    public HashMap<String, IRType> types = new HashMap<>();

    //函数
    //转为函数名（避免重名后）到函数的映射
    public HashMap<String, Function> funcDef = new HashMap<>();

    public BasicBlock globalVarDefBlock;

    public GlobalVarInitFunction globalVarInitFunction;

    /**
     * 将AST上的符号表重新转化为IR上的相应类型
     *
     * @param table sema阶段收集的符号表
     */
    public IRRoot(SymbolTable table) {
        //先将所有的非函数的类加入表
        for (Map.Entry<String, Type> entry : table.symbolTable.entrySet()) {
            String name = entry.getKey();
            Type type = entry.getValue();
            if (!(type instanceof FunctionType)) {
                addSymbol(name, type);
            }
        }
        for (Map.Entry<String, Type> entry : table.symbolTable.entrySet()) {
            String name = entry.getKey();
            Type type = entry.getValue();
            if (type instanceof FunctionType) {
                //全局的函数（包括内建），直接使用原有名
                addFunc(name, (FunctionType) type);
            } else {
                //自定义类和内置类，类名直接使用原有名
                addType(name, type);
            }
        }
        //特殊函数main，直接覆盖原先建的main
        funcDef.put("main", new MainFunc());
        //添加array的方法
        for (Map.Entry<String, Type> entry : utility.type.ArrayType.members.entrySet()) {
            String name = entry.getKey();
            FunctionType functionType = (FunctionType) entry.getValue();
            addFunc("_array_" + name, functionType);
        }
    }

    /**
     * 将type转化为用在IR上的IRType
     *
     * @param type Type
     * @return IRType
     */
    public IRType type2irType(Type type) {
        if (type instanceof utility.type.IntType) {
            return types.get("int");
        } else if (type instanceof BoolType) {
            return types.get("bool");
        } else if (type instanceof utility.type.VoidType) {
            return types.get("void");
        } else if (type instanceof StringType) {
            //字符串：指向一维字符数组的指针
            return types.get("string");
        } else if (type instanceof utility.type.ArrayType arrayType) {
            return new ir.irType.ArrayType(
                    type2irType(arrayType.eleType),
                    arrayType.dimensions
            );
        } else if (type instanceof ClassType classType) {
            return types.get(classType.name);
        } else {
            throw new InternalException("convert failed, unexpected type");
        }
    }

    private void addSymbol(String typeName, Type type) {
        switch (typeName) {
            case "void" -> types.put(typeName, new VoidType());
            case "int" -> types.put(typeName, new IntType(IntType.TypeName.INT));
            case "bool" -> types.put(typeName, new IntType(IntType.TypeName.BOOL));
            case "string" -> types.put(typeName,
                    new ir.irType.ArrayType(
                            new IntType(IntType.TypeName.CHAR),
                            1
                    )
            );
            case "null" -> types.put(typeName, new NullType());
            default -> types.put(typeName, new StructType(typeName));
        }
    }

    /**
     * 改写 + 提取类函数
     *
     * @param typeName 类名
     * @param type     类信息
     */
    private void addType(String typeName, Type type) {
        switch (typeName) {
            //没有内建函数的内置类
            case "void", "int", "bool", "null" -> {
                return;
            }
            //内置的string，有内建方法，加入func
            case "string" -> {
                for (Map.Entry<String, Type> entry : StringType.members.entrySet()) {
                    String name = entry.getKey();
                    FunctionType functionType = (FunctionType) entry.getValue();
                    addFunc("_string_" + name, functionType);
                }
                return;
            }
            default -> {
                if (type instanceof ClassType classType) {
                    StructType currentStruct = (StructType) types.get(classType.name);
                    for (Map.Entry<String, Type> entry : classType.classMembers.entrySet()) {
                        String name = entry.getKey();
                        Type memberType = entry.getValue();
                        //自定义类的所有函数
                        if (memberType instanceof FunctionType functionType) {
                            addFunc(typeName + "." + name, functionType);
                            currentStruct.members.put(name, -1);//fake index
                        }
                        //自定义类的所有成员变量
                        else {
                            currentStruct.addMember(name, type2irType(memberType));
                        }
                    }
                    //自定义类的构造函数（同类名）
                    //无参数，返回void
                    if (classType.constructor != null) {
                        Function function = new Function(
                                new VoidType(), typeName
                        );
                        funcDef.put(typeName, function);
                    }
                } else {
                    throw new InternalException("unexpected type");
                }
            }
        }
    }

    /**
     * @param funcName 函数名
     * @param type     函数信息
     */
    private void addFunc(String funcName, FunctionType type) {
        Function function = new Function(
                type2irType(type.returnType),
                funcName
        );
        funcDef.put(funcName, function);
    }

    public Function getFunc(String funcName) {
        return funcDef.get(funcName);
    }
}
