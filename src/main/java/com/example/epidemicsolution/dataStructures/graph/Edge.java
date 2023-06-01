package com.example.epidemicsolution.dataStructures.graph;

/**
 * Represents an edge in a graph.
 *
 * @param <K> the type of key used in vertices
 * @param <E> the type of element stored in vertices
 */
public record Edge<K extends Comparable<K>, E>(Vertex<K, E> start, Vertex<K, E> destination, int weight) {
}