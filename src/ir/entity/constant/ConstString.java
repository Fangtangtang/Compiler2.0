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
    String converted;
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
        this.converted = "";
        for (int i = 0; i < val.length(); ++i) {
            char c = val.charAt(i);
            if (c == '\\') {
                if (i + 1 < val.length()) {
                    ++i;
                    --strLength;//转义字符
                    c = val.charAt(i);
                    if (c == 'n') {
                        this.converted += "\\0A";
                    } else if (c == '\"') {
                        this.converted += "\\22";
                    } else if (c == '\\') {
                        this.converted += "\\\\";
                    } else {
                        this.converted += '\\';
                        this.converted += c;
                    }
                } else {
                    this.converted += '\\';
                }
            } else {
                this.converted += c;
            }
        }
        this.converted += "\\00";
    }

    @Override
    public String toString() {
        if (this.converted.length() > 0) {
            return ("[" + (strLength + 1) + " x i8] c" +
                    "\"" + this.converted + "\""
            );
        } else {
            return "[1 x i8] zeroinitializer";
        }
    }

    @Override
    public String renamedToString() {
        if (this.converted.length() > 0) {
            return ("[" + (strLength + 1) + " x i8] c" +
                    "\"" + this.converted + "\""
            );
        } else {
            return "[1 x i8] zeroinitializer";
        }
    }
}
