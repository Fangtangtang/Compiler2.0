package utility.error;

import utility.Position;

/**
 * @author F
 * 语法错误抛出异常
 */
public class InternalException extends MxException {
    public InternalException(String message) {
        super("InternalException:\t" + message);
    }
}

