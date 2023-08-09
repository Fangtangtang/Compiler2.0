package ir;


import ir.function.*;
import ir.irType.*;

import java.util.ArrayList;

/**
 * @author F
 * IR的根，相当于全局
 * 在IRBuilder工作，先扫一遍全局和symbol table
 * 将全局的类、函数、变量收集在IRRoot下
 * （考虑写在IRBuilder构造函数中）
 */
public class IRRoot {
    //类
    public ArrayList<StructType> classDef = new ArrayList<>();
    //函数
    public ArrayList<Function> funcDef = new ArrayList<>();

    //TODO：变量
    public ArrayList<IRType> varDef = new ArrayList<>();

}
