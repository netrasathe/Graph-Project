package graph;

/* See restrictions in Graph.java. */

import java.util.ArrayList;

/** Represents a general unlabeled directed graph whose vertices are denoted by
 *  positive integers. Graphs may have self edges.
 *
 *  @author Netra Sathe
 */
public class DirectedGraph extends GraphObj {

    @Override
    public boolean isDirected() {
        return true;
    }

    @Override
    public int inDegree(int v) {
        if (contains(v)) {
            return adj.get(find(v)).getPredecessor().size();
        }
        return 0;
    }

    @Override
    public int predecessor(int v, int k) {
        int vPos = find(v);
        ArrayList<Integer> temp = adj.get(vPos).getPredecessor();
        return temp.get(k);
    }

    @Override
    public Iteration<Integer> predecessors(int v) {
        if (!contains(v)) {
            ArrayList<Integer> bait = new ArrayList<>();
            return Iteration.iteration(bait);
        }
        return Iteration.iteration(adj.get(find(v))
                .getPredecessor().iterator());
    }

    /** given an edge value, find the array parent location. */
    /**
     *
     * @param v value whose node needs to be found.
     * @return index number of the node needed.
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
