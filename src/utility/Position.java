package utility;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * @author F
 * Position类，用于维护node、token在源码中的位置
 * 输出行列信息用于调试
 */
public class Position {

    private final int row;
    private final int column;

    public Position(int row, int col) {
        this.row = row;
        this.column = col;
    }

    /**
     * 记录Token的位置属性
     */
    public Position(Token token) {
        this.row = token.getLine();
        this.column = token.getCharPositionInLine();
    }

    /**
     * 词（终结符）
     * 代表的在源码中的Token
     */
    public Position(TerminalNode terminalNode) {
        this(terminalNode.getSymbol());
    }

    /**
     * Rule
     * 结点在源码中的起始Token
     */
    public Position(ParserRuleContext ctx) {
        this(ctx.getStart());
    }

    @Override
    public String toString() {
        return "row:" + row + "\t" + "column:" + column;
    }
}