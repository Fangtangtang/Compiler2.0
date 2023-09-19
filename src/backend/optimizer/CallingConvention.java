package backend.optimizer;

import asm.Block;
import asm.Func;
import asm.PhysicalRegMap;
import asm.instruction.ASMInstruction;
import asm.instruction.CallInst;
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
    //大index，callee saved
    HashSet<Colors.Color> usedInFunc = new HashSet<>();

    public CallingConvention(Func func, PhysicalRegMap registerMap) {
        this.func = func;
        this.registerMap = registerMap;
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
                //在call前插入

            }
            //维护
            use = inst.getUse();
            def = inst.getDef();
            if (use != null) {
                for (Register register : use) {
                    liveOut.add(register.color);
                    usedInFunc.add(register.color);
                }
            }
            if (def != null) {
                liveOut.remove(def.color);
                usedInFunc.add(def.color);
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
        ArrayList<Pair<PhysicalRegister, StackRegister>>pairs=new ArrayList<>();

        return pairs;
    }

    //call前，将reg存到stack
    void beforeCall(ListIterator<ASMInstruction> iter,
                    ArrayList<Pair<PhysicalRegister, StackRegister>> pairs) {

    }

    //call后复原
    void afterCall(ListIterator<ASMInstruction> iter,
                   ArrayList<Pair<PhysicalRegister, StackRegister>> pairs) {

    }
}
