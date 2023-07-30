package utility.type;

import ast.other.*;
import ast.stmt.*;

import java.util.*;

/**
 * @author F
 * 函数类型
 */
public class FunctionType extends Type {
    public String name;
    public Type returnType;
    public ArrayList<VarDefUnitNode> parameters = new ArrayList<>();
    public BlockStmtNode functionBody;

    public FunctionType() {
        this.typeName = Types.FUNCTION;
    }

    public FunctionType(String name,
                        Type returnType) {
        this.typeName = Types.FUNCTION;
        this.name = name;
        this.returnType = returnType;
    }
}
