package asm;

import asm.instruction.ASMInstruction;
import asm.instruction.BranchInst;
import asm.instruction.JumpInst;
import utility.error.InternalException;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author F
 * IR中的函数
 * ---------------------------------------------------------------
 * |	.section text
 * |	.globl	func                        # -- Begin function func 说明函数全局可见
 * |	.type	func,@function              # 说明func标签是个函数
 * |func:                                   # 函数标签
 * |# %bb.0:                        # 函数basicBlock标签
 * |	addi	sp, sp, -16
 * |	sw	ra, 12(sp)                      # 4-byte Folded Spill
 * |	sw	s0, 8(sp)                       # 4-byte Folded Spill
 * |	addi	s0, sp, 16
 * |	sw	a0, -12(s0)
 * |	lw	a0, -12(s0)
 * |	addi	a0, a0, 1
 * |	sw	a0, -12(s0)
 * |	lw	a0, -12(s0)
 * |	lw	ra, 12(sp)                      # 4-byte Folded Reload
 * |	lw	s0, 8(sp)                       # 4-byte Folded Reload
 * |	addi	sp, sp, 16
 * |	ret
 * |.Lfunc_end0:                    # 函数结束标签
 * |	.size	func, .Lfunc_end0-func      #由offset计算出函数体所占字节数
 * |                                        # -- End function
 * ---------------------------------------------------------------------------
 */
public class Func {
    public String name;
    //不需要名字标签的块(不放进funcBlocks)
    public Block entry;
    public ArrayList<Block> funcBlocks = new ArrayList<>();
    //反向CFG上RPO排列的Block
    public ArrayList<Block> reorderedBlock = new ArrayList<>();
    public Block entryBlock = null;
    public Block retBlock = null;
    //virtual register（栈上，局部变量+临时量）占用
    //默认含ra，fp
    public int basicSpace = 8;

    public int extraParamCnt = 0;//占用栈空间的传参（全按4byte算）

    public Func(String name) {
        this.name = name;
    }

    public HashMap<String, Block> constructGenealogy() {
        HashMap<String, Block> blockMap = new HashMap<>();
        funcBlocks.forEach(
                block -> {
                    block.predecessorList = new ArrayList<>();
                    block.successorList = new ArrayList<>();
                    blockMap.put(block.name, block);
                }
        );
        for (int i = 0; i < funcBlocks.size(); i++) {
            Block block = funcBlocks.get(i);
            if (block.instructions.size() == 0) {
                continue;
            }
            boolean flag = false;
            for (var instruction : block.controlInstructions) {
                Block target = null;
                if (instruction instanceof JumpInst jumpInst) {
                    flag = true;
                    target = blockMap.get(jumpInst.desName);
                } else if (instruction instanceof BranchInst branchInst) {
                    target = blockMap.get(branchInst.desName);
                }
                if (target != null) {
                    block.successorList.add(target);
                    target.predecessorList.add(block);
                } else {
                    throw new InternalException("unexpected control inst");
                }
            }
            if (!flag && i + 1 < funcBlocks.size()) {
                Block target = funcBlocks.get(i + 1);
                block.successorList.add(target);
                target.predecessorList.add(block);
            }
        }
        return blockMap;
    }

    public void print(PrintStream out) {
        String end = ".L" + name + "_end";
        out.println("\t.globl\t" + name);
        out.println("\t.type\t" + name + ",@function");
        out.println(name + ":");
        entry.print(out);
        funcBlocks.forEach(
                block -> {
                    out.println(block.name + ":");
                    block.print(out);
                }
        );
        out.println(end);
        out.println("\t.size\t" + name + ", " + end + "-" + name);
    }

    public void printRegColoring(PrintStream out) {
        String end = ".L" + name + "_end";
        out.println("\t.globl\t" + name);
        out.println("\t.type\t" + name + ",@function");
        out.println(name + ":");
        entry.printRegColoring(out);
        funcBlocks.forEach(
                block -> {
                    out.println(block.name + ":");
                    block.printRegColoring(out);
                }
        );
        out.println(end);
        out.println("\t.size\t" + name + ", " + end + "-" + name);
    }
}
