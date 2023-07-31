package tool;

import ast.ASTVisitor;
import ast.RootNode;
import ast.expr.*;
import ast.expr.ConstantExprNode.*;

import ast.other.*;
import ast.stmt.*;

/**
 * @author F
 * dfs遍历AST
 * 记录深度，打印出树形结构
 */
public class ASTPrinter implements ASTVisitor<Void> {
    int depth;

    public ASTPrinter() {
        this.depth = 0;
    }

    /**
     * 打印结点信息
     */
    protected void print(String message) {
        for (int i = 0; i < depth; ++i) {
            System.out.print('\t');
        }
        System.out.printf("%s\n", message);
    }

    //root
    @Override
    public Void visit(RootNode node) {
        String message = "RootNode: ";
        message += String.format("%d", node.declarations.size());
        print(message);
        //visit children
        ++depth;
        node.declarations.forEach(declaration -> declaration.accept(this));
        --depth;
        return null;
    }

    //stmt
    @Override
    public Void visit(BlockStmtNode node) {
        String message = "BlockStmtNode: ";
        message += String.format("Stmt:%d", node.statements.size());
        print(message);
        //visit children
        ++depth;
        node.statements.forEach(stmt -> stmt.accept(this));
        --depth;
        return null;
    }

    @Override
    public Void visit(BreakStmtNode node) {
        print("BreakStmtNode");
        return null;
    }

    @Override
    public Void visit(ConstructorDefStmtNode node) {
        String message = "ConstructorDefStmtNode: ";
        message += node.name;
        print(message);
        //visit children
        ++depth;
        node.suite.accept(this);
        --depth;
        return null;
    }

    @Override
    public Void visit(ContinueStmtNode node) {
        print("ContinueStmtNode");
        return null;
    }

    @Override
    public Void visit(ExprStmtNode node) {
        print("ExprStmtNode");
        //visit children
        ++depth;
        node.exprList.forEach(expr -> expr.accept(this));
        --depth;
        return null;
    }

    @Override
    public Void visit(ForStmtNode node) {
        print("ForStmtNode");
        //visit children
        ++depth;
        if (node.initializationStmt != null) {
            node.initializationStmt.accept(this);
        }
        if (node.condition != null) {
            node.condition.accept(this);
        }
        if (node.step != null) {
            node.step.accept(this);
        }
        node.statement.accept(this);
        --depth;
        return null;
    }

    @Override
    public Void visit(FuncDefStmtNode node) {
        print(String.format("FuncDefStmtNode: %s", node.name));
        //visit children
        ++depth;
        node.returnType.accept(this);
        if (node.parameterList != null) {
            node.parameterList.accept(this);
        }
        node.functionBody.accept(this);
        --depth;
        return null;
    }

    @Override
    public Void visit(IfStmtNode node) {
        print("IfStmtNode");
        //visit children
        ++depth;
        node.condition.accept(this);
        node.trueStatement.accept(this);
        if (node.falseStatement != null) {
            node.falseStatement.accept(this);
        }
        --depth;
        return null;
    }

    @Override
    public Void visit(ReturnStmtNode node) {
        print("ReturnStmtNode");
        //visit children
        ++depth;
        if (node.expression != null) {
            node.expression.accept(this);
        }
        --depth;
        return null;
    }

    @Override
    public Void visit(VarDefStmtNode node) {
        String message = "VarDefStmtNode: ";
        message += String.format("varDefUnits:%d", node.varDefUnitNodes.size());
        print(message);
        //visit children
        ++depth;
        node.varDefUnitNodes.forEach(varDefUnit -> varDefUnit.accept(this));
        --depth;
        return null;
    }

    @Override
    public Void visit(WhileStmtNode node) {
        print("WhileStmtNode");
        //visit children
        ++depth;
        node.condition.accept(this);
        node.statement.accept(this);
        --depth;
        return null;
    }

    //expr
    @Override
    public Void visit(BinaryExprNode node) {
        print(String.format("BinaryExprNode: %s", node.operator.name()));
        //visit children
        ++depth;
        node.lhs.accept(this);
        node.rhs.accept(this);
        --depth;
        return null;
    }

    @Override
    public Void visit(CmpExprNode node) {
        print(String.format("CmpExprNode: %s", node.operator.name()));
        //visit children
        ++depth;
        node.lhs.accept(this);
        node.rhs.accept(this);
        --depth;
        return null;
    }

    @Override
    public Void visit(AssignExprNode node) {
        print("AssignExprNode");
        //visit children
        ++depth;
        node.lhs.accept(this);
        node.rhs.accept(this);
        --depth;
        return null;
    }

