package backend;

import ir.BasicBlock;
import ir.IRRoot;
import ir.IRVisitor;
import ir.function.Function;
import ir.stmt.instruction.*;
import ir.stmt.terminal.Branch;
import ir.stmt.terminal.Jump;
import ir.stmt.terminal.Return;

/**
 * @author F
 * 遍历IR，转化为ASM指令
 */
public class InstSelector implements IRVisitor {
    /**
     * IRRoot
     * 访问全局变量的定义和初始化函数
     * 一一访问函数
     * @param root Mx的根
     */
    @Override
    public void visit(IRRoot root) {

    }

    @Override
    public void visit(BasicBlock basicBlock) {

    }

    @Override
    public void visit(Function function) {

    }

    @Override
    public void visit(Alloca stmt) {

    }

    @Override
    public void visit(Binary stmt) {

    }

    @Override
    public void visit(Call stmt) {

    }

    @Override
    public void visit(GetElementPtr stmt) {

    }

    @Override
    public void visit(Global stmt) {

    }

    @Override
    public void visit(Icmp stmt) {

    }

    @Override
    public void visit(Load stmt) {

    }

    @Override
    public void visit(Store stmt) {

    }

    @Override
    public void visit(Branch stmt) {

    }

    @Override
    public void visit(Jump stmt) {

    }

    @Override
    public void visit(Return stmt) {

    }

    @Override
    public void visit(Trunc stmt) {

    }

    @Override
    public void visit(Zext stmt) {

    }

    @Override
    public void visit(Phi stmt) {

    }

    @Override
    public void visit(Malloc stmt) {

    }
}
