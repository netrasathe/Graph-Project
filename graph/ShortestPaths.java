package graph;

/* See restrictions in Graph.java. */

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.Iterator;
import java.util.AbstractQueue;

/** The shortest paths through an edge-weighted graph.
 *  By overrriding methods getWeight, setWeight, getPredecessor, and
 *  setPredecessor, the client can determine how to represent the weighting
 *  and the search results.  By overriding estimatedDistance, clients
 *  can search for paths to specific destinations using A* search.
 *  @author Netra Sathe
 */
public abstract class ShortestPaths {

    /** The shortest paths in G from SOURCE. */
    public ShortestPaths(Graph G, int source) {
        this(G, source, 0);
    }

    /** Prev node. */
    protected int[] back;
    /** Search the graph. */
    protected final Graph graphh;
    /** Beginning source vertex. */
    private final int src;
    /** End vertex. */
    private final int desti;
    /** The fringe. */
    protected final DistanceQueue friinge;
    /** Distance of node to start. */
    protected double[] distance;

    /** A shortest path in G from SOURCE to DEST. */
    public ShortestPaths(Graph G, int source, int dest) {
        graphh = G;
        src = source;
        desti = dest;
        friinge = new DistanceQueue();
        back = new int[graphh.vertexSize() + 1];

        distance = new double[graphh.vertexSize() + 1];
        for (int i = 0; i < graphh.vertexSize() + 1; i += 1) {
            back[i] = 0;
            distance[i] = Double.MAX_VALUE;
        }
        distance[src] = 0.0;
    }

    /** Initialize the shortest paths.  Must be called before using
     *  getWeight, getPredecessor, and pathTo. */
    public void setPaths() {
        Astar traverse = new Astar(graphh, friinge);
        traverse.traverse(src);
    }

    /** Returns the starting vertex. */
    public int getSource() {
        return src;
    }

    /** Returns the target vertex, or 0 if there is none. */
    public int getDest() {
        return desti;
    }

    /** Returns the current weight of vertex V in the graph.  If V is
     *  not in the graph, returns positive infinity. */
    public abstract double getWeight(int v);

    /** Set getWeight(V) to W. Assumes V is in the graph. */
    protected abstract void setWeight(int v, double w);

    /** Returns the current predecessor vertex of vertex V in the graph, or 0 if
     *  V is not in the graph or has no predecessor. */
    public abstract int getPredecessor(int v);

    /** Set getPredecessor(V) to U. */
    protected abstract void setPredecessor(int v, int u);

    /** Returns an estimated heuristic weight of the shortest path from vertex
     *  V to the destination vertex (if any).  This is assumed to be less
     *  than the actual weight, and is 0 by default. */
    protected double estimatedDistance(int v) {
        return 0.0;
    }

    /** Returns the current weight of edge (U, V) in the graph.  If (U, V) is
     *  not in the graph, returns positive infinity. */
    protected abstract double getWeight(int u, int v);

    /** Returns a list of vertices starting at _source and ending
     *  at V that represents a shortest path to V.  Invalid if there is a
     *  destination vertex other than V. */
    public List<Integer> pathTo(int v) {
        ArrayList<Integer> rev = new ArrayList<>();
        int b = v;
        while (b != 0) {
            rev.add(b);
            b = getPredecessor(b);
        }

        ArrayList<Integer> result = new ArrayList<>();
        for (int i = rev.size() - 1; i >= 0; i -= 1) {
            result .add(rev.get(i));
        }
        return result;
    }

    /** Returns a list of vertices starting at the source and ending at the
     *  destination vertex. Invalid if the destination is not specified. */
    public List<Integer> pathTo() {
        return pathTo(getDest());
    }

    /** Astar queue. */
    private class DistanceQueue extends AbstractQueue<Integer> {

        /** Queue construct. */
        DistanceQueue() {
            super();
            dataa = new TreeSet<>(new MyTree());
        }

        @Override
        public Integer peek() {
            return dataa.first();
        }

        @Override
        public Integer poll() {
            return dataa.pollFirst();
        }

        /** private treeset to store data. */
        private TreeSet<Integer> dataa;

        @Override
        public Iterator<Integer> iterator() {
            return dataa.iterator();
        }

        @Override
        public boolean offer(Integer n) {
            return dataa.add(n);
        }

        @Override
        public boolean add(Integer n) {
            return dataa.add(n);
        }

        @Override
        public int size() {
            return dataa.size();
        }

        @Override
        public void clear() {
            dataa.clear();
        }
    }

    /** Tree Comparator. */
    class MyTree implements Comparator<Integer> {

        /** Constructor. */
        MyTree() {
            super();
        }

        @Override
        public int compare(Integer a, Integer b) {
            int one = a;
            int two = b;

            if (estimatedDistance(one) + getWeight(one)
                    > estimatedDistance(two) + getWeight(two)) {
                return 1;
            } else if (estimatedDistance(one)
                    + getWeight(one)
                    == estimatedDistance(two)
                    + getWeight(two)) {
                return one - two;
            } else {
                return -1;
            }
        }
    }

    /** Astar search new. */
    class Astar extends Traversal {

        /** Traverse it.
         *
         * @param g graph things
         * @param fringe queue
         */
        Astar(Graph g, DistanceQueue fringe) {
            super(g, fringe);
        }


        /** Perform a visit on vertex V.  Returns false iff the traversal is to
         *  terminate immediately. */
        @Override
        protected boolean visit(int v) {
            if (v == desti) {
                return false;
            }
            return true;
        }

        @Override
        protected boolean shouldPostVisit(int item) {
            return false;
        }

        @Override
        protected boolean reverseSuccessors(int item) {
            return false;
        }

        /** Process successor V to U.  Returns true iff V is then to
         *  be added to the fringe. By default, returns true iff V is
         *  unmarked. */
        @Override
        protected boolean processSuccessor(int u, int v) {
            if (marked(v)) {
                return false;
            }
            if ((getWeight(u) + getWeight(u, v)) < getWeight(v)) {
                friinge.remove(v);
                setWeight(v, getWeight(u) + getWeight(u, v));
                setPredecessor(v, u);
                return true;
            }
            return false;
        }
    }
}
