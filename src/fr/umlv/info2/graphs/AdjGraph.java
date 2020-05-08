package fr.umlv.info2.graphs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.Consumer;

public class AdjGraph implements Graph {
    private final ArrayList<LinkedList<Edge>> adj;
    private final int n; // number of vertices
    private int nbEdges;

    public AdjGraph(int nbVertices) {
        if (nbVertices <= 0) {
            throw new IllegalArgumentException("Vertices number should be positive");
        }
        n = nbVertices;
        adj = new ArrayList<>(n);
        for (int i = 0 ; i < n ; ++i) {
            adj.add(new LinkedList<>());
        }
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= n) {
            throw new IndexOutOfBoundsException("Index must be >= 0 and <= " + n);
        }
    }

    @Override
    public int numberOfEdges() {
        return nbEdges;
    }

    @Override
    public int numberOfVertices() {
        return n;
    }

    @Override
    public void addEdge(int i, int j, int value) {
        checkIndex(i);
        checkIndex(j);
        var edge = new Edge(i, j, value);
        if (adj.get(i).contains(edge)) {
            return;
        }
        adj.get(i).add(edge);
        nbEdges++;
    }

    @Override
    public boolean isEdge(int i, int j) {
        checkIndex(i);
        checkIndex(j);
        var edges = adj.get(i);
        for (Edge e : edges) {
            if (e.getEnd() == j) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getWeight(int i, int j) {
        checkIndex(i);
        checkIndex(j);
        var edges = adj.get(i);
        for (Edge e : edges) {
            if (e.getEnd() == j) {
                return e.getValue();
            }
        }
        return 0;
    }

    @Override
    public Iterator<Edge> edgeIterator(int i) {
        checkIndex(i);
        return adj.get(i).iterator();
    }

    @Override
    public void forEachEdge(int i, Consumer<Edge> consumer) {
        checkIndex(i);
        var it = edgeIterator(i);
        while (it.hasNext()) {
            consumer.accept(it.next());
        }
    }

    @Override
    public String toGraphviz() {
        StringBuilder builder = new StringBuilder();
        builder.append("digraph G {\n");
        for (int i = 0 ; i < n ; ++i) {
            builder.append("\t").append(i).append(";\n");
            forEachEdge(i, e -> builder.append("\t")
                    .append(e.getStart())
                    .append(" -> ")
                    .append(e.getEnd())
                    .append(" [ label=\"")
                    .append(e.getValue())
                    .append("\" ] ;\n"));
        }
        builder.append("}\n");
        return builder.toString();
    }
}
