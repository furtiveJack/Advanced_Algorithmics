package fr.umlv.info2.graphs.main;
import fr.umlv.info2.graphs.Graph;
import fr.umlv.info2.graphs.MatGraph;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GraphTest {

    @Order(1)
    @org.junit.jupiter.api.Test
    void shouldCreateARandomGraph() {
        Graph g = Graph.createRandomGraph(5, 8);
        assertEquals(5, g.numberOfVertices());
        assertEquals(8, g.numberOfEdges());
    }

    @Order(2)
    @org.junit.jupiter.api.Test
    void shouldThrowIAEOnRandomGraphCreation() {
        assertThrows(IllegalArgumentException.class, () -> Graph.createRandomGraph(2, 8));
        assertThrows(IllegalArgumentException.class, () -> Graph.createRandomGraph(0, 2));
    }

    @Order(2)
    @org.junit.jupiter.api.Test
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
}