    @Override
    public Void visit(LogicExprNode node) {
        print(String.format("LogicExprNode: %s", node.operator.name()));
        //visit children
        ++depth;
        node.lhs.accept(this);
        node.rhs.accept(this);
        --depth;
        return null;
    }

    @Override
    public Void visit(ParenthesisExprNode node) {
        print("ParenthesisExprNode");
        //visit children
        ++depth;
        node.expression.accept(this);
        --depth;
        return null;
    }

    @Override
    public Void visit(PrefixExprNode node) {
        print(String.format("PrefixExprNode: %s", node.operator.name()));
        //visit children
        ++depth;
        node.expression.accept(this);
        --depth;
        return null;
    }

    @Override
    public Void visit(LogicPrefixExprNode node) {
        print(String.format("LogicPrefixExprNode: %s", node.operator.name()));
        //visit children
        ++depth;
        node.expression.accept(this);
        --depth;
        return null;
    }

    @Override
    public Void visit(SuffixExprNode node) {
        print(String.format("SuffixExprNode: %s", node.operator.name()));
        //visit children
        ++depth;
        node.expression.accept(this);
        --depth;
        return null;
    }

    @Override
    public Void visit(TernaryExprNode node) {
        print("TernaryExprNode");
        //visit children
        ++depth;
        node.condition.accept(this);
        node.trueExpr.accept(this);
        node.falseExpr.accept(this);
        --depth;
        return null;
    }

    @Override
    public Void visit(NewExprNode node) {
        print(String.format("NewExprNode: dim%d", node.dimension));
        //visit children
        ++depth;
        node.typeNode.accept(this);
        node.dimensions.forEach(dim -> dim.accept(this));
        --depth;
        return null;
    }

    @Override
    public Void visit(MemberVisExprNode node) {
        print("MemberVisExprNode");
        //visit children
        ++depth;
        node.lhs.accept(this);
        node.rhs.accept(this);
        --depth;
        return null;
    }

    @Override
    public Void visit(IntConstantExprNode node) {
        print(String.format(
                "IntConstantExprNode: %s %d", node.exprType.toString(), node.value)
        );
        return null;
    }

    @Override
    public Void visit(StrConstantExprNode node) {
        print(String.format(
                "StrConstantExprNode: %s %s", node.exprType.toString(), node.value)
        );
        return null;
    }

    @Override
    public Void visit(BoolConstantExprNode node) {
        print(String.format(
                "BoolConstantExprNode: %s %b", node.exprType.toString(), node.value)
        );
        return null;
    }

    @Override
    public Void visit(NullConstantExprNode node) {
        print(String.format(
                "NullConstantExprNode: %s", node.exprType.toString())
        );
        return null;
    }

    @Override
    public Void visit(VarNameExprNode node) {
        print(String.format(
                "VarNameExprNode: %s", node.name)
        );
        return null;
    }

    @Override
    public Void visit(FuncCallExprNode node) {
        print(String.format("FuncCallExprNode: %s", node.exprType.toString()));
        //visit children
        ++depth;
        node.func.accept(this);
        node.parameter.accept(this);
        --depth;
        return null;
    }

    @Override
    public Void visit(ArrayVisExprNode node) {
        print(String.format("ArrayVisExprNode: dim:%d", node.indexList.size()));
        //visit children
        ++depth;
        node.arrayName.accept(this);
        node.indexList.forEach(ind -> ind.accept(this));
        --depth;
        return null;
    }

    @Override
    public Void visit(PointerExprNode node) {
        print("PointerExprNode");
        return null;
    }

    //other
    @Override
    public Void visit(TypeNode node) {
        print(String.format("TypeNode: %s", node.type.toString()));
        return null;
    }

    @Override
    public Void visit(VarDefUnitNode node) {
        print(String.format("VarDefUnitNode: %s", node.name));
        //visit children
        ++depth;
        node.typeNode.accept(this);
        if (node.initExpr != null) {
            node.initExpr.accept(this);
        }
        --depth;
        return null;
    }

    @Override
    public Void visit(InitNode node) {
        print(String.format("InitNode: vars:%d", node.varDefUnitNodes.size()));
        //visit children
        ++depth;
        node.varDefUnitNodes.forEach(var -> var.accept(this));
        --depth;
        return null;
    }

    @Override
    public Void visit(ParameterNode node) {
        print(String.format("ParameterNode: paras:%d", node.parameterList.size()));
        //visit children
        ++depth;
        node.parameterList.forEach(para -> para.accept(this));
        --depth;
        return null;
    }

    @Override
    public Void visit(ClassDefNode node) {
        print(String.format(
                "ClassDefNode: %s mem:%d", node.name, node.members.size())
        );
        //visit children
        ++depth;
        node.members.forEach(stmt -> stmt.accept(this));
        --depth;
        return null;
    }
}
