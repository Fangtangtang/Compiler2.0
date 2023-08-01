package utility.error;

import utility.Position;

/**
 * @author F
 * 语法错误抛出异常
 */
public class SemanticException extends MxException {

    public SemanticException(Position pos, String message) {
        super(pos, "SemanticException:\t" + message);
    }
}
