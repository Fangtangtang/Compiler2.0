package ir.stmt.instruction;

import ir.IRVisitor;
import ir.entity.*;
import ir.entity.ptr.*;

/**
 * @author F
 * 取元素指针指令，转为逐层解析
 * <result> = getelementptr <ty>, ptr <ptrval>{, <ty> <idx>}*
 * . Ptr                ptrval point to         索引类型
 * TODO:都有一个i32 0,表示取当前？
 * +----------------------------------------------
 * |
 * |    char d=b[1];
 * |        ->
 * |    %arrayidx = getelementptr inbounds [3 x i8], ptr %b, i32 0, i32 1
 * |    %0 = load i8, ptr %arrayidx, align 1
 * |    store i8 %0, ptr %d, align 1
 * |
 * +-----------------------------------------------
 */
public class GetElementPtr extends Instruction {

    public Ptr result;
    public Ptr ptrVal;
    public Entity idx;

    public GetElementPtr(Ptr result,
                         Ptr ptrVal,
                         Entity index) {
        this.result = result;
        this.ptrVal = ptrVal;
        this.idx = index;
    }

    @Override
    public void print() {
        System.out.println(result.toString() + " = getelementptr " +
                ptrVal.storage.toString() + ", ptr " +
                ptrVal.toString() + ", i32 0, " +
                idx.toString());
    }

    @Override
    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }
}
