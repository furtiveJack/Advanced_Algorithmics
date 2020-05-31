package fr.umlv.info2.graphs.main;

import fr.umlv.info2.graphs.Graph;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class GraphTest {

    @Test
    void shouldCreateARandomGraph() {
        Graph g = Graph.createRandomGraph(5, 8);
        assertEquals(5, g.numberOfVertices());
        assertEquals(8, g.numberOfEdges());
    }

    @Test
    void shouldThrowIAEOnRandomGraphCreation() {
        assertThrows(IllegalArgumentException.class, () -> Graph.createRandomGraph(2, 8));
        assertThrows(IllegalArgumentException.class, () -> Graph.createRandomGraph(0, 2));
    }

    @Test
    void shouldTransposeAGraph() {
        var g = Graph.createRandomGraph(5, 8);
        var t = g.transpose();
        assertEquals(g.numberOfEdges(), t.numberOfEdges());
        assertEquals(g.numberOfVertices(), t.numberOfVertices());
        for (int i = 0 ; i < g.numberOfVertices() ; ++i) {
            g.forEachEdge(i, e -> {
                assertTrue(t.isEdge(e.getEnd(), e.getStart()));
            });
        }
    }

    @Test
    void shouldLoadAGraph() throws IOException {
        var g = Graph.loadGraph("data/4vertices_shortest1.mat", "matrix");
        assertEquals(4, g.numberOfVertices());
        assertEquals(6, g.numberOfEdges());

        assertFalse(g.isEdge(0, 0));
        assertTrue(g.isEdge(0, 1));
        assertFalse(g.isEdge(0, 2));
        assertTrue(g.isEdge(0, 3));

        assertFalse(g.isEdge(1, 0));
        assertFalse(g.isEdge(1, 1));
        assertTrue(g.isEdge(1, 2));
        assertFalse(g.isEdge(1, 3));

        assertTrue(g.isEdge(2, 0));
        assertFalse(g.isEdge(2, 1));
        assertFalse(g.isEdge(2, 2));
        assertFalse(g.isEdge(2, 3));

        assertFalse(g.isEdge(3, 0));
        assertTrue(g.isEdge(3, 1));
        assertTrue(g.isEdge(3, 2));
        assertFalse(g.isEdge(3, 3));
    }

    @Test
    void shouldThrowExceptionWhenIncorrectFile() {
        assertThrows(IOException.class, () -> Graph.loadGraph("no_file_here", "matrix"));
        assertThrows(IllegalArgumentException.class, () -> Graph.loadGraph("data/incorrect_file.mat", "incorrect"));
        assertThrows(IllegalArgumentException.class, () -> Graph.loadGraph("data/incorrect_file.mat", "matrix"));
    }
}
