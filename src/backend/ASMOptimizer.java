package backend;

import asm.*;
import asm.instruction.*;
import asm.operand.*;
import asm.section.Text;
import backend.optimizer.*;

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
        //TODO:extra callee saved register

        func.entry.pushBack(
                new ImmBinaryInst(sp, new Imm(stackSize), fp, ImmBinaryInst.Opcode.addi)
        );
        //出函数的指令
        Block endBlock = func.funcBlocks.get(func.funcBlocks.size() - 1);
        endBlock.pushBack(
                new LoadInst(sp, regMap.getReg("ra"), new Imm(stackSize - 4))
        );
        endBlock.pushBack(
                new LoadInst(sp, fp, new Imm(stackSize - 8))
        );
        //TODO:extra callee saved register

        endBlock.pushBack(
                new ImmBinaryInst(sp, new Imm(stackSize), sp, ImmBinaryInst.Opcode.addi)
        );
        endBlock.pushBack(
                new RetInst()
        );
    }
}
