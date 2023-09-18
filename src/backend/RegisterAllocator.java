package backend;

import asm.*;
import asm.instruction.*;
import asm.operand.*;
import asm.section.Text;
import utility.Pair;
import utility.error.InternalException;

import java.util.HashMap;

/**
 * @author F
 * 基础版寄存器分配
 * 所有虚拟寄存器全部放在栈上
 * virtualReg -> stackReg
 */
public class RegisterAllocator implements ASMVisitor {
    PhysicalRegMap registerMap;
    PhysicalRegister a0, a1, a2, a3, a4;
    PhysicalRegister t0, t1, t2;
    Imm zero = new Imm(0);
    //函数重写替换
    Text text = new Text();
    Func currentFunc;
    Block currentBlock;
    int stackRegisterSpace;
    HashMap<String, StackRegister> stackRegMap = new HashMap<>();

    StackRegister virtual2Stack(VirtualRegister virtualRegister) {
        if (stackRegMap.containsKey(virtualRegister.name)) {
            return stackRegMap.get(virtualRegister.name);
        }
        stackRegisterSpace += virtualRegister.size;
        StackRegister stackRegister = new StackRegister(stackRegisterSpace, virtualRegister.size);
        stackRegMap.put(virtualRegister.name, stackRegister);
        return stackRegister;
    }

    void loadStackReg(PhysicalRegister reg, StackRegister stackRegister) {
        reg.size = stackRegister.size;
        Pair<Register, Imm> pair = getRegAddress(stackRegister);
        currentBlock.pushBack(
                new LoadInst(pair.getFirst(), reg, pair.getSecond())
        );
    }

    void storeStackReg(PhysicalRegister reg, StackRegister stackRegister) {
        reg.size = stackRegister.size;
        Pair<Register, Imm> pair = getRegAddress(stackRegister);
        currentBlock.pushBack(
                new StoreInst(reg, pair.getFirst(), pair.getSecond())
        );
    }

    /**
     * 获得load、store需要的register的位置
     * - offset不超过12位，直接fp(值为address)+offset
     * - 超过12位，直接寻址(计算出直接地址，存到临时量reg(t0))
     *
     * @param register 栈上虚拟寄存器
     * @return <baseReg,offset>
     */
    private Pair<Register, Imm> getRegAddress(StackRegister register) {
        if (register.offset < (1 << 11)) {
            return new Pair<>(registerMap.getReg("fp"), new Imm(-register.offset));
        }
        t0.size = 4;
        //offset
        Register num = (Register) number2operand(register.offset);
        //计算真正地址
        currentBlock.pushBack(
                new BinaryInst(registerMap.getReg("fp"), num, t0, BinaryInst.Opcode.sub)
        );
        return new Pair<>(t0, zero);
    }

    /**
     * 将int型数值转化为对应的操作数
     * 利用physical register a6计算
     *
     * @param num 数值
     * @return operand
     */
    Operand number2operand(int num) {
        //直接用imm
        if (num < (1 << 11)) {
            return new Imm(num);
        }
        //先lui，如果低位非0，addi
        else {
            t0.size = 4;
            currentBlock.pushBack(
                    new LuiInst(t0, new Imm((num >> 12)))
            );
            if ((num & 0xFFF) != 0) {
                currentBlock.pushBack(
                        new ImmBinaryInst(
                                t0,
                                new Imm(num & 0xFFF),
                                t0,
                                ImmBinaryInst.Opcode.addi
                        )
                );
            }
            return t0;
        }
    }

    public RegisterAllocator(PhysicalRegMap regMap) {
        this.registerMap = regMap;
        a0 = regMap.getReg("a0");
        a1 = regMap.getReg("a1");
        a2 = regMap.getReg("a2");
        a3 = regMap.getReg("a3");
        a4 = regMap.getReg("a4");
        t0 = regMap.getReg("t0");
        t1 = regMap.getReg("t1");
        t2 = regMap.getReg("t2");

    }

    @Override
    public void visit(Program program) {
        program.text.functions.forEach(
                this::visit
        );
        program.text = text;
    }

