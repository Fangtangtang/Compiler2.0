package ast;

import ast.other.ClassDefNode;
import utility.Position;

import java.util.*;

/**
 * @author F
 * AST的根节点，代表g4文件中的program
 * ------------------------------------------------
 * program:
 * (funcDefStatement | declarationStatement)*
 * EOF
 * ;
 */
public class RootNode extends ASTNode {
    public ArrayList<StmtNode> declarations = new ArrayList<>();

    public ArrayList<ClassDefNode> classDefs = new ArrayList<>();

    public RootNode(Position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
