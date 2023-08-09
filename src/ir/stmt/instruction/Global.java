package ir.stmt.instruction;

import ir.IRVisitor;
import ir.entity.constant.Constant;
import ir.entity.ptr.GlobalPtr;
import ir.irType.IRType;

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

    public GlobalPtr result;

    //若用字面量初始化，直接初始化
    //否则，用0或null
    public Global(Constant constant,
                  String identifier) {
        result = new GlobalPtr(constant, identifier);
    }

    @Override
    public void print() {
        System.out.println(result.toString() + " = global " + result.storage.toString());
    }

    @Override
    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }
}
