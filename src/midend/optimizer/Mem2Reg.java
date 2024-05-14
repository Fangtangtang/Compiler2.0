package midend.optimizer;

import ir.function.Function;

import java.util.*;

/**
 * @author F
 * promote memory to register
 * todo：暂时认为很早很早以前写的 dom tree 相关的没问题
 */
public class Mem2Reg {

    Function function;

    public void execute(Function func) {
        function=func;

    }


}
