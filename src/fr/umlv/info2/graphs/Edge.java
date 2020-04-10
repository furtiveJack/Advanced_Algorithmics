package fr.umlv.info2.graphs;

public class Edge {
    private final int start;
    private final int end;
    private final int value;

    public Edge(int start, int end, int value) {
        this.start = start;
        this.end = end;
        this.value = value;
    }

    public Edge(int start, int end) {
        this.start = start;
        this.end = end;
        this.value = 1;
    }

    public int getValue() { return value; }

    public int getStart() { return start; }

    public int getEnd() { return end; }

    @Override
    public String toString() {
        return start + " -- " + end + " ( " + value + " )";
    }

    @Override
    public boolean equals(Object o) {
        if ( ! (o instanceof  Edge) ) {
            return false;
        }
        Edge e = (Edge) o;
        return e.start == start && e.end == end && e.value == value;
    }

    @Override
    public int hashCode() {
        return start ^ end ^ value;
    }

}
