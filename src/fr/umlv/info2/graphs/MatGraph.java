package fr.umlv.info2.graphs;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;

public class MatGraph implements  Graph {
    private final int[][] mat;
    private final int n; // number of vertices
    private int nbEdges;

    public MatGraph(int nbVertices) {
        if (nbVertices <= 0) {
            throw new IllegalArgumentException("Incorrect number of vertices (should be positive)");
        }
        this.n = nbVertices;
        this.mat = new int[n][n];
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= n) {
            throw new IndexOutOfBoundsException("Index " + index + " must be >= 0 and <= " + n);
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
        mat[i][j] = value;
        nbEdges++;
    }

    @Override
    public boolean isEdge(int i, int j) {
        checkIndex(i);
        checkIndex(j);
        return mat[i][j] != 0;
    }

    @Override
    public int getWeight(int i, int j) {
        checkIndex(i);
        checkIndex(j);
        return mat[i][j];
    }

    @Override
    public Iterator<Edge> edgeIterator(int i) {
        checkIndex(i);
        return new Iterator<>() {
            private int j = 0;

            @Override
            public boolean hasNext() {
                for (var index = j ; index < n ; ++index) {
                    if (mat[i][index] != 0) {
                        j = index;
                        return true;
                    }
                }
                return false;
            }

            @Override
            public Edge next() {
                if (! hasNext()) {
                    throw new NoSuchElementException();
                }
                var index = j;
                j++;
                return new Edge(i, index, mat[i][index]);
            }
        };
    }

    @Override
    public void forEachEdge(int i, Consumer<Edge> consumer) {
        Objects.requireNonNull(consumer);
        checkIndex(i);
        var it = edgeIterator(i);
        while (it.hasNext()) {
            consumer.accept(it.next());
        }
    }

    @Override
    public String toGraphviz() {
        StringBuilder str = new StringBuilder();
        str.append("digraph G {\n");
        for (int i = 0 ; i < n ; ++i) {
            str.append("\t").append(i).append(";\n");
            forEachEdge(i, e -> str.append("\t")
                    .append(e.getStart())
                    .append(" -> ")
                    .append(e.getEnd())
                    .append(" [ label=\"")
                    .append(e.getValue())
                    .append("\" ] ;\n"));
        }
        str.append("}\n");
        return str.toString();
    }

    public static void main(String[] args) {
        var mat = new MatGraph(4);
        mat.addEdge(0, 1, 2);
        mat.addEdge(1, 3, 1);
        mat.addEdge(1, 1, 3);
        System.out.println(mat.toGraphviz());
    }
}
