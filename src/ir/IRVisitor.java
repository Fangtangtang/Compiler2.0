package ir;

import ir.function.Function;
import ir.stmt.instruction.*;
import ir.stmt.terminal.*;

/**
 * @author F
 * 访问IR的接口
 */
public interface IRVisitor {
    void visit(IRRoot root);
    void visit(BasicBlock basicBlock);
    void visit(Function function);

    void visit(Alloca stmt);

    void visit(Binary stmt);

    void visit(Call stmt);

    void visit(GetElementPtr stmt);

    void visit(Global stmt);

    void visit(Icmp stmt);

    void visit(Load stmt);

    void visit(Store stmt);

    void visit(Branch stmt);

    void visit(Jump stmt);

    void visit(Return stmt);

    void visit(Trunc stmt);

    void visit(Zext stmt);

    void visit(Phi stmt);

}
