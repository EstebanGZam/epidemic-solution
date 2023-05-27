package com.example.epidemicsolution.dataStructures.graph;

public record Edge<K extends Comparable<K>, E>(Vertex<K, E> start, Vertex<K, E> destination, double weight) {
}
