package asm.section;

import java.io.PrintStream;

/**
 * @author F
 * 对应asm中的.section
 */
public abstract class Section {
    public abstract void print(PrintStream out);
}
