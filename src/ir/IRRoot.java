package ir;


import ir.entity.Storage;
import ir.entity.var.LocalVar;
import ir.function.*;
import ir.irType.*;
import ir.irType.ArrayType;
import ir.irType.IntType;
import ir.irType.NullType;
import ir.irType.VoidType;
import utility.SymbolTable;
import utility.error.InternalException;
import utility.type.*;

import java.io.PrintStream;
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
                addSymbol(name);
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
            addBuiltinFunc("_array_" + name, functionType, new ArrayType());
        }
        //添加malloc
        Function function = new Function(
                new PtrType(),
                "_malloc"
        );
        funcDef.put("_malloc", function);
        //添加字符串二元运算函数
        IntType bool = new IntType(IntType.TypeName.TMP_BOOL);
        //==
        function = new Function(bool, "_string_equal");
        funcDef.put("_string_equal", function);
        //!=
        function = new Function(bool, "_string_notEqual");
        funcDef.put("_string_notEqual", function);
        //<
        function = new Function(bool, "_string_less");
        funcDef.put("_string_less", function);
        //<=
        function = new Function(bool, "_string_lessOrEqual");
        funcDef.put("_string_lessOrEqual", function);
        //>
        function = new Function(bool, "_string_greater");
        funcDef.put("_string_greater", function);
        //>=
        function = new Function(bool, "_string_greaterOrEqual");
        funcDef.put("_string_greaterOrEqual", function);
        //+
        function = new Function(types.get("string"), "_string_add");
        funcDef.put("_string_add", function);
        addBuiltinFuncParam();
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

    private void addSymbol(String typeName) {
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
            }
            //内置的string，有内建方法，加入func
            case "string" -> {
                for (Map.Entry<String, Type> entry : StringType.members.entrySet()) {
                    String name = entry.getKey();
                    FunctionType functionType = (FunctionType) entry.getValue();
                    addBuiltinFunc("_string_" + name, functionType, types.get("string"));
                }
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

    //内建类方法
    private void addBuiltinFunc(String funcName,
                                FunctionType type,
                                IRType irType) {
        Function function = new Function(
                type2irType(type.returnType),
                funcName
        );
        function.parameterList.add(new LocalVar(
                new Storage(new PtrType(irType)),
                "this"
        ));
        type.parameters.forEach(
                param -> function.parameterList.add(new LocalVar(
                        new Storage(type2irType(param.type)),
                        param.name
                ))
        );
        funcDef.put(funcName, function);
    }

    //添加内建函数的参数
    private void addBuiltinFuncParam() {
        LocalVar intParam = new LocalVar(
                new Storage(types.get("int")),
                "n"
        );
        LocalVar strParam = new LocalVar(
                new Storage(types.get("string")),
                "str"
        );
        LocalVar strParam1 = new LocalVar(
                new Storage(types.get("string")),
                "str1"
        );
        Function builtinFunc;
        //void print(string str);
        builtinFunc = funcDef.get("print");
        builtinFunc.parameterList.add(strParam);
        //void println(string str);
        builtinFunc = funcDef.get("println");
        builtinFunc.parameterList.add(strParam);
        //void printInt(int n);
        builtinFunc = funcDef.get("printInt");
        builtinFunc.parameterList.add(intParam);
        //void printlnInt(int n);
        builtinFunc = funcDef.get("printlnInt");
        builtinFunc.parameterList.add(intParam);
        //string toString(int i);
        builtinFunc = funcDef.get("toString");
        builtinFunc.parameterList.add(intParam);
        //char *_malloc(int size)
        builtinFunc = funcDef.get("_malloc");
        builtinFunc.parameterList.add(intParam);
        //str1+str2
        builtinFunc = funcDef.get("_string_add");
        builtinFunc.parameterList.add(strParam);
        builtinFunc.parameterList.add(strParam1);
        //==
        builtinFunc = funcDef.get("_string_equal");
        builtinFunc.parameterList.add(strParam);
        builtinFunc.parameterList.add(strParam1);
        //!=
        builtinFunc = funcDef.get("_string_notEqual");
        builtinFunc.parameterList.add(strParam);
        builtinFunc.parameterList.add(strParam1);
        //<
        builtinFunc = funcDef.get("_string_less");
        builtinFunc.parameterList.add(strParam);
        builtinFunc.parameterList.add(strParam1);
        //<=
        builtinFunc = funcDef.get("_string_lessOrEqual");
        builtinFunc.parameterList.add(strParam);
        builtinFunc.parameterList.add(strParam1);
        //>
        builtinFunc = funcDef.get("_string_greater");
        builtinFunc.parameterList.add(strParam);
        builtinFunc.parameterList.add(strParam1);
        //>=
        builtinFunc = funcDef.get("_string_greaterOrEqual");
        builtinFunc.parameterList.add(strParam);
        builtinFunc.parameterList.add(strParam1);
    }

    public Function getFunc(String funcName) {
        return funcDef.get(funcName);
    }

    public void printStruct(PrintStream output) {
        for (Map.Entry<String, IRType> entry : types.entrySet()) {
            IRType type = entry.getValue();
            if (type instanceof StructType structType) {
                String str = structType.memberInformation();
                if (structType.padding) {
                    output.println(
                            structType + " = type <{ " + str + " }>"
                    );
                } else {
                    output.println(
                            structType + " = type { " + str + " }"
                    );
                }
            }
        }
    }
}
