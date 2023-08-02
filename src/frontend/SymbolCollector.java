package frontend;

import ast.*;
import ast.other.ClassDefNode;
import ast.other.TypeNode;
import ast.stmt.ConstructorDefStmtNode;
import ast.stmt.FuncDefStmtNode;
import ast.stmt.VarDefStmtNode;
import utility.*;
import utility.error.SemanticException;
import utility.scope.Scope;
import utility.type.ArrayType;
import utility.type.ClassType;
import utility.type.FunctionType;
import utility.type.Type;

import java.util.Objects;

/**
 * @author F
 * 遍历AST结点
 * 收集全局作用域内的类、函数
 * 扫两遍
 * - 收集所有的类名
 * - 收集所有函数、类的成员，并检查函数的返回类型、参数类型
 * TODO：变量名和类名可以重复什么意思
 */
public class SymbolCollector extends ASTBaseVisitor<Type> {
    //自定义类，
    //指向main中同一个symbolTable
    private SymbolTable symbolTable;

    public SymbolCollector(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    @Override
    public Type visit(RootNode node) {
        //第一趟，访问root的所有class子结点
        for (ASTNode childNode : node.declarations) {
            if (childNode instanceof ClassDefNode tmp) {
                symbolTable.addSymbol(
                        tmp.name, new ClassType(tmp.name), tmp.pos
                );
            }
        }
        //第二趟，访问所有函数、类的成员，并检查函数的返回类型、参数类型
        //TODO:类成员中的构造函数怎么处理
        for (ASTNode childNode : node.declarations) {
            if (childNode instanceof ClassDefNode tmp) {
                visit(tmp);
            } else if (childNode instanceof FuncDefStmtNode tmp) {
                symbolTable.addSymbol(tmp.name, visit(tmp), tmp.pos);
            }
        }
        //检查全局是否有合法main
        //有且仅有1个函数的名字可以为 main，
        //main函数的定义仅可为 int main()
        symbolTable.checkMain(node.pos);
        return null;
    }

    @Override
    public Type visit(ClassDefNode node) {
        ClassType classType = (ClassType) symbolTable.getSymbol(node.name);
        for (ASTNode childNode : node.members) {
            //类的成员函数
            if (childNode instanceof FuncDefStmtNode tmp) {
                if (classType.classMembers.containsKey(tmp.name)) {
                    throw new SemanticException(tmp.pos, "multiple definition of " + tmp.name + " in class");
                }
                classType.classMembers.put(tmp.name, visit(tmp));
            }
            //类的成员变量
            else if (childNode instanceof VarDefStmtNode tmp) {
                tmp.varDefUnitNodes.forEach(
                        var -> {
                            if (classType.classMembers.containsKey(var.name)) {
                                throw new SemanticException(var.pos, "multiple definition of " + var.name + " in class");
                            }
                            classType.classMembers.put(var.name, visit(var.typeNode));
                        }
                );
            } else if (childNode instanceof ConstructorDefStmtNode) {
                classType.constructor = new FunctionType();
                classType.constructor.functionBody = ((ConstructorDefStmtNode) childNode).suite;
            } else {
                throw new SemanticException(node.pos, "invalid member in class " + node.name);
            }
        }
        return classType;
    }

    //访问函数定义结点，检查返回值和参数类型
    //若均合法，加入symbolTable
    @Override
    public Type visit(FuncDefStmtNode node) {
        symbolTable.getSymbol(
                node.returnType.type.toString(),
                node.returnType.pos
        );
        //TODO:浅拷贝？指向同一个地址？
        FunctionType func = new FunctionType(node.returnType.type);
        if (node.parameterList != null) {
            node.parameterList.varDefUnitNodes.forEach(
                    var -> {
                        func.parameters.add(
                                new ParameterUnit(visit(var.typeNode), var.name)
                        );
                    }
            );
        }
        return func;
    }

    /**
     * 判断类型是否合法
     *
     * @param node TypeNode
     * @return type
     */
    @Override
    public Type visit(TypeNode node) {
        if (!(node.type instanceof ArrayType)) {
            node.type = symbolTable.getSymbol(node.type.toString(), node.pos);
        } else {
            ((ArrayType) node.type).eleType = symbolTable.getSymbol(node.type.toString(), node.pos);
        }
        return node.type;
    }
}
