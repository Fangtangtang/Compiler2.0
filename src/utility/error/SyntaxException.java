package utility.error;

import utility.Position;

/**
 * @author F
 * 句法错误抛出异常
 */
public class SyntaxException extends MxException {

    public SyntaxException(Position pos, String message) {
        super(pos, "SyntaxException " + message);
    }
}
