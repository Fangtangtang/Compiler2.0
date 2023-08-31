package ast.type;

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
        if (!(other instanceof FunctionType function)) {
            return false;
        }
        if (!this.returnType.equals(function.returnType)) {
            return false;
        }
        if (this.parameters.size() != function.parameters.size()) {
            return false;
        }
        for (int i = 0; i < this.parameters.size(); ++i) {
            if (!this.parameters.get(i).equals(((FunctionType) other).parameters.get(i))) {
                return false;
            }
        }
        return true;
    }
}
