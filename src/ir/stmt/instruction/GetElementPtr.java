package ir.stmt.instruction;

import ir.IRVisitor;
import ir.entity.*;
import ir.entity.constant.Constant;
import ir.entity.var.GlobalVar;
import ir.entity.var.LocalTmpVar;
import ir.entity.var.LocalVar;
import ir.entity.var.Ptr;
import ir.irType.ArrayType;
import ir.irType.StructType;
import ir.stmt.Stmt;
import utility.Pair;
import utility.error.InternalException;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author F
 * 取元素指针指令，转为逐层解析
 * <result> = getelementptr <ty>, ptr <ptrval>{, <ty> <idx>}*
 * .                  ptrval point to         索引类型
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

    public LocalTmpVar result;
    public SSAEntity ssaResult;
    public Storage ptrVal;//指针类型
    public SSAEntity ssaPtrVal;
    public Entity idx;
    public SSAEntity ssaIdx;

    public GetElementPtr(LocalTmpVar result,
                         Storage ptrVal,
                         Entity index) {
        this.result = result;
        this.ptrVal = ptrVal;
        this.idx = index;
    }

    @Override
    public void print(PrintStream out) {
        String ptrType;
        if (ptrVal.type instanceof ArrayType arrayType) {
            if (arrayType.dimension == 1) {
                ptrType = arrayType.type.toString();
            } else {
                ptrType = "ptr";
            }
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
                ", " + str
        );
    }

    @Override
    public void printSSA(PrintStream out) {
        String ptrType;
        if (ptrVal.type instanceof ArrayType arrayType) {
            if (arrayType.dimension == 1) {
                ptrType = arrayType.type.toString();
            } else {
                ptrType = "ptr";
            }
        } else if (ptrVal.type instanceof StructType structType) {
            ptrType = structType.toString();
        } else if (ptrVal instanceof Ptr || ptrVal instanceof LocalTmpVar) {
            ptrType = "ptr";
        } else {
            throw new InternalException("unexpected type in GetElementPtr");
        }
        String str;
        if (idx instanceof Constant) {
            str = ssaIdx.toString();
        } else {
            str = idx.type + " " + idx;
        }
        out.println("\t" + ssaResult.toString() + " = getelementptr " +
                ptrType + ", ptr " +
                ssaPtrVal.toString() +
                ", " + str
        );
    }

    @Override
    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }

    @Override
    public ArrayList<Entity> getUse() {
        ArrayList<Entity> ret = new ArrayList<>();
        ret.add(idx);
        ret.add(ptrVal);
        return ret;
    }

    @Override
    public Entity getDef() {
        return result;
    }

    @Override
    public void promoteGlobalVar() {
        if (idx instanceof GlobalVar globalVar && globalVar.convertedLocalVar != null) {
            idx = globalVar.convertedLocalVar;
        }
        if (ptrVal instanceof GlobalVar globalVar && globalVar.convertedLocalVar != null) {
            ptrVal = globalVar.convertedLocalVar;
        }
    }

    @Override
    public void propagateLocalTmpVar() {
        if (idx instanceof Ptr ptr) {
            idx = ptr.valueInBasicBlock == null ? idx : ptr.valueInBasicBlock;
        } else if (idx instanceof LocalTmpVar tmpVar) {
            idx = tmpVar.valueInBasicBlock == null ? idx : tmpVar.valueInBasicBlock;
        }
        if (ptrVal instanceof Ptr ptr) {
            ptrVal = ptr.valueInBasicBlock == null ? ptrVal : ptr.valueInBasicBlock;
        } else if (ptrVal instanceof LocalTmpVar tmpVar) {
            ptrVal = tmpVar.valueInBasicBlock == null ? ptrVal : tmpVar.valueInBasicBlock;
        }
    }

    @Override
    public Pair<Stmt, LocalTmpVar> creatCopy(String suffix) {
        LocalTmpVar newResult = new LocalTmpVar(result.type, result.identity + suffix);
        Stmt stmt = new GetElementPtr(
                newResult, (Storage) ptrVal, idx
        );
        return new Pair<>(stmt, newResult);
    }

    @Override
    public void replaceUse(HashMap<LocalTmpVar, Storage> copyMap, HashMap<LocalVar, LocalVar> curAllocaMap) {
        idx = replace(idx, copyMap, curAllocaMap);
        ptrVal = (Storage) replace(ptrVal, copyMap, curAllocaMap);
    }

    @Override
    public Constant getConstResult() {
        return null;
    }

    @Override
    public void setUse(ArrayList<SSAEntity> list) {
        ssaIdx = list.get(0);
        ssaPtrVal = list.get(1);
    }

    @Override
    public void setDef(SSAEntity entity) {
        ssaResult = entity;
    }

    @Override
    public ArrayList<SSAEntity> getSSAUse() {
        ArrayList<SSAEntity> ret = new ArrayList<>();
        ret.add(ssaIdx);
        ret.add(ssaPtrVal);
        return ret;
    }

    @Override
    public SSAEntity getSSADef() {
        return ssaResult;
    }


}
