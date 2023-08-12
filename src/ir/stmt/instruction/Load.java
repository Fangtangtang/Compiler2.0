package ir.stmt.instruction;

import ir.IRVisitor;
import ir.entity.Entity;
import ir.entity.var.*;


/**
 * @author F
 * 将指针指向的值赋值给result
 * <result> = load <ty>, ptr <pointer>
 * + -----------------------------------
 * |
 * |    int a = b;
 * |    %0 = load i32, ptr %b
 * |    store i32 %0, ptr %a
 * |
 * |    char a;
 * |    char b = a;
 * |    %a = alloca i8, align 1
 * |    %b = alloca i8, align 1
 * |    %0 = load i8, ptr %a, align 1 //局部变量，char型
 * |    store i8 %0, ptr %b, align 1
 * |
 * + ------------------------------------
 */
public class Load extends Instruction {
    public Entity result;
    public Entity pointer;

    public Load(Entity result,
                Entity pointer) {
        this.result = result;
        this.pointer = pointer;
    }

    @Override
    public void print() {
        System.out.println(result.toString() + " = load "
                + result.toString() + ", ptr " + pointer.toString());
    }

    @Override
    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }
}
