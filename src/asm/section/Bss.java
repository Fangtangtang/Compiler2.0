package asm.section;

import java.io.PrintStream;

/**
 * .bss
 * 隐式0初始化
 * -------------------------------------
 * |	.type	b,@object                       # @b
 * |	.section	.bss
 * |	.globl	b
 * |b:
 * |	.byte	0                               # 0x0
 * |	.size	b, 1
 * |
 * |# int
 * |	.type	c,@object                       # @c
 * |	.section	.bss
 * |	.globl	c
 * |c:
 * |	.word	0                               # 0x0 初值
 * |	.size	c, 4                            # 占用字节数
 * ---------------------------------------
 */
public class Bss extends Section {

    public String name;
    boolean isBool = false;

    public Bss(String name) {
        this.name = name;
    }

    public Bss(String name, boolean isBool) {
        this.name = name;
        this.isBool = isBool;
    }

    @Override
    public void print(PrintStream out) {
        out.println("\t.type\t" + name + ",@object");
        out.println("\t.section\t.bss");
        out.println("\t.globl\t" + name);
        out.println(name + ":");
        if (isBool) {
            out.println("\t.byte\t0");
            out.println("\t.size\t" + name + ", 1");
        } else {
            out.println("\t.word\t0");
            out.println("\t.size\t" + name + ", 4");
        }
    }
}
