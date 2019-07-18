package graph;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Queue;

/* See restrictions in Graph.java. */

/** Implements a generalized traversal of a graph.  At any given time,
 *  there is a particular collection of untraversed vertices---the "fringe."
 *  Traversal consists of repeatedly removing an untraversed vertex
 *  from the fringe, visting it, and then adding its untraversed
 *  successors to the fringe.
 *
 *  Generally, the client will extend Traversal.  By overriding the visit
 *  method, the client can determine what happens when a node is visited.
 *  By supplying an appropriate type of Queue object to the constructor,
 *  the client can control the behavior of the fringe. By overriding the
 *  shouldPostVisit and postVisit methods, the client can arrange for
 *  post-visits of a node (as in depth-first search).  By overriding
 *  the reverseSuccessors and processSuccessor methods, the client can control
 *  the addition of neighbor vertices to the fringe when a vertex is visited.
 *
 *  Traversals may be interrupted or restarted, remembering the previously
 *  marked vertices.
 *  @author Netra Sathe
 */
public abstract class Traversal {

    /** Array for nodes. */
    private ArrayList<Integer> arrayy;
    /** Indices track. */
    private ArrayList<Integer> ind;

    /** A Traversal of G, using FRINGE as the fringe. */
    protected Traversal(Graph G, Queue<Integer> fringe) {
        graphh = G;
        friinge = fringe;
        arrayy = new ArrayList<>();
        ind = new ArrayList<>();
    }

    /** All vertices in the graph. */
    public void clear() {
        arrayy.clear();
    }

    /** Initialize the fringe to V0 and perform a traversal. */
    public void traverse(Collection<Integer> V0) {
        friinge.clear();
        friinge.addAll(V0);
        while (!friinge.isEmpty()) {
            int item = friinge.poll();

            if (!marked(item)) {
                mark(item);
                if (!visit(item)) {
                    break;
                }

                if (shouldPostVisit(item) && !reverseSuccessors(item)) {
                    friinge.add(item);
                }

                if (!reverseSuccessors(item)) {
                    for (int i : graphh.successors(item)) {
                        if (processSuccessor(item, i)) {
                            friinge.add(i);
                        }
                    }
                } else {
                    ArrayList<Integer> reverser = new ArrayList<>();
                    while (!friinge.isEmpty()) {
                        reverser.add(friinge.poll());
                    }
                    for (int i : graphh.successors(item)) {
                        if (processSuccessor(item, i)) {
                            friinge.add(i);
                        }
                    }
                    if (shouldPostVisit(item)) {
                        friinge.add(item);
                    }
                    friinge.addAll(reverser);
                }
            } else if (!ind.contains(item)) {
                postVisit(item);
                ind.add(item);
            }
        }
    }

    /** Initialize the fringe to { V0 } and perform a traversal. */
    public void traverse(int v0) {
        traverse(Arrays.<Integer>asList(v0));
    }

    /** Returns true if V has been marked. */
    protected boolean marked(int v) {
        if (arrayy.contains(v)) {
            return true;
        }
        return false;
    }

    /** Mark vertex V. */
    protected void mark(int v) {
        arrayy.add(v);
    }

    /** Perform a visit on vertex V.
     * @return boolean */
    protected boolean visit(int v) {
        return true;
    }

    /** Return true if we should postVisit V after traversing its
     *  successors.  (Post-visiting generally is useful only for depth-first
     *  traversals, although we define it for all traversals.) */
    protected boolean shouldPostVisit(int v) {
        return false;
    }

    /** Revisit vertex V after traversing its successors.  Returns false iff
     *  the traversal is to terminate immediately. */
    protected boolean postVisit(int v) {
        return true;
    }

    /** Return true if we should schedule successors of V in reverse order. */
    protected boolean reverseSuccessors(int v) {
        return false;
    }

    /** Process the successors of vertex U.  Assumes U has been visited.  This
     *  default implementation simply processes each successor using
     *  processSuccessor. */
    protected void processSuccessors(int u) {
        for (int v : graphh.successors(u)) {
            if (processSuccessor(u, v)) {
                friinge.add(v);
            }
        }
    }

    /** Process successor V to U.  Returns true iff V is then to
     *  be added to the fringe.  By default, returns true iff V is unmarked. */
    protected boolean processSuccessor(int u, int v) {
        return !marked(v);
    }

    /** Traversed graph. */
    private final Graph graphh;
    /** Fringe finally. */
    protected final Queue<Integer> friinge;
}
