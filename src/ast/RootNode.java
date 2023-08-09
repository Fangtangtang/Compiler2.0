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
    public ArrayList<ASTNode> declarations = new ArrayList<>();

    public RootNode(Position pos) {
        super(pos);
    }

    @Override
    public <T> T accept(ASTVisitor<? extends T> visitor) {
        return visitor.visit(this);
    }
}
