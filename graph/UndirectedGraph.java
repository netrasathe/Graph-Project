package graph;
import java.util.ArrayList;
/* See restrictions in Graph.java. */

/** Represents an undirected graph.  Out edges and in edges are not
 *  distinguished.  Likewise for successors and predecessors.
 *
 *  @author Netra Sathe
 */
public class UndirectedGraph extends GraphObj {

    @Override
    public boolean isDirected() {
        return false;
    }

    @Override
    public int inDegree(int v) {
        if (contains(v)) {
            return adj.get(find(v)).getTotal().size();
        }
        return 0;
    }

    @Override
    public int add(int u, int v) {
        int uParent = find(u);
        ArrayList temp = adj.get(uParent).getSuccessor();
        for (int i = 0; i < temp.size(); i++) {
            if (temp.get(i).equals(v)) {
                return 0;
            }
        }
        adj.get(uParent).addSuccessor(v);
        adj.get(uParent).addTotal(v);
        if (!(u == v)) {
            int vParent = find(v);
            adj.get(vParent).addPredecessor(u);
            adj.get(vParent).addTotal(u);
        }
        int[] temporary = {u, v};
        edgy.add(temporary);
        return edgeId(u, v);
    }

    @Override
    public void remove(int u, int v) {
        int uParent = find(u);
        int vParent = find(v);
        adj.get(uParent).removeSuccessor(v);
        adj.get(uParent).removeTotal(v);
        if (!(u == v)) {
            adj.get(vParent).removePredecessor(u);
            adj.get(vParent).removeTotal(u);
        }
        for (int i = 0; i < edgy.size(); i++) {
            if (edgy.get(i)[0] == u && edgy.get(i)[1] == v) {
                edgy.remove(i);
            }
        }
    }

    @Override
    public boolean contains(int u, int v) {
        if (contains(u) && contains(v)) {
            return adj.get(find(u)).getTotal().contains(v);
        }
        return false;
    }

    @Override
    public int successor(int v, int k) {
        return predecessor(v, k);
    }

    @Override
    public Iteration<Integer> successors(int v) {
        return predecessors(v);
    }

    @Override
    public int predecessor(int v, int k) {
        return adj.get(find(v)).getTotal().get(k);
    }

    @Override
    public Iteration<Integer> predecessors(int v) {
        if (!contains(v)) {
            ArrayList<Integer> bait = new ArrayList<>();
            return Iteration.iteration(bait);
        }
        return Iteration.iteration(adj.get(find(v)).getTotal().iterator());
    }

    /** Position array */
    /**
     *
     * @param v input representing the value of the item.
     * @return item's position in the array.
     */
    private int find(int v) {
        checkMyVertex(v);
        for (int i = 0; i < adj.size(); i++) {
            if (adj.get(i).getValue() == v) {
                return i;
            }
        }
        return 0;
    }
}

