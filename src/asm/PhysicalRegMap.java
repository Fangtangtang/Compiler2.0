package asm;

import asm.operand.PhysicalRegister;

import java.util.HashMap;

/**
 * @author F
 * 所有asm中物理寄存器的hashMap
 */
public class PhysicalRegMap {
    public HashMap<String, PhysicalRegister> registerMap = new HashMap<>();

    public PhysicalRegMap() {
        //恒定为0
        registerMap.put("zero", new PhysicalRegister("zero"));
        //return address
        registerMap.put("ra", new PhysicalRegister("ra"));
        //stack pointer
        registerMap.put("sp", new PhysicalRegister("sp"));
        //frame pointer
        registerMap.put("fp", new PhysicalRegister("fp"));
        //临时量
        //TODO：useful？
        registerMap.put("t0", new PhysicalRegister("t0"));
        registerMap.put("t1", new PhysicalRegister("t1"));
        registerMap.put("t2", new PhysicalRegister("t2"));
        //8个可用寄存器
        registerMap.put("a0", new PhysicalRegister("a0"));
        registerMap.put("a1", new PhysicalRegister("a1"));
        registerMap.put("a2", new PhysicalRegister("a2"));
        registerMap.put("a3", new PhysicalRegister("a3"));
        registerMap.put("a4", new PhysicalRegister("a4"));
        registerMap.put("a5", new PhysicalRegister("a5"));
        registerMap.put("a6", new PhysicalRegister("a6"));
        registerMap.put("a7", new PhysicalRegister("a7"));
    }

    public PhysicalRegister getReg(String name) {
        return registerMap.get(name);
    }
}
