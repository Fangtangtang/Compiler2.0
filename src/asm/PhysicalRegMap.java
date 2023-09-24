package asm;

import asm.operand.PhysicalRegister;
import backend.optimizer.interferenceGraph.Colors;

import java.util.HashMap;

/**
 * @author F
 * 所有asm中物理寄存器的hashMap
 */
public class PhysicalRegMap {
    public HashMap<String, PhysicalRegister> registerMap = new HashMap<>();

    public PhysicalRegMap() {
        for (Colors.Color regColor : Colors.Color.values()) {
            registerMap.put(regColor.name(), new PhysicalRegister(regColor.name()));
        }
    }

    public PhysicalRegister getReg(String name) {
        return registerMap.get(name);
    }

    public PhysicalRegister getReg(Colors.Color color) {
        return registerMap.get(color.name());
    }

}
