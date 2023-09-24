package backend.optimizer.interferenceGraph;

import java.util.HashSet;
import java.util.List;

import static backend.optimizer.interferenceGraph.Colors.Color.*;

/**
 * @author F
 * 被染色的所有颜色
 */
public class Colors {
    public enum Color {
        zero,
        ra, sp, gp, tp,
        fp,
        t0, t1, t2, t3, t4, t5, t6,
        a0, a1, a2, a3, a4, a5, a6, a7,
        s1,
        s2, s3, s4, s5, s6, s7, s8, s9, s10, s11
    }

    //可被用于染色的color
    //sp，fp全程被占用；zero不能被赋值
    static Color[] availableColor = {
            t0, t1, t2, t3, t4, t5, t6,//caller saved
            a0, a1, a2, a3, a4, a5, a6, a7,//caller saved
//            s1,
            s2, s3, s4, s5, s6, s7, s8, s9, s10, s11//callee saved
    };

    public HashSet<Color> available;

    public Colors() {
        available = new HashSet<>();
        available.addAll(List.of(availableColor));
    }

}
