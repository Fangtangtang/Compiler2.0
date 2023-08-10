package ir.stmt.instruction;

import ir.IRVisitor;
import ir.entity.*;
import ir.entity.var.*;
import ir.irType.IRType;


/**
 * @author F
 * 局部变量分配内存空间的指令
 * <result> = alloca <type>
 * + --------------------------------
 * |
 * | char a;    ->  %a = alloca i8
 * | int b;     ->  %b = alloca i32,
 * |
 * +---------------------------------
 * result为指针类型(LocalVar)
 * type为指针指向对象的类型
 */
public class Alloca extends Instruction {
    public LocalVar result;

    public Alloca(IRType irType,
                  String identifier) {
        this.result = new LocalVar(new Storage(irType), identifier);
    }

    @Override
    public void print() {
        System.out.println(result.toString() + " = alloca " + result.storage.toString());
    }

    @Override
    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }
}
