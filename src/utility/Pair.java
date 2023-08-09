package utility;

/**
 * @author F
 * 数据对
 */
public class Pair<F, S> {
    private F first;
    private S second;

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }

    public void setFey(F first) {
        this.first = first;
    }

    public void setSecond(S second) {
        this.second = second;
    }
}
