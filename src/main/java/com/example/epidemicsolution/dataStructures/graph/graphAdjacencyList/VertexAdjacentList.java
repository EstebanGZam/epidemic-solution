package com.example.epidemicsolution.dataStructures.graph.graphAdjacencyList;

import com.example.epidemicsolution.dataStructures.graph.Edge;
import com.example.epidemicsolution.dataStructures.graph.Vertex;

import java.util.LinkedList;

/**
 * Represents a vertex in an adjacency list implementation of a graph.
 *
 * @param <K> The type of the vertex key.
 * @param <E> The type of the vertex element.
 */
public class VertexAdjacentList<K extends Comparable<K>, E> extends Vertex<K, E> {

    private final LinkedList<Edge<K, E>> adjacentList;

    /**
     * Constructs a new VertexAdjacentList object with the specified key and element.
     *
     * @param key     the key of the vertex
     * @param element the element stored in the vertex
     */
    public VertexAdjacentList(K key, E element) {
        super(key, element);
        adjacentList = new LinkedList<>();
    }

    public LinkedList<Edge<K, E>> getAdjacentList() {
        return adjacentList;
    }

}
