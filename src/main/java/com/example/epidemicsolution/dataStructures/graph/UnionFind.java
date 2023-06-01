package com.example.epidemicsolution.dataStructures.graph;

/**
 * The UnionFind class represents a data structure for efficiently managing disjoint sets.
 * It supports the find and union operations.
 */
public class UnionFind {

    private final int[] parent;

    /**
     * Creates a new UnionFind instance with the specified size.
     *
     * @param size the size of the UnionFind data structure.
     */
    public UnionFind(int size) {
        parent = new int[size];
        for (int i = 0; i < size; i++) {
            parent[i] = i;
        }
    }

    /**
     * Finds the representative element of the set that the element 'a' belongs to.
     * Uses path compression to optimize the find operation.
     *
     * @param a the element to find
     * @return the representative element of the set
     */
    public int find(int a) {
        if (parent[a] != a) parent[a] = find(parent[a]);
        return parent[a];
    }

    /**
     * Unions the sets that contain elements 'a' and 'b' by setting the root of one set as the parent of the other set's root.
     *
     * @param a the first element
     * @param b the second element
     */
    public void union(int a, int b) {
        int rootX = find(a);
        int rootY = find(b);
        if (rootX != rootY) parent[rootX] = rootY;
    }

}
