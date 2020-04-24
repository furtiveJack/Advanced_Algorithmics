package fr.umlv.info2.graphs;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.LongAdder;

public class Graphs {
    /**
     * Recursive method used to compute the DFS from a starting vertex given
     * @param g         the graph on which we are running the DFS
     * @param v0        the starting vertex from which in should start to explore
     * @param visited   an array indicating for each vertex if it has already been visited or not
     * @param res       the resulting list constructed by applying the DFS algorithm (modified by this method)
     */

    private static void DFS_forVertex(Graph g, int v0, boolean[] visited, List<Integer> res) {
        Stack<Integer> toBeTreated = new Stack<>();
        toBeTreated.push(v0);
        while (! toBeTreated.empty()) {
            var current = toBeTreated.pop();
            if (! visited[current]) {
                res.add(current);
                visited[current] = true;
            }
            g.forEachEdge(current, edge -> {
                if (! visited[edge.getEnd()]) {
                    toBeTreated.push(edge.getEnd());
                }
            });
        }
    }
    /**
     * Recursive method used to compute the partial BFS from a starting vertex given
     * @param g         the graph on which we are running the BFS
     * @param v0        the starting vertex from which in should start to explore
     * @param visited   an array indicating for each vertex if it has already been visited or not
     * @param res       the resulting list constructed by applying the BFS algorithm (modified by this method)
     */
    private static void BFS_forVertex(Graph g, int v0, boolean[] visited, List<Integer> res) {
        var toBeTreated = new ArrayDeque<Integer>(g.numberOfVertices());
        toBeTreated.add(v0);
        while (! toBeTreated.isEmpty()) {
            var current = toBeTreated.poll();
            if (! visited[current]) {
                res.add(current);
                visited[current] = true;
            }
            g.forEachEdge(current, edge -> {
                if (!visited[edge.getEnd()]) {
                    toBeTreated.add(edge.getEnd());
                }
            });
        }
    }
    /**
     * This method computes a full DFS on the graph provided, starting from vertex v0.
     * @param g     the graph on which the full time DFS algorithm should be applied.
     * @param v0    the vertex on witch the DFS should be started.
     * @return      a list containing the vertices in the order found thanks to DFS algorithm.
     */
    public static List<Integer> DFS(Graph g, int v0) {
        List<Integer> res = new LinkedList<>();
        boolean[] visited = new boolean[g.numberOfVertices()];
        for (int i = v0 ; i < g.numberOfVertices() ; ++i) {
            if (! visited[i]) {
                DFS_forVertex(g, i, visited, res);
            }
        }
        for (int i = 0 ; i < v0 ; ++i) {
            if (! visited[i]) {
                DFS_forVertex(g, i, visited, res);
            }
        }
        return res;
    }
    /**
     * This method computes a full BFS on the graph provided, starting from vertex v0.
     * @param g     the graph on which the full time BFS algorithm should be applied.
     * @param v0    the vertex on witch the BFS should be started.
     * @return      a list containing the vertices in the order found thanks to BFS algorithm.
     */
    public static List<Integer> BFS(Graph g, int v0) {
        List<Integer> res = new LinkedList<>();
        boolean[] visited = new boolean[g.numberOfVertices()];
        for (int i = v0 ; i < g.numberOfVertices() ; ++i) {
            if (! visited[i]) {
                BFS_forVertex(g, i, visited, res);
            }
        }
        for (int i = 0 ; i < v0 ; ++i) {
            if (! visited[i]) {
                BFS_forVertex(g, i, visited, res);
            }
        }
        return res;
    }

    private static void timedDFS_rec(Graph g, int v0, int[] start, int[] end, LongAdder count) {
        start[v0] = count.intValue();
        count.increment();
        g.forEachEdge(v0, e -> {
            int t = e.getEnd();
            if (start[t] == -1) {
                timedDFS_rec(g, t, start, end, count);
            }
        });
        end[v0] = count.intValue();
        count.increment();
    }

