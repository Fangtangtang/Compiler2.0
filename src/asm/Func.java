package asm;

import asm.instruction.ASMInstruction;

import java.io.PrintStream;
import java.util.ArrayList;

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
    //不需要名字标签的块
    public Block entry;
    public ArrayList<Block> funcBlocks = new ArrayList<>();
    public ArrayList<ASMInstruction> getParams = new ArrayList<>();

    //virtual register（栈上，局部变量+临时量）占用
    //默认含ra，fp
    public int basicSpace = 8;

    public int extraParamCnt = 0;//占用栈空间的传参（全按4byte算）

    public Func(String name) {
        this.name = name;
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
}
