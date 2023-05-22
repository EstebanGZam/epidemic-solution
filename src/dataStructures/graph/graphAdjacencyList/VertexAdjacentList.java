package dataStructures.graph.graphAdjacencyList;

import dataStructures.graph.Edge;
import dataStructures.graph.Vertex;

import java.util.LinkedList;

public class VertexAdjacentList<K extends Comparable<K>, E> extends Vertex<K, E> {
	private final LinkedList<Edge<K, E>> adjacentList;
	public VertexAdjacentList(K key, E element) {
		super(key, element);
		adjacentList = new LinkedList<>();
	}

	public LinkedList<Edge<K, E>> getAdjacentList() {
		return adjacentList;
	}
}
