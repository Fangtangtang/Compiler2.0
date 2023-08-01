package utility.type;

import ast.other.*;
import ast.stmt.*;
import utility.ParameterUnit;

import java.util.*;

/**
 * @author F
 * 函数类型
 */
public class FunctionType extends Type {
    public String name;
    public Type returnType;
    public ArrayList<ParameterUnit> parameters = new ArrayList<>();
    public BlockStmtNode functionBody;

    public FunctionType() {
        this.typeName = Types.FUNCTION;
    }

    public FunctionType(Type returnType) {
        this.typeName = Types.FUNCTION;
        this.returnType = returnType;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Type other) {
        return (other instanceof FunctionType);
    }
}
