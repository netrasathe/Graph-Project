package graph;

/* See restrictions in Graph.java. */

import java.util.ArrayList;


/** A partial implementation of Graph containing elements common to
 *  directed and undirected graphs.
 *
 *  @author Netra Sathe
 */
abstract class GraphObj extends Graph {

    /** An ArrayList of LinkedLists for the adj list. */
    protected ArrayList<Node> adj;

    /** An ArrayList consisting of all of the intz. */
    protected ArrayList<Integer> done;

    /** An ArrayList for edgez. */
    protected ArrayList<int[]> edgy;


    /** A new Graph object. */
    GraphObj() {
        adj = new ArrayList<>();
        done = new ArrayList<>();
        edgy = new ArrayList<int[]>();
    }

    @Override
    public int vertexSize() {
        return adj.size();
    }

    @Override
    public int maxVertex() {
        int maxx = 0;
        for (int i = 0; i < adj.size(); i += 1) {
            maxx = Integer.max(maxx, adj.get(i).getValue());
        }
        return maxx;
    }

    @Override
    public int edgeSize() {
        return edgy.size();
    }

    @Override
    public abstract boolean isDirected();

    @Override
    public int outDegree(int v) {
        int out = 0;
        if (contains(v)) {
            for (int i:successors(v)) {
                out++;
            }
        }
        return out;
    }

    @Override
    public abstract int inDegree(int v);

    @Override
    public boolean contains(int u) {
        if (done.contains(u)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean contains(int u, int v) {
        if (contains(u) && contains(v)) {
            return adj.get(find(u)).getSuccessor().contains(v);
        }
        return false;
    }

    @Override
    public int add() {
        Integer indx = 1;
        while (done.contains(indx)) {
            indx++;
        }
        Node temp = new Node(indx);
        adj.add(temp);
        done.add(indx);
        return indx;
    }

    @Override
    public int add(int u, int v) {
        int p = find(u);
        ArrayList neww = adj.get(p).getSuccessor();
        for (int i = 0; i < neww.size(); i++) {
            if (neww.get(i).equals(v)) {
                return 0;
            }
        }
        adj.get(p).addSuccessor(v);
        adj.get(p).addTotal(v);

        int par = find(v);
        adj.get(par).addPredecessor(u);
        adj.get(par).addTotal(u);

        int[] t = {u, v};
        edgy.add(t);
        return edgeId(u, v);
    }

    @Override
    public void remove(int v) {
        int k = find(v);

        ArrayList<Integer> succ = adj.get(k).getSuccessor();
        ArrayList<Integer> pred = adj.get(k).getPredecessor();

        while (!succ.isEmpty()) {
            remove(v, succ.get(0));
        }
        while (!pred.isEmpty()) {
            remove(pred.get(0), v);
        }
        adj.remove(k);
        done.remove(done.indexOf(v));
    }

    /** Removes from edges. */
    @Override
    public void remove(int u, int v) {
        int p = find(u);
        int par = find(v);
        adj.get(p).removeSuccessor(v);
        adj.get(p).removeTotal(v);
        adj.get(par).removePredecessor(u);
        adj.get(par).removeTotal(u);
        for (int i = 0; i < edgy.size(); i++) {
            if (edgy.get(i)[0] == u && edgy.get(i)[1] == v) {
                edgy.remove(i);
            }
        }
    }

    @Override
    public Iteration<Integer> vertices() {
        ArrayList<Integer> out = new ArrayList<>();
        int ind = 1;
        while (out.size() < done.size()) {
            if (done.contains(ind)) {
                out.add(ind);
            }
            ind++;
        }
        return Iteration.iteration(out.iterator());
    }

    @Override
    public int successor(int v, int k) {
        int position = find(v);
        return adj.get(position).getSuccessor().get(k);
    }

    @Override
    public Iteration<Integer> successors(int v) {
        if (!contains(v)) {
            ArrayList<Integer> lol = new ArrayList<>();
            return Iteration.iteration(lol);
        }
        return Iteration.iteration(adj.get(find(v)).getSuccessor().iterator());
    }

    @Override
    public abstract Iteration<Integer> predecessors(int v);

    @Override
    public abstract int predecessor(int v, int u);

    @Override
    public Iteration<int[]> edges() {
        return Iteration.iteration(edgy.iterator());
    }

    @Override
    protected void checkMyVertex(int v) {
        if (!contains(v)) {
            throw new IllegalArgumentException("vertex not from Graph");
        }
    }

    @Override
    protected int edgeId(int u, int v) {
        if (!contains(u, v)) {
            return 0;
        }
        if (!isDirected()) {
            int upper = Integer.max(u, v);
            int lower = Integer.min(u, v);
            return ((upper + lower) * (upper + lower + 1) / 2 + lower);
        }
        return ((u + v) * (u + v + 1) / 2 + v);
    }

    /** Finds parent loc.
     *
     * @param v value whose node position needs to be found.
     * @return the index number of the node needed.
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

    /** private node with vertex info. */
    protected class Node {
        /** Values stored. */
        private int valyoo;
        /** Successor list. */
        private ArrayList<Integer> succ;
        /** Pred list. */
        private ArrayList<Integer> pred;
        /** All edges list. */
        private ArrayList<Integer> edges;

        /** The Constructor.
         *
         * @param val value of the node.
         */
        Node(int val) {
            valyoo = val;
            succ = new ArrayList<Integer>();
            pred = new ArrayList<Integer>();
            edges = new ArrayList<Integer>();
        }

        /** Get method to get predecessor.
         * @return the list of predecessors.
         */
        ArrayList<Integer> getPredecessor() {
            return pred;
        }

        /** Get method to return successor.
         * @return the list of successors.
         */
        ArrayList<Integer> getSuccessor() {
            return succ;
        }

        /** Get method to return all edges.
         * @return the list of all edges to and from.
         */
        ArrayList<Integer> getTotal() {
            return edges;
        }

        /** removes the predecessor of the value n.
         * @param n remove the nth predecessor.
         */
        void removePredecessor(int n) {
            pred.remove(pred.indexOf(n));
        }

        /** Setter method to remove successor.
         * @param n return the nth successor.
         */
        void removeSuccessor(int n) {
            succ.remove(succ.indexOf(n));
        }

        /** Setter method to remove item.
         * @param n remove the nth item from totals.
         */
        void removeTotal(int n) {
            edges.remove(edges.indexOf(n));
        }

        /** adds an edge to the predecessor to item n.
         * @param n value to be added as predecessor.
         */
        void addPredecessor(int n) {
            pred.add(n);
        }

        /** Setter method to set successor.
         * @param n integer to be added as successor.
         */
        void addSuccessor(int n) {
            succ.add(n);
        }

        /** Setter method to add edge.
         * @param n integer to be added to total.
         */
        void addTotal(int n) {
            if (isDirected()) {
                edges.add(n);
            } else if (!edges.contains(n)) {
                edges.add(n);
            }
        }

        /** A get method.
         * @return value of node.
         */
        int getValue() {
            return valyoo;
        }
    }
}
