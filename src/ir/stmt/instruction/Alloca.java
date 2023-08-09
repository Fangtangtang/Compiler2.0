package ir.stmt.instruction;

import ir.IRVisitor;
import ir.entity.*;
import ir.entity.ptr.GlobalPtr;
import ir.entity.ptr.LocalPtr;
import ir.entity.ptr.Ptr;
import utility.error.InternalException;


/**
 * @author F
 * 分配内存空间的指令
 * <result> = alloca <type>
 * + --------------------------------
 * |
 * | char a;    ->  %a = alloca i8
 * | int b;     ->  %b = alloca i32,
 * |
 * +---------------------------------
 * result为指针类型(GlobalPtr\LocalPtr)
 * type为指针指向对象的类型
 */
public class Alloca extends Instruction {
    public Ptr result;

    public Alloca(Ptr result) {
        this.result = result;
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
