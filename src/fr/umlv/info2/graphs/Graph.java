package fr.umlv.info2.graphs;


import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.IntSupplier;

public interface Graph {
    int numberOfEdges();
    int numberOfVertices();
    void addEdge(int i, int j, int value);
    boolean isEdge(int i, int j);
    int getWeight(int i, int j);

    Iterator<Edge> edgeIterator(int i);
    void forEachEdge(int i, Consumer<Edge> consumer);
    String toGraphviz();

    /**
     * Load the graph stored in the file whose path is given in argument.
     * This file should provide a matrix representation of the graph wanted, and needs to be well formatted, which is :
     *      - first line must contain the number of vertices _n_
     *      - then all the lines should contain exactly _n_ integers separated from the others by a space
     *          These integers represent the weight on the edges of the graph
     *  If the format is not respected, an IllegalArgumentException will be thrown
     *  This method returns an object implementing the interface Graph. The type of this object can be MatGraph
     *  if the 'type' argument provided to the method is 'matrix', otherwise if the type is 'list' ht method will return
     *  an AdjGraph.
     *  An IllegalArgumentException will be thrown if the type is not one of these values.
     *
     * @param path path of file to parse
     * @param type Type of graph representation wanted in return ('matrix' for matrix representation and 'list' for
     *             adjacency list representation)
     * @return an object representing a graph (either a MatGraph or a AdjGraph)
     * @throws IOException if I/O error occurs while reading the file
     */
    static Graph loadGraph(String path, String type) throws IOException {
        Path p = Path.of(path);
        Graph g;
        try (var reader = new BufferedReader(new FileReader(p.toFile()))) {
            String line = reader.readLine();
            int n = Integer.parseInt(line);
            if (type.equals("matrix")) {
                g = new MatGraph(n);
            }
            else if (type.equals("list")) {
                g = new AdjGraph(n);
            }
            else {
                throw new IllegalArgumentException("Graph type should be matrix or list");
            }
            int i = 0;
            line = reader.readLine();
            while (line != null) {
                var values = line.split(" ");
                if (values.length != n) {
                    throw new IllegalArgumentException("File is not well formatted");
                }
                for (int j = 0 ; j < n ; ++j) {
                    int weight = Integer.parseInt(values[j]);
                    if (weight != 0) {
                        g.addEdge(i, j, weight);
                    }
                }
                ++i;
                line = reader.readLine();
            }
        }
        return g;
    }

    /**
     * This method uses graphviz to generate a .png image representing the graph.
     * To do so, it writes the dot representation of the graph (gotten using Graph.toGraphviz()) into a .dot file,
     * and then uses a dot shell command to convert this file to a .png image.
     * The method then opens the image generated, and blocks until the file is closed by user.
     * @param g The graph for which an image view is wanted
     * @param fileName The file to use for writing (should end with a .dot suffix);
     * @throws IOException
     */
    static void getImageView(Graph g, String fileName) throws IOException {
        Objects.requireNonNull(g);
        Objects.requireNonNull(fileName);
        if (! fileName.contains(".dot")) {
            throw new IllegalArgumentException("File name should have a .dot suffix");
        }
        // Writing the graph dot representation to a .dot file
        try (var fc = FileChannel.open(Path.of(fileName), StandardOpenOption.CREATE, StandardOpenOption.WRITE
                                    , StandardOpenOption.TRUNCATE_EXISTING)) {
            fc.write(StandardCharsets.UTF_8.encode(g.toGraphviz()));
            System.out.println("Wrote graph to file " + fileName);
        }
        // Using dot command to convert .dot file to an .png image
        var graphImg = (fileName.split(".dot"))[0] + ".png";
        var cmd = new ProcessBuilder();
        cmd.command("dot", fileName, "-Tpng", "-o", graphImg);
        try {
            var process = cmd.start();
            var rc = process.waitFor();
            if (rc == 0) {
                System.out.println("Successfully converted graph.dot to graph.png ");
            }
        } catch (InterruptedException e) {
            throw new AssertionError("Error while running the dot command");
        }
        // Opening the .png image generated by graphviz
        cmd.command("eog", graphImg);
        try {
            System.out.println("Try to open image...");
            var process = cmd.start();
            var rc = process.waitFor();
            if (rc == 0) {
                System.out.println("Successfully opened " + graphImg);
            }
        } catch (InterruptedException e) {
            throw new AssertionError("Error while opening the image");
        }
    }

    /**
     * Create a random graph the given number of vertices and edges
     * @param n         number of vertices
     * @param nbEdges   number of edges
     * @return          the random graph created
     */
    static Graph createRandomGraph(int n, int nbEdges) {
        if (nbEdges > n * n) {
            throw new IllegalArgumentException("Number of edges should be less than n*n");
        }
        if (n <= 0 || nbEdges <= 0) {
            throw new IllegalArgumentException("Values given should be positive");
        }
        var g = new MatGraph(n);
        int maxWeight = 10;
        IntSupplier vertexGenerator = () -> (int) (Math.random() * (n));
        IntSupplier weightGenerator = () -> (int) (Math.random() * (maxWeight + 1));
        while (g.numberOfEdges() < nbEdges) {
            int i, j, weight;
            do {
                i = vertexGenerator.getAsInt();
                j = vertexGenerator.getAsInt();
            } while (g.isEdge(i, j) );
            do {
                weight = weightGenerator.getAsInt();
            } while (weight == 0);
//            System.out.println("Adding edge : " + i + " -> " + j + " : " + weight);
            g.addEdge(i, j, weight);
        }
        return g;
    }

    /**
     * Compute the transposed graph of the current graph
     * @return the transposed graph
     */
    default Graph transpose() {
        var g = new MatGraph(numberOfVertices());
        for (int i = 0 ; i < numberOfVertices() ; ++i) {
            forEachEdge(i, e -> {
                g.addEdge(e.getEnd(), e.getStart(), e.getValue());
            });
        }
        return g;
    }
}
