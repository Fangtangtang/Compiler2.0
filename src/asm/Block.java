package asm;

import asm.instruction.*;
import asm.operand.*;

import java.io.PrintStream;
import java.util.*;

/**
 * @author F
 * 将IR函数中的basicBloack转为ASM上block
 */
public class Block {
    public String name;

    public ArrayList<ASMInstruction> instructions = new ArrayList<>();
    public ArrayList<Block> predecessorList = new ArrayList<>();
    public ArrayList<Block> successorList = new ArrayList<>();
    public int reversePostorder;

    public HashSet<Register> use = new HashSet<>();
    public HashSet<Register> def = new HashSet<>();
    public HashSet<Register> liveOut ;

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
