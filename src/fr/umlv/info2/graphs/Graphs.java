package fr.umlv.info2.graphs;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Consumer;

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
     * Compute a topological sort for the graph given.
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
     * Do a timedDFS on the graph from the vertex given, but instead of storing the beginning/end time for each vertices,
     * we push the vertices into a stack by decreasing end date order.
     * This method is called for each vertex of the graph in order to compute the final stack.
     * @param g : a valid graph
     * @param v0 : current index
     * @param visited : an array to know which vertex has already been visited
     * @param stack : the stack that will store the vertices by decreasing end time order
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

    /**
     * Compute the shortest path from the starting vertex v0 to any other vertex of the graph, using
     * the Bellman-Ford algorithm. This method detects if the graph contains negatives cycles.
     * @param g : a valid graph
     * @param v0 : starting vertex of the graph
     * @return an ShortestPathFromOneVertex object storing  an array of distances and an array of predecessors.
     * @throws IllegalStateException if the graph contains negative cycles.
     * @throws IndexOutOfBoundsException if the given starting vertex is < 0 or >= g.numberOfVertices().
     */
    public static ShortestPathFromOneVertex bellmanFord(Graph g, int v0) {
        int V = g.numberOfVertices();
        if (v0 > V) {
            throw new IndexOutOfBoundsException("Index of starting vertex is out of bounds");
        }
        int[] d = new int[V];
        int[] p = new int[V];
        // Initialization
        for (int i = 0; i < V; ++i) {
            d[i] = Integer.MAX_VALUE;
            p[i] = Integer.MIN_VALUE;
        }
        d[v0] = 0;
        p[v0] = v0;
        // Main loop
        for (int i = 1; i < V; ++i) {
            for (int j = 0; j < V ; ++j) {
                g.forEachEdge(j, edge -> {
                    int s = edge.getStart();
                    int t = edge.getEnd();
                    int w = edge.getValue();
                    if (d[s] != Integer.MAX_VALUE && (d[s] + w) < d[t]) {
                        d[t] = d[s] + w;
                        p[t] = s;
                    }
                });
            }
        }
        // check for negative cycles
        for (int i = 0 ; i < V ; ++i) {
            g.forEachEdge(i, edge -> {
                int s = edge.getStart();
                int t = edge.getEnd();
                int w = edge.getValue();
                if (d[s] != Integer.MAX_VALUE && d[t] > d[s] + w) {
                    throw new IllegalStateException("Graph contains a negative cycle");
                }
            });
        }
        return new ShortestPathFromOneVertex(v0, d, p);
    }

    /**
     * Extract from todo the next vertex that minimizes the array d
     * @param d : an array containing the distances from a starting vertex
     * @param todo : an array telling which vertex can still be extracted
     * @return the index of the vertex that minimizes the distance
     * @throws IllegalStateException if there is no more vertex to extract in the todo array.
     */
    private static int extractMinDistanceVertex(int[] d, boolean[] todo) {
        int n = d.length;
        int minDistance = Integer.MAX_VALUE;
        int min = -1; // index of vertex that minimizes d
        for (int i = 0 ; i < n ; ++i) {
            if (todo[i] && d[i] < minDistance) {
                min = i;
                minDistance = d[i];
            }
        }
        if (min == -1) {
            throw new IllegalStateException("No more vertex to extract");
        }
        todo[min] = false;
        return min;
    }

    /**
     * Compute the shortest path from the starting vertex v0 to any other vertex of the graph, using
     * the Dijkstra algorithm.
     * @param g : a valid graph
     * @param v0 : starting vertex of the graph
     * @return an ShortestPathFromOneVertex object storing  an array of distances and an array of predecessors.
     * @throws IndexOutOfBoundsException if the given starting vertex is < 0 or >= g.numberOfVertices()
     */
    public static ShortestPathFromOneVertex dijkstra(Graph g, int v0) {
        int V = g.numberOfVertices();
        if (v0 > V) {
            throw new IndexOutOfBoundsException("Index of starting vertex is out of bounds");
        }
        int[] d = new int[V];
        int[] p = new int[V];
        boolean[] todo = new boolean[V];
        for (var i = 0 ; i < V ; ++i) {
            d[i] = Integer.MAX_VALUE;
            p[i] = Integer.MIN_VALUE;
            todo[i] = true;
        }
        d[v0] = 0;
        p[v0] = v0;
        while (true) {
            int s;
            try {
                s = extractMinDistanceVertex(d, todo);
            } catch (IllegalStateException e) {
                break; // no more vertex to extract
            }
            g.forEachEdge(s, edge -> {
                int t = edge.getEnd();
                int w = edge.getValue();
                if (d[s] != Integer.MAX_VALUE && d[s] + w < d[t]) {
                    d[t] = d[s] + w;
                    p[t] = s;
                }
            });
        }
        return new ShortestPathFromOneVertex(v0, d, p);
    }

    private static void print2dArrays(int[][] d, int[][] p) {
        StringBuffer bf = new StringBuffer();
        for (int i = 0; i < d.length; i++) {
            bf.append(Arrays.toString(d[i])).append("\t\t\t\t").append(Arrays.toString(p[i])).append("\n");
        }
        System.out.println(bf.toString());
    }
    public static ShortestPathFromAllVertices floydWarshall(Graph g) {
        Objects.requireNonNull(g);
        int V = g.numberOfVertices();
        int[][] d = new int[V][V];
        int[][] p = new int[V][V];
        // Initialization
        for (int s = 0 ; s < V ; ++s) {
            for (int t = 0 ; t < V ; ++t) {
                if (s == t) {
                    d[s][t] = 0;
                    p[s][t] = s;
                }
                else {
                    if (g.isEdge(s, t)) {
                        d[s][t] = g.getWeight(s, t);
                        p[s][t] = s;
                    }
                    else {
                        d[s][t] = Integer.MAX_VALUE;
                        p[s][t] = Integer.MIN_VALUE;
                    }
                }
            }
        }
//        print2dArrays(d, p);
        // Main loop
        for (int k = 0 ; k < V ; ++k) {
//            System.out.println(" k:" + k);
//            print2dArrays(d, p);
            for (int s = 0 ; s < V ; ++s) {
                for (int t = 0 ; t < V ; ++t) {
                    if (s != k && t != k && d[s][k] != Integer.MAX_VALUE && d[k][t] != Integer.MAX_VALUE) {
                        if (d[s][t] > d[s][k] + d[k][t]) {
                            d[s][t] = d[s][k] + d[k][t];
                            p[s][t] = p[k][t];
                        }
                    }
                }
            }
        }
        return new ShortestPathFromAllVertices(d, p);
    }


    public static void main(String[] args) throws IOException {
        var mat = Graph.loadGraph("data/5vertices_shortest.mat", "list");
        System.out.println(mat.toGraphviz());

        System.out.println("Bellman");
        var shortestB = bellmanFord(mat, 0);
        System.out.println(shortestB);
        shortestB.printShortestPath(2);

        System.out.println("Dijkstra");
        var shortestD = dijkstra(mat, 0);
        System.out.println(shortestD);
        shortestD.printShortestPath(2);

        System.out.println("----------");
        for (int i = 0 ; i < mat.numberOfVertices() ; ++i) {
            System.out.println(dijkstra(mat, i));
            System.out.println(bellmanFord(mat, i));
        }
        System.out.println("FloydWarshall");
        var shortestF = floydWarshall(mat);
        System.out.println(shortestF);
//        shortestF.printShortestPath(0,2);

//        var timings = timedDepthFirstSearch(mat, 0);
//        var topoSort = topologicalSort(mat, true);
//        System.out.println(topoSort);
//        for (var a : timings) {
//            System.out.println(Arrays.toString(a));
//        }



//        var mat = Graph.loadGraph("src/matrix.mat", "list");
//        var scc = scc(mat);
//        scc.forEach(System.out::println);




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
