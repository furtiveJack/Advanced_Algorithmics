package fr.umlv.info2.graphs;

import java.util.Arrays;

public class ShortestPathFromAllVertices {
    private final int[][] d;
    private final int[][] pi;

    ShortestPathFromAllVertices(int[][] d, int[][] pi) {
        this.d = d;
        this.pi = pi;
    }

    @Override
    public String toString() {
        StringBuffer bf = new StringBuffer();
        for (int i = 0; i < d.length; i++) {
            bf.append(Arrays.toString(d[i])).append("\t").append(Arrays.toString(pi[i])).append("\n");
        }

        return bf.toString();
    }
}
