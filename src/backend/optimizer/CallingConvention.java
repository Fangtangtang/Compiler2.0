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
    PhysicalRegister fp, sp, gp;
    Imm zero = new Imm(0);
    //大index，callee saved
    HashSet<Colors.Color> usedInFunc = new HashSet<>();

    public CallingConvention(Func func, PhysicalRegMap registerMap) {
        this.func = func;
        this.registerMap = registerMap;
        fp = registerMap.getReg("fp");
        sp = registerMap.getReg("sp");
        gp = registerMap.getReg("gp");
        gp.color = Colors.Color.gp;
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
                    if (regColor.ordinal() >= Colors.Color.s2.ordinal()) {
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
                    ASMInstruction instruction = iter.next();
                    if (instruction instanceof MoveInst mvRetInst &&
                            mvRetInst.rs1.color == Colors.Color.a0) {
                        iter.remove(); // mv
                        iter.add(
                                new MoveInst(gp, mvRetInst.rs1)
                        );
                        iter.add(
                                new MoveInst(mvRetInst.rd, gp)
                        );
                        iter.previous();
                        iter.previous();
                        afterCall(iter, pairs);
                        iter.previous();
                    } else {
                        iter.previous();
                        iter.previous();
                        afterCall(iter, pairs);
                    }
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
                    if (regColor.ordinal() < Colors.Color.s1.ordinal() &&
                            regColor.ordinal() > Colors.Color.fp.ordinal()) {
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
            pairs.add(0, new Pair<>(reg, newReg));
        }
        return pairs;
    }

    //call前，将reg存到stack
    //TODO:s1冲突？
    void beforeCall(ListIterator<ASMInstruction> iter,
                    ArrayList<Pair<PhysicalRegister, StackRegister>> pairs) {
        for (var pair : pairs) {
            pair.getFirst().size = pair.getSecond().size;
            int size;
            if (pair.getSecond().offset < (1 << 11)) {
                iter.add(
                        new StoreInst(pair.getFirst(), fp, new Imm(-pair.getSecond().offset))
                );
                size = 1;
            } else {
                size = 3;
                PhysicalRegister s1 = new PhysicalRegister("s1", 4);
                iter.add(
                        new LuiInst(s1, new Imm((pair.getSecond().offset >> 12)))
                );
                if ((pair.getSecond().offset & 0xFFF) != 0) {
                    ++size;
                    iter.add(
                            new ImmBinaryInst(
                                    s1,
                                    new Imm(pair.getSecond().offset & 0xFFF),
                                    s1,
                                    ImmBinaryInst.Opcode.addi
                            )
                    );
                }
                iter.add(
                        new BinaryInst(fp, s1, s1, BinaryInst.Opcode.sub)
                );
                iter.add(
                        new StoreInst(pair.getFirst(), s1, zero)
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
            pair.getFirst().size = pair.getSecond().size;
            int size;
            if (pair.getSecond().offset < (1 << 11)) {
                iter.add(
                        new LoadInst(fp, pair.getFirst(), new Imm(-pair.getSecond().offset))
                );
                size = 1;
            } else {
                size = 3;
                PhysicalRegister s1 = new PhysicalRegister("s1", 4);
                iter.add(
                        new LuiInst(s1, new Imm((pair.getSecond().offset >> 12)))
                );
                if ((pair.getSecond().offset & 0xFFF) != 0) {
                    ++size;
                    iter.add(
                            new ImmBinaryInst(
                                    s1,
                                    new Imm(pair.getSecond().offset & 0xFFF),
                                    s1,
                                    ImmBinaryInst.Opcode.addi
                            )
                    );
                }
                iter.add(
                        new BinaryInst(fp, s1, s1, BinaryInst.Opcode.sub)
                );
                iter.add(
                        new LoadInst(s1, pair.getFirst(), zero)
                );
            }
            for (int i = 0; i < size; i++) {
                iter.previous();
            }
        }
        iter.previous();
    }
}
