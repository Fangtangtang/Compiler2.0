package utility.live;

/**
 * @author F
 * 全局活跃范围
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

    public GlobalLiveRange find() {
        if (father == null) {
            return this;
        }
        return father = father.find();
    }

    public void union(GlobalLiveRange lr) {
        GlobalLiveRange root1 = this.find(), root2 = lr.find();
        if (root1 == root2) {
            return;
        }
        root1.father = root2;
    }

    //所处的并查集的重命名
    public String setName() {
        return this.find().rename;
    }
}
