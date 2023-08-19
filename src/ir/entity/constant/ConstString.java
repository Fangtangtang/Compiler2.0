package ir.entity.constant;

import ir.irType.ArrayType;
import ir.irType.IRType;
import ir.irType.IntType;
import ir.irType.PtrType;

/**
 * @author F
 * 字符串常量
 * IR类型：字符数组
 */
public class ConstString extends Constant {

    public String value;
    public int strLength;

    public ConstString(String value) {
        super(new ArrayType(
                new IntType(IntType.TypeName.CHAR),
                1
        ));
        this.value = (!"".equals(value) && value.charAt(0) == '"') ?
                value.substring(1, value.length() - 1) : value;
        strLength = this.value.length();
        convertStr();
    }

    private void convertStr() {
        String val = this.value;
        this.value = "";
        for (int i = 0; i < val.length(); ++i) {
            char c = val.charAt(i);
            if (c == '\\') {
                if (i + 1 < val.length()) {
                    ++i;
                    --strLength;//转义字符
                    c = val.charAt(i);
                    if (c == 'n') {
                        this.value += "\\0A";
                    } else if (c == '\"') {
                        this.value += "\\22";
                    } else if (c == '\\') {
                        this.value += "\\\\";
                    } else {
                        this.value += '\\';
                        this.value += c;
                    }
                } else {
                    this.value += '\\';
                }
            } else {
                this.value += c;
            }
        }
        this.value += "\\00";
    }

    @Override
    public String toString() {
//        return type.toString() + " " + value;
        if (this.value.length() > 0) {
            return ("[" + (strLength + 1) + " x i8] c" +
                    "\"" + this.value + "\""
            );
        } else {
            return "[1 x i8] zeroinitializer";
        }
    }
}
