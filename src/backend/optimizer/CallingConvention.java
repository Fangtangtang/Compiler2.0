package backend.optimizer;

import asm.Block;
import asm.Func;
import asm.PhysicalRegMap;
import asm.instruction.*;
import asm.operand.Imm;
import asm.operand.PhysicalRegister;
import asm.operand.Register;
import asm.operand.StackRegister;
import backend.optimizer.interferenceGraph.Colors;
import utility.Pair;

import java.util.*;

/**
 * @author F
 * 图染色后维护caller saved和callee saved
 */
public class CallingConvention {
    Func func;
    PhysicalRegMap registerMap;
    PhysicalRegister fp, sp;
    Imm zero = new Imm(0);
    //大index，callee saved
    HashSet<Colors.Color> usedInFunc = new HashSet<>();

    public CallingConvention(Func func, PhysicalRegMap registerMap) {
        this.func = func;
        this.registerMap = registerMap;
        fp = registerMap.getReg("fp");
        sp = registerMap.getReg("sp");
    }

    /**
     * 在call前后插入caller saved reg
     *
     * @return callee saved reg
     */
    public ArrayList<PhysicalRegister> execute() {
        //遍历每一个bb
        func.funcBlocks.forEach(
                this::executeOnBlock
        );
        //callee saved
        ArrayList<PhysicalRegister> calleeSaved = new ArrayList<>();
        usedInFunc.forEach(
                regColor -> {
                    if (regColor.ordinal() >= Colors.Color.s1.ordinal()) {
                        calleeSaved.add(registerMap.getReg(regColor));
                    }
                }
        );
        return calleeSaved;
    }

    void executeOnBlock(Block block) {
        HashSet<Colors.Color> liveOut = new HashSet<>();
        for (Register reg : block.liveOut) {
            liveOut.add(reg.color);
        }
        ArrayList<Register> use;
        Register def;
        //反向遍历
        ListIterator<ASMInstruction> iter = block.instructions.listIterator(block.instructions.size());
        while (iter.hasPrevious()) {
            ASMInstruction inst = iter.previous();
            //caller save
            if (inst instanceof CallInst callInst) {
                //caller saved registers
                ArrayList<PhysicalRegister> callerSaved = callerSavedReg(liveOut);
                ArrayList<Pair<PhysicalRegister, StackRegister>> pairs = allocateStack(callerSaved);
                //call后插入
                if (callInst.hasReturn) {
                    iter.next();
                    afterCall(iter, pairs);
                    iter.previous();
                } else {
                    afterCall(iter, pairs);
                }
                //在call前插入
                beforeCall(iter, pairs);
            }
            //维护
            use = inst.getUse();
            def = inst.getDef();
            if (def != null) {
                liveOut.remove(def.color);
                usedInFunc.add(def.color);
            }
            if (use != null) {
                for (Register register : use) {
                    liveOut.add(register.color);
                    usedInFunc.add(register.color);
                }
            }
        }
    }

    ArrayList<PhysicalRegister> callerSavedReg(HashSet<Colors.Color> liveOut) {
        ArrayList<PhysicalRegister> callerSaved = new ArrayList<>();
        liveOut.forEach(
                regColor -> {
                    if (regColor.ordinal() < Colors.Color.s1.ordinal()) {
                        callerSaved.add(registerMap.getReg(regColor));
                    }
                }
        );
        return callerSaved;
    }

    //给要保存的reg分配栈上空间
    ArrayList<Pair<PhysicalRegister, StackRegister>> allocateStack(ArrayList<PhysicalRegister> callerSaved) {
        ArrayList<Pair<PhysicalRegister, StackRegister>> pairs = new ArrayList<>();
        for (var reg : callerSaved) {
            func.basicSpace += reg.size;
            StackRegister newReg = new StackRegister(func.basicSpace, reg.size);
            pairs.add(new Pair<>(reg, newReg));
        }
        return pairs;
    }

    //call前，将reg存到stack
    void beforeCall(ListIterator<ASMInstruction> iter,
                    ArrayList<Pair<PhysicalRegister, StackRegister>> pairs) {
        for (var pair : pairs) {
            int size;
            if (pair.getSecond().offset < (1 << 11)) {
                iter.add(
                        new StoreInst(pair.getFirst(), fp, new Imm(-pair.getSecond().offset))
                );
                size = 1;
            } else {
                size = 3;
                PhysicalRegister t0 = new PhysicalRegister("t0", 4);
                iter.add(
                        new LuiInst(t0, new Imm((pair.getSecond().offset >> 12)))
                );
                if ((pair.getSecond().offset & 0xFFF) != 0) {
                    ++size;
                    iter.add(
                            new ImmBinaryInst(
                                    t0,
                                    new Imm(pair.getSecond().offset & 0xFFF),
                                    t0,
                                    ImmBinaryInst.Opcode.addi
                            )
                    );
                }
                iter.add(
                        new BinaryInst(fp, t0, t0, BinaryInst.Opcode.sub)
                );
                iter.add(
                        new StoreInst(pair.getFirst(), t0, zero)
                );
            }
            for (int i = 0; i < size; i++) {
                iter.previous();
            }
        }
    }

    //call后复原
    void afterCall(ListIterator<ASMInstruction> iter,
                   ArrayList<Pair<PhysicalRegister, StackRegister>> pairs) {
        iter.next();
        for (var pair : pairs) {
            int size;
            if (pair.getSecond().offset < (1 << 11)) {
                iter.add(
                        new LoadInst(fp, pair.getFirst(), new Imm(-pair.getSecond().offset))
                );
                size = 1;
            } else {
                size = 3;
                PhysicalRegister t0 = new PhysicalRegister("t0", 4);
                iter.add(
                        new LuiInst(t0, new Imm((pair.getSecond().offset >> 12)))
                );
                if ((pair.getSecond().offset & 0xFFF) != 0) {
                    ++size;
                    iter.add(
                            new ImmBinaryInst(
                                    t0,
                                    new Imm(pair.getSecond().offset & 0xFFF),
                                    t0,
                                    ImmBinaryInst.Opcode.addi
                            )
                    );
                }
                iter.add(
                        new BinaryInst(fp, t0, t0, BinaryInst.Opcode.sub)
                );
                iter.add(
                        new LoadInst(t0, pair.getFirst(), zero)
                );
            }
            for (int i = 0; i < size; i++) {
                iter.previous();
            }
        }
        iter.previous();
    }
}
