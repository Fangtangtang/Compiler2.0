package backend.optimizer.interferenceGraph;

import utility.Pair;

import java.util.*;

/**
 * @author F
 * 冲突图
 */
public class InterferenceGraph {
    //冲突边集合
    public HashSet<Pair<Node, Node>> adjSet = new HashSet<>();

    //邻接表表示
    public HashMap<Node, HashSet<Node>> adjList = new HashMap<>();

    public void addEdge(Node u, Node v) {
        if (u.equals(v)) {
            return;
        }
        Pair<Node, Node> u2v = new Pair<>(u, v), v2u = new Pair<>(v, u);
        if (adjSet.contains(u2v)) {
            return;
        }
        //add edge to adjSet
        adjSet.add(u2v);
        adjSet.add(v2u);
        if (!(u instanceof PrecoloredNode)) {
            adjList.get(u).add(v);
            u.degree += 1;
        }
        if (!(v instanceof PrecoloredNode)) {
            adjList.get(v).add(u);
            v.degree += 1;
        }
    }

    public HashSet<Node> getAdjList(Node node) {
        return adjList.get(node);
    }
}
