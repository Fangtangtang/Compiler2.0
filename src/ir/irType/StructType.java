package ir.irType;

import java.util.*;

/**
 * @author F
 * IR结构体
 * 类的数据成员抽取为一个结构体
 * name -> index -> IRType （接近LLVM IR的输出）
 */
public class StructType extends IRType {
    private final String name;
    private final HashMap<String, Integer> members = new HashMap<>();

    private final ArrayList<IRType> memberTypes = new ArrayList<>();

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

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("class:").append(this.name).append("\n");
        for (String member : members.keySet()) {
            Integer index = members.get(member);
            str.append(member).append("\t").append(memberTypes.get(index).toString()).append("\n");
        }
        return str.toString();
    }
}
