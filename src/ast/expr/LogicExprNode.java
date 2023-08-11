package ast.expr;

import ast.ASTVisitor;
import ast.ExprNode;
import ast.stmt.IfStmtNode;
import ir.BasicBlock;
import ir.entity.Entity;
import ir.stmt.terminal.Branch;
import ir.stmt.terminal.Jump;
import utility.Position;
import utility.type.BoolType;

/**
 * @author F
 * 逻辑二元运算表达式
 * bool类型
 * 不可赋值
 */
public class LogicExprNode extends ExprNode {
    public enum LogicOperator {
        AndAnd, OrOr
    }

    public ExprNode lhs, rhs;
    public LogicOperator operator;

    public LogicExprNode(Position pos,
                         ExprNode lhs,
                         ExprNode rhs,
                         LogicOperator operator) {
        super(pos);
        this.exprType = new BoolType();
        this.lhs = lhs;
        this.rhs = rhs;
        this.operator = operator;
    }

    @Override
    public <T> T accept(ASTVisitor<? extends T> visitor) {
        return visitor.visit(this);
    }

}

//    @Override
//    public Entity visit(IfStmtNode node) {
//        int label = currentFunction.cnt++;
//        BasicBlock trueStmtBlock = new BasicBlock("if.then" + label);
//        BasicBlock falseStmtBlock = null;
//        BasicBlock endBlock = new BasicBlock("if.end" + label);
//        BasicBlock next = endBlock;
//        if (node.falseStatement != null) {
//            falseStmtBlock = new BasicBlock("if.else" + label);
//            next = falseStmtBlock;
//        }
//        //cond：在上一个块里
//        Entity entity = node.condition.accept(this);
//        currentBlock.pushBack(
//                new Branch(entity, trueStmtBlock, next)
//        );
//        currentBlock = trueStmtBlock;
//        node.trueStatement.accept(this);
//        currentBlock.pushBack(
//                new Jump(endBlock)
//        );
//        if (node.falseStatement != null) {
//            currentBlock = falseStmtBlock;
//            node.falseStatement.accept(this);
//            currentBlock.pushBack(
//                    new Jump(endBlock)
//            );
//        }
//        currentBlock = endBlock;
//        return null;
//    }