    @Override
    public void visit(Func func) {
        //重写函数
        currentFunc = new Func(func.name);
        currentFunc.extraParamCnt = func.extraParamCnt;//照搬
        text.functions.add(currentFunc);
        stackRegisterSpace = 8;
        stackRegMap = new HashMap<>();
        func.funcBlocks.forEach(
                block -> {
                    currentBlock = new Block(block.name);
                    currentFunc.funcBlocks.add(currentBlock);
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
//        func.getParams.forEach(
//                instruction -> instruction.accept(this)
//        );
        //出函数的指令
        currentBlock = currentFunc.funcBlocks.get(currentFunc.funcBlocks.size() - 1);
        currentBlock.pushBack(
                new LoadInst(sp, registerMap.getReg("ra"), new Imm(stackSize - 4))
        );
        currentBlock.pushBack(
                new LoadInst(sp, fp, new Imm(stackSize - 8))
        );
        currentBlock.pushBack(
                new ImmBinaryInst(sp, new Imm(stackSize), sp, ImmBinaryInst.Opcode.addi)
        );
        currentBlock.pushBack(
                new RetInst()
        );
    }

    @Override
    public void visit(Block block) {
        block.instructions.forEach(
                inst -> inst.accept(this)
        );
    }

    Pair<Operand, Operand> getReg(Operand rs1, Operand rs2) {
        Operand rs1Reg, rs2Reg;
        if (rs1 instanceof VirtualRegister r1) {
            rs1 = virtual2Stack(r1);
            loadStackReg(a0, (StackRegister) rs1);
            rs1Reg = a0;
        } else if (rs1 instanceof Imm imm1) {
            currentBlock.pushBack(
                    new LiInst(a1, imm1)
            );
            rs1Reg = a1;
        } else {
            rs1Reg = rs1;
        }
        if (rs2 instanceof VirtualRegister r2) {
            rs2 = virtual2Stack(r2);
            loadStackReg(a2, (StackRegister) rs2);
            rs2Reg = a2;
        } else if (rs2 instanceof Imm imm2) {
            currentBlock.pushBack(
                    new LiInst(a3, imm2)
            );
            rs2Reg = a3;
        } else {
            rs2Reg = rs2;
        }
        return new Pair<>(rs1Reg, rs2Reg);
    }

    @Override
    public void visit(BinaryInst inst) {
        Pair<Operand, Operand> pair = getReg(inst.rs1, inst.rs2);
        Operand rs1Reg = pair.getFirst(), rs2Reg = pair.getSecond();
        if (inst.rd instanceof VirtualRegister rd) {
            inst.rd = virtual2Stack(rd);
            currentBlock.pushBack(
                    new BinaryInst(rs1Reg, rs2Reg, a0, inst.op)
            );
            storeStackReg(a0, (StackRegister) inst.rd);
        } else if (inst.rd instanceof PhysicalRegister reg) {
            currentBlock.pushBack(
                    new BinaryInst(rs1Reg, rs2Reg, inst.rd, inst.op)
            );
        } else {
            throw new InternalException("unexpected in BinaryInst");
        }
    }

    @Override
    public void visit(BranchInst inst) {
        PhysicalRegister rs1Reg;
        if (inst.rs1 instanceof VirtualRegister rs1) {
            inst.rs1 = virtual2Stack(rs1);
            loadStackReg(a0, (StackRegister) inst.rs1);
            rs1Reg = a0;
        } else if (inst.rs1 instanceof PhysicalRegister rs1) {
            rs1Reg = rs1;
        } else {
            throw new InternalException("unexpected in BranchInst");
        }
        currentBlock.pushBack(
                new BranchInst(rs1Reg, inst.desName)
        );
    }

    @Override
    public void visit(CallInst inst) {
        currentBlock.pushBack(inst);
    }

    @Override
    public void visit(CmpInst inst) {
        Pair<Operand, Operand> pair = getReg(inst.rs1, inst.rs2);
        Operand rs1Reg = pair.getFirst(), rs2Reg = pair.getSecond();
        if (inst.rd instanceof VirtualRegister rd) {
            inst.rd = virtual2Stack(rd);
            currentBlock.pushBack(
                    new CmpInst(rs1Reg, rs2Reg, a4, inst.op)
            );
            storeStackReg(a4, (StackRegister) inst.rd);
        } else if (inst.rd instanceof PhysicalRegister reg) {
            currentBlock.pushBack(
                    new CmpInst(rs1Reg, rs2Reg, inst.rd, inst.op)
            );
        } else {
            throw new InternalException("unexpected in CmpInst");
        }
    }

    @Override
    public void visit(ImmBinaryInst inst) {
        PhysicalRegister rs1Reg;
        if (inst.rs1 instanceof VirtualRegister rs1) {
            inst.rs1 = virtual2Stack(rs1);
            loadStackReg(a0, (StackRegister) inst.rs1);
            rs1Reg = a0;
        } else if (inst.rs1 instanceof PhysicalRegister rs1) {
            rs1Reg = rs1;
        } else {
            throw new InternalException("unexpected in ImmBinaryInst");
        }
        currentBlock.pushBack(
                new ImmBinaryInst(rs1Reg, inst.imm, rs1Reg, inst.op)
        );
        if (inst.rd instanceof VirtualRegister rd) {
            inst.rd = virtual2Stack(rd);
            storeStackReg(rs1Reg, (StackRegister) inst.rd);
        } else if (inst.rd instanceof PhysicalRegister reg) {
            currentBlock.pushBack(
                    new MoveInst(reg, rs1Reg)
            );
        } else {
            throw new InternalException("unexpected in ImmBinaryInst");
        }
    }

    @Override
    public void visit(JumpInst inst) {
        currentBlock.pushBack(inst);
    }

    @Override
    public void visit(LoadInst inst) {
        if (inst.complete) {
            currentBlock.pushBack(inst);
            return;
        }
        PhysicalRegister rs1Reg;
        Imm imm1 = zero;
        //取到pointer地址载入rs1Reg,imm1
        if (inst.rs1 instanceof PhysicalRegister physicalReg) {
            rs1Reg = physicalReg;
        } else {
            inst.rs1 = virtual2Stack((VirtualRegister) inst.rs1);
            Pair<Register, Imm> addr = getRegAddress((StackRegister) inst.rs1);
            if (inst.needPointerAddr) {
                rs1Reg = (PhysicalRegister) addr.getFirst();
                imm1 = addr.getSecond();
            } else {
                currentBlock.pushBack(
                        new LoadInst((PhysicalRegister) addr.getFirst(), a0, addr.getSecond())
                );
                rs1Reg = a0;
            }
        }
        //值载入a1
        a1.size = inst.rd.size;
        currentBlock.pushBack(
                new LoadInst(rs1Reg, a1, imm1)
        );
        PhysicalRegister rdReg;
        Imm imm2 = zero;
        //取到result地址载入rs2Reg,imm2
        if (inst.rd instanceof PhysicalRegister physicalReg) {
            rdReg = physicalReg;
        } else {
            inst.rd = virtual2Stack((VirtualRegister) inst.rd);
            Pair<Register, Imm> addr = getRegAddress((StackRegister) inst.rd);
            rdReg = (PhysicalRegister) addr.getFirst();
            imm2 = addr.getSecond();
        }
        currentBlock.pushBack(
                new StoreInst(a1, rdReg, imm2)
        );
    }

    @Override
    public void visit(RetInst inst) {
    }

    @Override
    public void visit(MoveInst inst) {
        if (inst.rs1 instanceof PhysicalRegister reg1 &&
                inst.rd instanceof PhysicalRegister reg2) {
            currentBlock.pushBack(inst);
            return;
        }
        if (inst.rs1 instanceof VirtualRegister rs1) {
            inst.rs1 = virtual2Stack(rs1);
            loadStackReg(t2, (StackRegister) inst.rs1);
            if (inst.rd instanceof VirtualRegister rd) {
                inst.rd = virtual2Stack(rd);
                storeStackReg(t2, (StackRegister) inst.rd);
            } else {
                currentBlock.pushBack(
                        new MoveInst(inst.rd, t2)
                );
            }
        } else {
            if (inst.rd instanceof VirtualRegister rd) {
                inst.rd = virtual2Stack(rd);
                storeStackReg((PhysicalRegister) inst.rs1, (StackRegister) inst.rd);
            } else {
                throw new InternalException("unexpected type in move instruction");
            }
        }

    }

    @Override
    public void visit(LiInst inst) {
        if (inst.rd instanceof VirtualRegister rd) {
            currentBlock.pushBack(
                    new LiInst(a0, inst.imm)
            );
            inst.rd = virtual2Stack(rd);
            storeStackReg(a0, (StackRegister) inst.rd);
        } else {
            currentBlock.pushBack(inst);
        }
    }

    @Override
    public void visit(StoreInst inst) {
        if (inst.complete) {
            currentBlock.pushBack(inst);
            return;
        }
        //a0:存放待存的数
        if (inst.rs1 instanceof VirtualRegister rs1) {
            inst.rs1 = virtual2Stack(rs1);
            loadStackReg(a0, (StackRegister) inst.rs1);
        } else if (inst.rs1 instanceof PhysicalRegister physicalReg) {
            currentBlock.pushBack(
                    new MoveInst(a0, physicalReg)
            );
        } else {
            currentBlock.pushBack(
                    new LiInst(a0, (Imm) inst.rs1)
            );
        }
        PhysicalRegister rs2Reg;
        Imm imm = zero;
        //取到pointer地址载入rs2Reg,imm
        if (inst.rs2 instanceof PhysicalRegister physicalReg) {
            rs2Reg = physicalReg;
        } else {
            inst.rs2 = virtual2Stack((VirtualRegister) inst.rs2);
            Pair<Register, Imm> addr = getRegAddress((StackRegister) inst.rs2);
            if (inst.needPointerAddr) {
                rs2Reg = (PhysicalRegister) addr.getFirst();
                imm = addr.getSecond();
            } else {
                currentBlock.pushBack(
                        new LoadInst((PhysicalRegister) addr.getFirst(), a1, addr.getSecond())
                );
                rs2Reg = a1;
            }
        }
        currentBlock.pushBack(
                new StoreInst(a0, rs2Reg, imm)
        );
    }

    @Override
    public void visit(EqualZeroInst inst) {
        PhysicalRegister rs1Reg;
        if (inst.rs1 instanceof VirtualRegister rs1) {
            inst.rs1 = virtual2Stack(rs1);
            loadStackReg(a0, (StackRegister) inst.rs1);
            rs1Reg = a0;
        } else if (inst.rs1 instanceof PhysicalRegister physicalReg) {
            rs1Reg = physicalReg;
        } else {
            throw new InternalException("unexpected rs1 type in EqualZeroInst");
        }
        currentBlock.pushBack(
                new EqualZeroInst(rs1Reg, a1, inst.op)
        );
        if (inst.rd instanceof VirtualRegister rd) {
            inst.rd = virtual2Stack(rd);
            storeStackReg(a1, (StackRegister) inst.rd);
        }
    }

    @Override
    public void visit(LuiInst inst) {
        if (inst.rd instanceof VirtualRegister rd) {
            a0.size = 4;
            currentBlock.pushBack(
                    new LuiInst(a0, inst.imm)
            );
            inst.rd = virtual2Stack(rd);
            storeStackReg(a0, (StackRegister) inst.rd);
        } else {
            currentBlock.pushBack(inst);
        }
    }

    @Override
    public void visit(GlobalAddrInst inst) {
        if (inst.rd instanceof VirtualRegister rd) {
            a0.size = 4;
            currentBlock.pushBack(
                    new GlobalAddrInst(a0, inst.name)
            );
            inst.rd = virtual2Stack(rd);
            storeStackReg(a0, (StackRegister) inst.rd);
        } else {
            currentBlock.pushBack(inst);
        }
    }

    @Override
    public void visit(NotInst inst) {
        PhysicalRegister rs1Reg;
        if (inst.rs1 instanceof VirtualRegister rs1) {
            inst.rs1 = virtual2Stack(rs1);
            loadStackReg(a0, (StackRegister) inst.rs1);
            rs1Reg = a0;
        } else if (inst.rs1 instanceof PhysicalRegister physicalReg) {
            rs1Reg = physicalReg;
        } else {
            throw new InternalException("unexpected rs1 type in NotInst");
        }
        currentBlock.pushBack(
                new NotInst(a0, rs1Reg)
        );
        if (inst.rd instanceof VirtualRegister rd) {
            inst.rd = virtual2Stack(rd);
            storeStackReg(a0, (StackRegister) inst.rd);
        }
    }

    @Override
    public void visit(Imm operand) {
    }

    @Override
    public void visit(VirtualRegister operand) {
    }
}
