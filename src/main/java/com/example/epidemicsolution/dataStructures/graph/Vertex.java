package com.example.epidemicsolution.dataStructures.graph;

/**
 * The {@code Vertex} class represents a vertex in a graph.
 *
 * @param <K> the type of the key used in the vertex, must extend {@code Comparable}
 * @param <E> the type of the data stored in the vertex
 */
public class Vertex<K extends Comparable<K>, E> {

    private final K key; // The key of the vertex
    private final E element; // The element stored in the vertex
    private Color color; // The color of the vertex
    private int distance = 0, discoveryTime = 0, finishTime = 0;
    private Vertex<K, E> predecessor;

    /**
     * Constructs a new vertex with the specified key and element.
     *
     * @param key     the key of the vertex
     * @param element the element stored in the vertex
     */
    public Vertex(K key, E element) {
        this.key = key;
        this.element = element;
        color = Color.WHITE;
    }

    public K getKey() {
        return key;
    }

    public E getElement() {
        return element;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setPredecessor(Vertex<K, E> predecessor) {
        this.predecessor = predecessor;
    }

    public void setDiscoveryTime(int discoverTime) {
        this.discoveryTime = discoverTime;
    }

    public void setFinishTime(int finishTime) {
        this.finishTime = finishTime;
    }

    public int getDiscoveryTime() {
        return discoveryTime;
    }

    public int getFinishTime() {
        return finishTime;
    }

}