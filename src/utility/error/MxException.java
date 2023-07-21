package utility.error;

import utility.Position;

/**
 * @author F
 * Mx异常的抽象类
 * 从RuntimeException（程序逻辑错误）继承
 */
public class MxException extends RuntimeException {
    private final Position pos;
    private final String message;

    public MxException(Position pos, String message) {
        this.pos = pos;
        this.message = message;
    }

    //输出报错信息
    @Override
    public String toString() {
        return message + ": " + pos.toString();
    }
}
