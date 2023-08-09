package ir.stmt.instruction;

import ir.IRVisitor;
import ir.entity.*;
import ir.entity.ptr.GlobalPtr;
import ir.entity.ptr.LocalPtr;
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
    public Entity result;

    public Alloca(Entity result) {
        this.result = result;
    }

    @Override
    public void print() {
        StringBuilder str = new StringBuilder(result.toString() + " = alloca ");
        if (result instanceof LocalPtr ptr) {
            str.append(ptr.storage.toString());
        } else if (result instanceof GlobalPtr ptr) {
            str.append(ptr.storage.toString());
        } else {
            throw new InternalException("unexpected result in alloca");
        }
        System.out.println(str.toString());
    }

    @Override
    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }
}
