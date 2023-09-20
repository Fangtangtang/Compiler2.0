package backend;

import asm.*;
import asm.instruction.*;
import asm.operand.*;
import asm.section.Text;
import backend.optimizer.*;
import utility.Pair;

import java.util.ArrayList;

/**
 * @author F
 * ASM上优化
 */
public class ASMOptimizer {
    public Text text;
    PhysicalRegMap regMap;
    PhysicalRegister fp, sp, t0;

    public ASMOptimizer(Text text, PhysicalRegMap regMap) {
        this.text = text;
        this.regMap = regMap;
        fp = regMap.getReg("fp");
        sp = regMap.getReg("sp");
        t0 = regMap.getReg("t0");
    }

    public void execute() {
        CFGBuilder cfgBuilder = new CFGBuilder(text);
        cfgBuilder.build();
        text.functions.forEach(
                func -> {
                    GraphColoring graphColoring = new GraphColoring(func, regMap);
                    graphColoring.execute();
                    CallingConvention callingConvention = new CallingConvention(func, regMap);
                    ArrayList<PhysicalRegister> calleeSaved = callingConvention.execute();
                    addExtraInstToFunc(func, calleeSaved);
                }
        );
    }

    /**
     * 添加进出函数时的额外指令
     * - 开栈
     * - 回收
     */
    void addExtraInstToFunc(Func func, ArrayList<PhysicalRegister> calleeSaved) {
        ArrayList<Pair<PhysicalRegister, StackRegister>> pairs = allocateStack(func, calleeSaved);
        int stackSize = func.basicSpace + (func.extraParamCnt << 2);
        stackSize = (stackSize + 15) >> 4;
        stackSize <<= 4;
        //进入函数时的额外指令(不加入funcBlocks)
        func.entry = new Block(func.name);
        func.entry.pushBack(
                new ImmBinaryInst(sp, new Imm(-stackSize), sp, ImmBinaryInst.Opcode.addi)
        );
        func.entry.pushBack(
                new StoreInst(regMap.getReg("ra"), sp, new Imm(stackSize - 4))
        );
        func.entry.pushBack(
                new StoreInst(fp, sp, new Imm(stackSize - 8))
        );
        func.entry.pushBack(
                new ImmBinaryInst(sp, new Imm(stackSize), fp, ImmBinaryInst.Opcode.addi)
        );
        store(func.entry, pairs);
        //出函数的指令
        Block endBlock = func.funcBlocks.get(func.funcBlocks.size() - 1);
        load(endBlock, pairs);
        endBlock.pushBack(
                new LoadInst(sp, regMap.getReg("ra"), new Imm(stackSize - 4))
        );
        endBlock.pushBack(
                new LoadInst(sp, fp, new Imm(stackSize - 8))
        );
        endBlock.pushBack(
                new ImmBinaryInst(sp, new Imm(stackSize), sp, ImmBinaryInst.Opcode.addi)
        );
        endBlock.pushBack(
                new RetInst()
        );
    }

    ArrayList<Pair<PhysicalRegister, StackRegister>> allocateStack(Func func,
                                                                   ArrayList<PhysicalRegister> callerSaved) {
        ArrayList<Pair<PhysicalRegister, StackRegister>> pairs = new ArrayList<>();
        for (var reg : callerSaved) {
            func.basicSpace += reg.size;
            StackRegister newReg = new StackRegister(func.basicSpace, reg.size);
            pairs.add(0, new Pair<>(reg, newReg));
        }
        return pairs;
    }

    Imm zero = new Imm(0);

    void store(Block block,
               ArrayList<Pair<PhysicalRegister, StackRegister>> pairs) {
        for (var pair : pairs) {
            pair.getFirst().size = pair.getSecond().size;
            if (pair.getSecond().offset < (1 << 11)) {
                block.pushBack(
                        new StoreInst(pair.getFirst(), fp, new Imm(-pair.getSecond().offset))
                );
            } else {
                PhysicalRegister t0 = new PhysicalRegister("t0", 4);
                block.pushBack(
                        new LuiInst(t0, new Imm((pair.getSecond().offset >> 12)))
                );
                if ((pair.getSecond().offset & 0xFFF) != 0) {
                    block.pushBack(
                            new ImmBinaryInst(
                                    t0,
                                    new Imm(pair.getSecond().offset & 0xFFF),
                                    t0,
                                    ImmBinaryInst.Opcode.addi
                            )
                    );
                }
                block.pushBack(
                        new BinaryInst(fp, t0, t0, BinaryInst.Opcode.sub)
                );
                block.pushBack(
                        new StoreInst(pair.getFirst(), t0, zero)
                );
            }
        }
    }

    void load(Block block,
              ArrayList<Pair<PhysicalRegister, StackRegister>> pairs) {
        for (var pair : pairs) {
            pair.getFirst().size = pair.getSecond().size;
            if (pair.getSecond().offset < (1 << 11)) {
                block.pushBack(
                        new LoadInst(fp, pair.getFirst(), new Imm(-pair.getSecond().offset))
                );
            } else {
                PhysicalRegister t0 = new PhysicalRegister("t0", 4);
                block.pushBack(
                        new LuiInst(t0, new Imm((pair.getSecond().offset >> 12)))
                );
                if ((pair.getSecond().offset & 0xFFF) != 0) {
                    block.pushBack(
                            new ImmBinaryInst(
                                    t0,
                                    new Imm(pair.getSecond().offset & 0xFFF),
                                    t0,
                                    ImmBinaryInst.Opcode.addi
                            )
                    );
                }
                block.pushBack(
                        new BinaryInst(fp, t0, t0, BinaryInst.Opcode.sub)
                );
                block.pushBack(
                        new LoadInst(t0, pair.getFirst(), zero)
                );
            }
        }
    }
}

