package asm.section;

import java.io.PrintStream;

/**
 * @author F
 * .data初始化的全局变量
 * 仅有int和bool
 * -----------------------------------------------------------
 * |# bool
 * |	.type	a,@object                       # @a
 * |	.section	.data
 * |	.globl	a
 * |a:
 * |	.byte	1                               # 0x1
 * |	.size	a, 1
 * -------------------------------------------------------------
 */
public class Data extends Section {
    public String name;
    public String value;
    boolean isBool = false;

    public Data(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public Data(String name, String value, boolean isBool) {
        this.name = name;
        this.value = value;
        this.isBool = isBool;
    }

    @Override
    public void print(PrintStream out) {
        out.println("\t.type\t" + name + ",@object");
        out.println("\t.section\t.data");
        out.println("\t.globl\t" + name);
        out.println(name + ":");
        if (isBool) {
            out.println("\t.byte\t" + value);
            out.println("\t.size\t" + name + ", 1");
        } else {
            out.println("\t.word\t" + value);
            out.println("\t.size\t" + name + ", 4");
        }
    }
}
