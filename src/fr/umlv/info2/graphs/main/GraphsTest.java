package fr.umlv.info2.graphs.main;

import fr.umlv.info2.graphs.Graph;
import fr.umlv.info2.graphs.Graphs;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GraphsTest {

    @Test
    public void shouldComputeDFS() throws IOException {
        var g = Graph.loadGraph("data/7vertices_traversal.mat", "matrix");
        var res = Graphs.DFS(g, 0);
        assertEquals(res, List.of(0, 2, 3, 1, 4, 5, 6));
    }

    @Test
    public void shouldComputeBFS() throws IOException {
        var g = Graph.loadGraph("data/7vertices_traversal.mat", "matrix");
        var res = Graphs.BFS(g, 0);
        assertEquals(res, List.of(0, 1, 2, 3, 4, 5, 6));
    }

    @Test
    public void shouldComputeTimedDFS() throws IOException {
        var g = Graph.loadGraph("data/7vertices_traversal.mat", "matrix");
        var timings = Graphs.timedDepthFirstSearch(g, 0);
        var expected = new int[timings.length][2];
        expected[0][0] = 0; expected[0][1] = 7;
        expected[1][0] = 1; expected[1][1] = 6;
        expected[2][0] = 2; expected[2][1] = 5;
        expected[3][0] = 3; expected[3][1] = 4;
        expected[4][0] = 8; expected[4][1] = 11;
        expected[5][0] = 9; expected[5][1] = 10;
        expected[6][0] = 12; expected[6][1] = 13;
        for (int i = 0 ; i < timings.length ; ++i) {
            assertTrue(Arrays.equals(timings[i], expected[i]));
        }
    }

    @Test
    public void shouldComputeTopologicalSort() throws IOException {
        var g = Graph.loadGraph("data/14edges_acyclic.mat", "matrix");
        var topo = Graphs.topologicalSort(g, true);
        assertEquals(topo, List.of(3, 1, 2, 6, 0, 5, 12, 9, 11, 10, 13, 8, 4, 7));
        g = Graph.loadGraph("data/6edges_acyclic.mat", "matrix");
        topo = Graphs.topologicalSort(g, true);
        assertEquals(topo, List.of(0, 2, 3, 5, 4, 1));
    }

    @Test
    public void shouldThrowErrorIfCycles() throws IOException {
        var g = Graph.loadGraph("data/6edges_cyclic.mat", "matrix");
        assertThrows(IllegalStateException.class, () -> Graphs.topologicalSort(g, true));
    }

    @Test
    public void shouldIgnoreErrorIfCycles() throws IOException {
        var g = Graph.loadGraph("data/6edges_cyclic.mat", "matrix");
        assertDoesNotThrow(() -> Graphs.topologicalSort(g, false));
    }

    @Test
    public void shouldComputeSCC() throws IOException {
        var g = Graph.loadGraph("data/9vertices_scc.mat", "matrix");
        var scc = Graphs.scc(g);
        scc.forEach(System.out::println);
        assertEquals(scc.size(), 3);
        assertEquals(scc.get(0), List.of(8, 7, 3));
        assertEquals(scc.get(1), List.of(6, 4, 5, 2, 0));
        assertEquals(scc.get(2), List.of(1));
    }

    @Test
    public void shouldComputeBellmanFord() throws IOException {
        var g = Graph.loadGraph("data/4vertices_shortest1.mat", "matrix");
        var shortestB = Graphs.bellmanFord(g, 0);
        assertEquals(shortestB.printShortestPath(2), List.of(0, 3, 1, 2));
        var expectedD = new ArrayList<Integer>(shortestB.getD().length);
        for (int i : shortestB.getD()) {
            expectedD.add(i);
        }
        var expectedPi = new ArrayList<Integer>(shortestB.getPi().length);
        for (int i : shortestB.getPi()) {
            expectedPi.add(i);
        }
        assertEquals(expectedD, List.of(0, 3, 4, 1));
        assertEquals(expectedPi, List.of(0, 3, 1, 0));
    }

    @Test
    public void shouldThrowErrorIfNegativeCycleBellmanFord() throws IOException {
        var g = Graph.loadGraph("data/6vertices_shortest2.mat", "matrix");
        assertThrows(IllegalStateException.class, () -> Graphs.bellmanFord(g, 0));
    }

    @Test
    public void shouldComputeDijkstra() throws IOException {
        var g = Graph.loadGraph("data/5vertices_dijkstra.mat", "matrix");
        var shortest = Graphs.dijkstra(g, 0);
        assertEquals(shortest.printShortestPath(2), List.of(0, 1, 2));
        var expectedD = new ArrayList<Integer>(shortest.getD().length);
        for (int i : shortest.getD()) {
            expectedD.add(i);
        }
        var expectedPi = new ArrayList<Integer>(shortest.getPi().length);
        for (int i : shortest.getPi()) {
            expectedPi.add(i);
        }
        assertEquals(expectedD, List.of(0, 2, 4, 3, 6));
        assertEquals(expectedPi, List.of(0, 0, 1, 1, 2));
    }

    @Test
    public void shouldComputeFloydWarshall() throws IOException {
        var g = Graph.loadGraph("data/8vertices_shortest.mat", "list");
        var shortest = Graphs.floydWarshall(g);
        assertEquals(List.of(4, 2, 5, 7), shortest.printShortestPath(4, 7));
    }
}
