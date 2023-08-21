package asm.section;

import java.io.PrintStream;

/**
 * @author F
 * .rodata:read only data
 * 字符串常量
 * -----------------------------------
 * |	.type	.L.str,@object                  # @.str
 * |	.section	.rodata        # .rodata只读数据段
 * |.L.str:
 * |	.asciz	"123"
 * |	.size	.L.str, 4
 * -------------------------------------
 */
public class Rodata extends Section {
    public String strName;
    public String value;

    public Rodata(String name, String value) {
        this.strName = ".L" + name;
        this.value = value;
    }

    @Override
    public void print(PrintStream out) {
        out.println("\t.type\t" + strName + ",@object");
        out.println("\t.section\trodata");
        out.println(strName + ":");
        out.println("\t.asciz\t" + value);
        out.println("\t.size\t" + strName + ", " + value.length());
    }

}
