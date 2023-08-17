package ir.stmt.instruction;

import ir.IRVisitor;
import ir.entity.Entity;
import ir.entity.constant.ConstInt;
import ir.entity.var.LocalTmpVar;
import ir.irType.IRType;
import ir.irType.IntType;
import ir.irType.PtrType;

import java.io.PrintStream;

/**
 * @author F
 * 一个自定义的内存分配指令
 * 调用_malloc
 */
public class Malloc extends Instruction {

    public LocalTmpVar result;

    public Entity length;
    //字节数
    public Integer mallocSize;

    //构建数组
    //在最开头预留4字节存放数组长度
    public Malloc(LocalTmpVar result,
                  IRType eleType,
                  Entity length) {
        this.result = result;

    }

    //类构建实例
    public Malloc(LocalTmpVar result,
                  int size) {
        this.result = result;
        this.mallocSize = size / 8;
        length = new ConstInt(mallocSize.toString());
    }

    @Override
    public void print(PrintStream out) {
        out.println(
                "\t" + result + " = malloc "
                        + length + ", " + ((PtrType) result.type).type
        );
    }

    @Override
    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }
}
