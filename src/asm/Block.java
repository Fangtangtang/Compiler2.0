package asm;

import asm.instruction.ASMInstruction;

import java.io.PrintStream;
import java.util.ArrayList;

/**
 * @author F
 * 将IR函数中的basicBloack转为ASM上block
 */
public class Block {
    public String name;

    public ArrayList<ASMInstruction> instructions = new ArrayList<>();

    public Block(String name) {
        this.name = name;
    }

    public void pushBack(ASMInstruction instruction) {
        instructions.add(instruction);
    }

    public void print(PrintStream out) {
        instructions.forEach(
                instruction -> instruction.print(out)
        );
    }
}
