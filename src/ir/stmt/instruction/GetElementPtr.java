package ir.stmt.instruction;

import ir.IRVisitor;
import ir.entity.*;
import ir.entity.constant.Constant;
import ir.entity.var.LocalTmpVar;
import ir.entity.var.Ptr;
import ir.irType.ArrayType;
import ir.irType.PtrType;
import ir.irType.StructType;
import utility.error.InternalException;

import java.io.PrintStream;

/**
 * @author F
 * 取元素指针指令，转为逐层解析
 * <result> = getelementptr <ty>, ptr <ptrval>{, <ty> <idx>}*
 * .                  ptrval point to         索引类型
 * TODO:都有一个i32 0,表示取当前？
 * +----------------------------------------------
 * |
 * |    char d=b[1];
 * |        ->
 * |    %arrayidx = getelementptr [3 x i8], ptr %b, i32 0, i32 1
 * |    %0 = load i8, ptr %arrayidx, align 1
 * |    store i8 %0, ptr %d, align 1
 * |
 * +-----------------------------------------------
 */
public class GetElementPtr extends Instruction {

    public Storage result;
    public Storage ptrVal;//指针类型
    public Entity idx;

    public GetElementPtr(Storage result,
                         Storage ptrVal,
                         Entity index) {
        this.result = result;
        this.ptrVal = ptrVal;
        this.idx = index;
    }

    //TODO:ptrType?
    @Override
    public void print(PrintStream out) {
        String ptrType;
        if (ptrVal.type instanceof ArrayType arrayType) {
            ptrType = arrayType.type.toString();
        } else if (ptrVal.type instanceof StructType structType) {
            ptrType = structType.toString();
        } else if (ptrVal instanceof Ptr || ptrVal instanceof LocalTmpVar) {
            ptrType = "ptr";
        } else {
            throw new InternalException("unexpected type in GetElementPtr");
        }
        String str;
        if (idx instanceof Constant) {
            str = idx.toString();
        } else {
            str = idx.type + " " + idx;
        }
        out.println("\t" + result.toString() + " = getelementptr " +
                        ptrType + ", ptr " +
                        ptrVal.toString() +
//                ", i32 0, " + str
                        ", " + str
        );
    }

    @Override
    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }
}
