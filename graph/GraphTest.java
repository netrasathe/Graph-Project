package graph;

import org.junit.Test;
import static org.junit.Assert.*;

/** Unit tests for the Graph class.
 *  @author Netra Sathe
 */
public class GraphTest {

    @Test
    public void emptyGraph() {
        DirectedGraph g = new DirectedGraph();
        assertEquals("Initial graph has vertices", 0, g.vertexSize());
        assertEquals("Initial graph has edges", 0, g.edgeSize());
    }

    @Test
    public void graphTest() {
        DirectedGraph g = new DirectedGraph();
        g.add();
        g.add();
        assertEquals(2, g.vertexSize());
        assertEquals(0, g.edgeSize());
    }

    @Test
    public void testDirected() {
        DirectedGraph g = new DirectedGraph();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add(1, 2);
        g.add(1, 3);
        g.add(2, 3);
        assertTrue(g.isDirected());
        assertEquals(0, g.inDegree(1));
        assertEquals(1, g.inDegree(2));
        assertEquals(2, g.inDegree(3));
        int item = 1;
        for (int i:g.predecessors(3)) {
            assertEquals(item, i);
            item += 1;
        }

        g = new DirectedGraph();
        for (int i = 0; i < 10; i++) {
            g.add();
        }
        g.add(2, 5);
        g.add(2, 3);
        g.add(2, 6);
        g.add(3, 7);
        g.add(3, 8);
        g.add(8, 1);
        g.add(8, 9);
        g.add(1, 1);
        g.add(8, 8);
        g.add(2, 2);
        g.add(1, 2);
        g.add(1, 3);
        g.add(1, 4);
        g.add(8, 10);
        g.add(10, 7);
    }

    @Test
    public void testMax() {
        DirectedGraph g = new DirectedGraph();
        g.add();
        g.add();
        g.add();
        g.add(1, 2);
        g.add(2, 3);
        assertEquals(3, g.maxVertex());
    }

    @Test
    public void testAdd() {
        DirectedGraph g = new DirectedGraph();
        g.add();
        g.add();
        g.add();
        g.add(1, 2);
        g.add(2, 3);
        assertEquals(3, g.vertexSize());
        assertEquals(2, g.edgeSize());
    }

    @Test
    public void testContains() {
        DirectedGraph g = new DirectedGraph();
        g.add();
        g.add();
        g.add();
        g.add(1, 2);
        g.add(2, 3);
        assertTrue(g.contains(1));
        assertTrue(g.contains(2));
        assertTrue(g.contains(3));
        assertTrue(g.contains(1, 2));
        assertTrue(g.contains(2, 3));

        g.add(1, 3);
        assertTrue(g.contains(1, 3));
    }

    @Test
    public void testSuccessor() {
        DirectedGraph g = new DirectedGraph();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add(1, 2);
        g.add(1, 3);
        g.add(1, 4);
        assertEquals(4, g.successor(1, 2));
        assertEquals(3, g.successor(1, 1));
        assertEquals(2, g.successor(1, 0));
    }

    @Test
    public void testSuccessors() {
        DirectedGraph g = new DirectedGraph();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add(1, 2);
        g.add(1, 3);
        g.add(1, 4);
        int value = 2;
        for (int i: g.successors(1)) {
            assertEquals(i, value);
            value += 1;
        }
    }


}
