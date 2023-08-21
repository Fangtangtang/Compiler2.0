package asm.section;

import asm.Func;

import java.io.PrintStream;
import java.util.ArrayList;

/**
 * @author F
 * .text代码段部分
 */
public class Text extends Section {
    //所有的函数
    public ArrayList<Func> functions = new ArrayList<>();

    @Override
    public void print(PrintStream out) {
        out.println("\t.section\t.text");
        functions.forEach(
                func -> {
                    func.print(out);
                    out.println();
                }
        );
    }
}
