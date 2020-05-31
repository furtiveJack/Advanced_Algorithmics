package fr.umlv.info2.graphs;

import java.util.Arrays;
import java.util.LinkedList;

public class ShortestPathFromAllVertices {
    private final int[][] d;
    private final int[][] pi;

    ShortestPathFromAllVertices(int[][] d, int[][] pi) {
        this.d = d;
        this.pi = pi;
    }

    @Override
    public String toString() {
        StringBuilder bf = new StringBuilder();
        for (int i = 0; i < d.length; i++) {
            bf.append(Arrays.toString(d[i])).append("\t\t\t\t").append(Arrays.toString(pi[i])).append("\n");
        }

        return bf.toString();
    }

    public LinkedList<Integer> printShortestPath(int source, int dest) {
        int current = pi[source][dest];
        if (current == Integer.MIN_VALUE) {
            System.out.println("[]");
            return null;
        }
        LinkedList<Integer> res = new LinkedList<>();
        res.push(dest);
        while (current != source) {
            res.push(current);
            current = pi[source][current];
            if (current == Integer.MIN_VALUE) {
                System.out.println("[]");
                return null;
            }
        }
        res.push(current);
        System.out.println(res);
        return res;
    }
}
