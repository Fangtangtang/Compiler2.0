package utility;

/**
 * @author F
 * 活跃范围
 * 类似并查集
 */
public class GlobalLiveRange {
    //变量的重命名
    public String rename;
    //树形并查集
    GlobalLiveRange father = null;

    public GlobalLiveRange(String rename) {
        this.rename = rename;
    }

    public GlobalLiveRange find(GlobalLiveRange lr) {
        if (father == null) {
            return lr;
        }
        return lr.father = find(lr.father);
    }

    public void union(GlobalLiveRange lr1,
                      GlobalLiveRange lr2) {
        GlobalLiveRange root1 = find(lr1), root2 = find(lr2);
        if (root1 == root2) {
            return;
        }
        root1.father = root2;
    }
}
