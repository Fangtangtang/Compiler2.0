package backend;

import asm.*;
import asm.instruction.*;
import asm.operand.*;
import ir.BasicBlock;

import java.util.Map;


/**
 * @author F
 * 基础版寄存器分配
 * 所有虚拟寄存器全部放在栈上
 * virtual -> stack
 */
public class RegisterAllocator implements ASMVisitor {
    PhysicalRegMap registerMap;
    Func currentFunc;
    Block currentBlock;
    int stackRegisterSpace;

    @Override
    public void visit(Program program) {

    }

    @Override
    public void visit(Func func) {
        currentFunc = func;
        stackRegisterSpace = 8;
        func.funcBlocks.forEach(
                block -> {
                    visit(block);
                }
        );
        //exit function
        currentFunc.basicSpace = stackRegisterSpace;
        int stackSize = currentFunc.basicSpace
                + (currentFunc.extraParamCnt << 2);
        stackSize = (stackSize + 15) >> 4;
        stackSize <<= 4;
        //进入函数时的额外指令(不加入funcBlocks)
        currentFunc.entry = new Block(func.name);
        currentBlock = currentFunc.entry;
        PhysicalRegister sp = registerMap.getReg("sp");
        PhysicalRegister fp = registerMap.getReg("fp");
        currentBlock.pushBack(
                new ImmBinaryInst(sp, new Imm(-stackSize), sp, ImmBinaryInst.Opcode.addi)
        );
        currentBlock.pushBack(
                new StoreInst(registerMap.getReg("ra"), sp, new Imm(stackSize - 4))
        );
        currentBlock.pushBack(
                new StoreInst(fp, sp, new Imm(stackSize - 8))
        );
        currentBlock.pushBack(
                new ImmBinaryInst(sp, new Imm(stackSize), fp, ImmBinaryInst.Opcode.addi)
        );

    }

    @Override
    public void visit(Block block) {

    }

    @Override
    public void visit(BinaryInst inst) {

    }

    @Override
    public void visit(BranchInst inst) {

    }

    @Override
    public void visit(CallInst inst) {

    }

    @Override
    public void visit(CmpInst inst) {

    }

    @Override
    public void visit(ImmBinaryInst inst) {

    }

    @Override
    public void visit(JumpInst inst) {

    }

    @Override
    public void visit(LoadInst inst) {

    }

    @Override
    public void visit(RetInst inst) {

    }

    @Override
    public void visit(MoveInst inst) {

    }

    @Override
    public void visit(LiInst inst) {

    }

    @Override
    public void visit(StoreInst inst) {

    }

    @Override
    public void visit(EqualZeroInst inst) {

    }

    @Override
    public void visit(LuiInst inst) {

    }

    @Override
    public void visit(GlobalAddrInst inst) {

    }

    @Override
    public void visit(NotInst inst) {

    }

    @Override
    public void visit(Imm operand) {

    }

    @Override
    public void visit(VirtualRegister operand) {

    }
}
