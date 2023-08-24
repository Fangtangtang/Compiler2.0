package asm;

import asm.instruction.*;
import asm.operand.*;

/**
 * @author F
 * 遍历ASM的接口
 */
public interface ASMVisitor {
    void visit(Program program);

    void visit(Block block);

    void visit(Func func);

    //instruction
    void visit(BinaryInst inst);

    void visit(BranchInst inst);

    void visit(CallInst inst);

    void visit(CmpInst inst);

    void visit(ImmBinaryInst inst);

    void visit(JumpInst inst);

    void visit(LoadInst inst);

    void visit(RetInst inst);

    void visit(MoveInst inst);

    void visit(LiInst inst);

    void visit(LoadAddrInst inst);

    void visit(StoreInst inst);

    void visit(EqualZeroInst inst);

    void visit(LuiInst inst);

    void visit(GlobalAddrInst inst);

    //operand
    void visit(Imm operand);

    void visit(VirtualRegister operand);

}
