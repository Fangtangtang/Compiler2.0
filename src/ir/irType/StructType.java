package ir.irType;

import java.util.*;

/**
 * @author F
 * IR结构体
 * 为一个特殊的指针，指向结构体组成元素的一大块空间
 * 类的数据成员抽取为一个结构体
 * name -> index -> IRType （接近LLVM IR的输出）
 */
public class StructType extends IRType {
    public String name;
    public boolean padding = false;
    public HashMap<String, Integer> members = new HashMap<>();

    public ArrayList<IRType> memberTypes = new ArrayList<>();

    public StructType(String name) {
        this.name = name;
    }

    public int name2Index(String name) {
        return members.get(name);
    }

    public IRType name2Type(String name) {
        return memberTypes.get(members.get(name));
    }

    public void addMember(String name, IRType irType) {
        members.put(name, memberTypes.size());
        memberTypes.add(irType);
    }

    //TODO:对齐？空间优化
    @Override
    public int getSize() {
        if (size > 0) {
            return size;
        }
        size = 32 * memberTypes.size();
        return size;
    }

    @Override
    public String toString() {
        return "%class." + name;
    }

    private String typeSpace(IRType type) {
        if (type instanceof StructType structType) {
            return structType.toString();
        }
        if (type instanceof IntType intType) {
            if (intType.typeName != IntType.TypeName.INT) {
                padding = true;
                return "i8, [3 x i8]";
            } else {
                return "i32";
            }
        }
        return "ptr";
    }

    public String memberInformation() {
        StringBuilder str = new StringBuilder();
        IRType type;
        if (memberTypes.size() > 0) {
            str.append(typeSpace(memberTypes.get(0)));
        }
        for (int i = 1; i < memberTypes.size(); ++i) {
            str.append(", ").append(typeSpace(memberTypes.get(i)));
        }
        return str.toString();
    }
}
