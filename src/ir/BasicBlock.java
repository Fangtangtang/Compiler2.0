package ir;

import ir.stmt.Stmt;
import ir.stmt.terminal.TerminalStmt;
import utility.Pair;
import utility.error.InternalException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * @author F
 * 基本块，控制流的单位
 * 成员statements：语句块中的语句
 * 成员tailStmt：语句块的终结语句
 */
public class BasicBlock {
    //每个基本块的标识符，本函数内唯一
    public String label;

    public LinkedList<Stmt> statements = new LinkedList<>();
    public TerminalStmt tailStmt = null;

    //CFG上的前驱后继
    public ArrayList<BasicBlock> predecessorList = new ArrayList<>();
    public ArrayList<BasicBlock> successorList = new ArrayList<>();
    public int reversePostorder;
    //所有mem2reg生成的phi
    //name -> <rename,phiList>
    public HashMap<String, Pair<String, String[]>> phiMap;

    public BasicBlock(String label) {
        this.label = label;
    }

    /**
     * 将语句按出现顺序加入到链表中
     * 记录语句块的终结语句
     * 每个语句块以TerminalStmt（branch、jump，ret）结尾
     * 每个语句块仅有一个终结语句
     *
     * @param stmt 出现在block中的语句
     */
    public void pushBack(Stmt stmt) {
        if (stmt instanceof TerminalStmt terminalStmt) {
            if (tailStmt == null) {
                tailStmt = terminalStmt;
                return;
            } else {
                throw new InternalException("basic block " + label + " has multiple exits");
            }
        }
        statements.add(stmt);
    }

    @Override
    public String toString() {
        return label;
    }

    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }
}