    public static int[][] timedDepthFirstSearch(Graph g, int v0) {
        int n = g.numberOfVertices();
        if (v0 > n || v0 < 0) {
            throw new IllegalArgumentException("Incorrect value for starting vertex");
        }
        LongAdder count = new LongAdder();
        int[] start = new int[n];
        int[] end = new int[n];
        for (int i = 0 ; i < n ; ++i) {
            start[i] = -1;
            end[i] = -1;
        }
        for (int v = v0 ; v < n ; ++v) {
            if (start[v] == -1) {
                timedDFS_rec(g, v, start, end, count);
            }
        }
        for (int v = 0 ; v < v0 ; ++v) {
            if (start[v] == -1) {
                timedDFS_rec(g, v, start, end, count);
            }
        }
        int[][] timings = new int[n][2];
        for (int i = 0 ; i < n ; ++i) {
            timings[i][0] = start[i];
            timings[i][1] = end[i];
        }
        return timings;
    }

    private static void topologicalSort_rec(Graph g, int v0, boolean[] visited, LinkedList<Integer> list,
                                            boolean cycleDetect, boolean[] finished) {
        visited[v0] = true;
        g.forEachEdge(v0, e -> {
            var t = e.getEnd();
            if (! visited[t]) {
                topologicalSort_rec(g, t, visited, list, cycleDetect, finished);
            }
            else {
                if (! finished[t]) {
                    if (cycleDetect) {
                        throw new IllegalStateException("Graph contains cycle from " + v0 + " to " + t);
                    } else {
                        System.out.println("Detected a cycle in the graph, but user asked to ignore cycles");
                    }
                }
            }
        });
        finished[v0] = true;
        list.push(v0);
    }

    /**
     * Compute a topological sort for the grpah given.
     * @param g : a valid graph
     * @param cycleDetect : if set to True, cycles will be treat as errors, otherwise they will be ignore.
     * @return a list representing the topological sort of this graph
     */
    public static List<Integer> topologicalSort(Graph g, boolean cycleDetect) {
        int n = g.numberOfVertices();
        var visited = new boolean[n];
        var finished = new boolean[n];
        var list = new LinkedList<Integer>();
        for (int v = 0 ; v < n ; ++v) {
            if (! visited[v]) {
                topologicalSort_rec(g, v, visited, list, cycleDetect, finished);
            }
        }
        return list;
    }


    /**
     * Do a timedDFS on the graph from the vertex given, but instead of storing the beginning/end time for each vertice,
     * we push the vertices into a stack by decreasing end date order.
     * @param g : graph to work on
     * @param v0 : current index
     * @param visited : an array to know which vertex has already been visited
     * @param stack : the stack that store the vertices by decreasing end time order
     */
    private static void fillStackByDecreasingOrder(Graph g, int v0, boolean[] visited, Stack<Integer> stack) {
        visited[v0] = true;
        g.forEachEdge(v0, e -> {
            var t = e.getEnd();
            if (! visited[t]) {
                fillStackByDecreasingOrder(g, t, visited, stack);
            }
        });
        stack.push(v0);
    }

    /**
     * Compute the strongly connected components of the graph g.
     * @param g : a valid graph
     * @return a list of the connected components of the graph
     */
    public static List<List<Integer>> scc(Graph g) {
        var n = g.numberOfVertices();
        var visited = new boolean[n];
        var decreasingEnds = new Stack<Integer>();
        for (int i = 0 ; i < n ; ++i) {
            fillStackByDecreasingOrder(g, i, visited, decreasingEnds);
        }
        var transposed = g.transpose();
        visited = new boolean[n];
        List<List<Integer>> result = new ArrayList<>();
        while (! decreasingEnds.empty()) {
            var v = decreasingEnds.pop();
            if (! visited[v]) {
                List<Integer> tmp = new ArrayList<>();
                DFS_forVertex(transposed, v, visited, tmp);
                result.add(tmp);
            }
        }
        return result;
    }

    public static void main(String[] args) throws IOException {
        var mat = Graph.loadGraph("src/matrix.mat", "list");
        var scc = scc(mat);
        scc.forEach(System.out::println);




//        var mat = Graph.loadGraph("src/matrix.mat", "list");
//        var topoSort = topologicalSort(mat, true);
//        System.out.println(topoSort);
//        var timings = timedDepthFirstSearch(mat, 0);
//        for (var a : timings) {
//            System.out.println(Arrays.toString(a));
//        }
//        System.out.println(mat.toGraphviz());
//        System.out.println(BFS(mat, 0));
//        System.out.println(DFS(mat, 0));
//        Graph.toDotFile(mat, "matrix.dot");
//        var g = Graph.createRandomGraph(5, 7);
//        System.out.println(g.toGraphviz());
//        Graph.getImageView(g, "graph.dot");
    }
}
