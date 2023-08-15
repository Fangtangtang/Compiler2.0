package ir.stmt.instruction;

import ir.IRVisitor;
import ir.entity.Storage;
import ir.entity.constant.Constant;
import ir.entity.var.GlobalVar;
import ir.irType.ArrayType;
import ir.irType.IRType;
import ir.irType.PtrType;
import ir.irType.StructType;

import java.io.PrintStream;

/**
 * @author F
 * 为全局变量开空间（赋字面量初值）
 * +-----------------------------------------
 * |
 * |    char n;     ->  @n = global i8 0
 * |    int c=1;    ->  @c = global i32 1
 * |
 * +----------------------------------------
 */
public class Global extends Instruction {

    public GlobalVar result;

    //若用字面量初始化，直接初始化
    //否则，用0或null
    public Global(Storage constant,
                  String identifier) {
        result = new GlobalVar(constant, identifier);
    }

    @Override
    public void print(PrintStream out) {
        String str;
        if (result.storage instanceof Constant) {
            str = result.storage.toString();
        } else if (result.storage.type instanceof PtrType) {
            str = "ptr null";
        } else if (result.storage.type instanceof StructType
                || result.storage.type instanceof ArrayType) {
            str = result.storage.type + " zeroinitializer";
        } else {
            str = result.storage.type + " 0";
        }
        out.println(result.toString() + " = global " + str);
    }

    @Override
    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }
}
