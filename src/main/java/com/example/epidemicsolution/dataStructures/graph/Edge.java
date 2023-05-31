package com.example.epidemicsolution.dataStructures.graph;

public record Edge<K extends Comparable<K>, E>(Vertex<K, E> start, Vertex<K, E> destination, int weight) {
	public String toString() {
		return start.toString() + "->" + destination.toString();
	}
}
