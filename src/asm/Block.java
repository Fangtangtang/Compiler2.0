package asm;

import asm.instruction.*;
import asm.operand.*;
import utility.error.InternalException;

import java.io.PrintStream;
import java.util.*;

/**
 * @author F
 * 将IR函数中的basicBloack转为ASM上block
 */
public class Block {
    public String name;

    public LinkedList<ASMInstruction> instructions = new LinkedList<>();
    public ArrayList<ASMInstruction> controlInstructions = new ArrayList<>();
    public ArrayList<Block> predecessorList = new ArrayList<>();
    public ArrayList<Block> successorList = new ArrayList<>();
    public int reversePostorder;

    public HashSet<Register> use;
    public HashSet<Register> def;
    public HashSet<Register> liveOut;

    public Block(String name) {
        this.name = name;
    }

    public void pushBack(ASMInstruction instruction) {
        instructions.add(instruction);
        if (instruction instanceof BranchInst || instruction instanceof JumpInst) {
            controlInstructions.add(instruction);
        }
    }

    public void print(PrintStream out) {
        instructions.forEach(
                instruction -> instruction.print(out)
        );
    }

    public void printRegColoring(PrintStream out) {
        instructions.forEach(
                instruction -> instruction.printRegColoring(out)
        );
    }
}
