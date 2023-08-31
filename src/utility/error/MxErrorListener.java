package utility.error;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import utility.Position;

/**
 * @author F
 * remove 默认errorListener
 * 使用自定义的，检查词法语法错误
 * 遇到错误时，抛出SyntaxException
 */
public class MxErrorListener extends BaseErrorListener {
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer,
                            Object offendingSymbol,
                            int line,
                            int charPositionInLine,
                            String msg,
                            RecognitionException e) {
        throw new SyntaxException(
                new Position(line, charPositionInLine),
                msg
        );
    }
}
