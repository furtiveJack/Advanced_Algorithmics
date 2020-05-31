package fr.umlv.info2.graphs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class ShortestPathFromOneVertex {
    private final int source;
    private final int[] d;
    private final int[] pi;

    ShortestPathFromOneVertex(int vertex, int[] d, int[] pi) {
        this.source = vertex;
        this.d = d;
        this.pi = pi;
    }

    @Override
    public String toString() {
        return source + " " + Arrays.toString(d) + " " + Arrays.toString(pi);
    }

    public LinkedList<Integer> printShortestPath(int dest) {
        int current = pi[dest];
        LinkedList<Integer> res = new LinkedList<>();
        res.push(dest);
        while (current != source) {
            res.push(current);
            current = pi[current];
        }
        res.push(current);
        System.out.println(res);
        return res;
    }

    public int[] getD() {
        return d;
    }

    public int[] getPi() {
        return pi;
    }
}