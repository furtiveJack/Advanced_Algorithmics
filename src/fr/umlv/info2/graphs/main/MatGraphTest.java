package fr.umlv.info2.graphs.main;

import fr.umlv.info2.graphs.MatGraph;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MatGraphTest {

    @Order(1)
    @org.junit.jupiter.api.Test
    void numberOfVertices() {
        MatGraph matGraph = new MatGraph(5);
        assertEquals(5, matGraph.numberOfVertices(), "The number of Vertices needs to be the same as set in the constructor !");
    }

    @Order(2)
    @org.junit.jupiter.api.Test
    void addEdge() throws NoSuchFieldException, IllegalAccessException {
        VarHandle vh = MethodHandles.privateLookupIn(MatGraph.class, MethodHandles.lookup()).findVarHandle(MatGraph.class, "mat", int[][].class);

        MatGraph matGraph = new MatGraph(10);
        matGraph.addEdge(0, 9, 1);

        assertEquals(1, ((int[][])vh.get(matGraph))[0][9]);
    }

    @Order(3)
    @org.junit.jupiter.api.Test
    void numberOfEdgesSelfLoop() {
        MatGraph matGraph = new MatGraph(10);

        for ( var i = 0 ; i < 10 ; i++ ) {
            matGraph.addEdge(i,i, 1);
        }
        assertEquals(10, matGraph.numberOfEdges());
    }

    @Order(4)
    @org.junit.jupiter.api.Test
    void numberOfEdgesNeighbor() {
        MatGraph matGraph = new MatGraph(10);

        for ( var i = 0 ; i < 10 ; i++ ) {
            matGraph.addEdge(i,0, 1);
        }

        assertEquals(10, matGraph.numberOfEdges());
    }

    @Order(5)
    @org.junit.jupiter.api.Test
    void numberOfEdgesAll() {
        MatGraph matGraph = new MatGraph(10);

        for ( var i = 0 ; i < 10 ; i++ ) {
            for ( var j = 0 ; j < 10 ; j++ ) {
                matGraph.addEdge(i,j, 1);
            }
        }
        assertEquals(100, matGraph.numberOfEdges());
    }

    @Order(6)
    @org.junit.jupiter.api.Test
    void isEdge() {
        MatGraph matGraph = new MatGraph(10);

        matGraph.addEdge(3, 3, 1);
        assertTrue(matGraph.isEdge(3, 3));
        matGraph.addEdge(3, 3, 0);
        assertFalse(matGraph.isEdge(3, 3));
    }

    @org.junit.jupiter.api.Test
    void getWeight() {
        MatGraph matGraph = new MatGraph(10);

        matGraph.addEdge(3, 3, 1);
        assertEquals(1, matGraph.getWeight(3, 3));
        matGraph.addEdge(3, 3, 0);
        assertEquals(0, matGraph.getWeight(3, 3));
        matGraph.addEdge(1, 1, 12);
        assertEquals(12, matGraph.getWeight(1, 1));
    }

    @org.junit.jupiter.api.Test
    void edgeIterator() {
        MatGraph matGraph = new MatGraph(6);
        matGraph.addEdge(1, 2, 222);
        matGraph.addEdge(1, 5, 555);

        var iterator = matGraph.edgeIterator(1);
        assertEquals(222, iterator.next().getValue());
        assertEquals(555, iterator.next().getValue());
    }

    @org.junit.jupiter.api.Test
    void forEachEdge() {
        MatGraph mat = new MatGraph(10);

    }

    @org.junit.jupiter.api.Test
    void toGraphviz() {
    }

    @org.junit.jupiter.api.Test
    void graphIterator() {
    }
}