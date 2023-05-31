package com.example.epidemicsolution.dataStructures.graph;

import com.example.epidemicsolution.exception.GraphException;

import java.util.ArrayList;

/**
 * The {@code IGraph} interface represents a graph data structure.
 *
 * @param <K> the type of the key used in the graph vertices, must extend {@code Comparable}
 * @param <E> the type of the data stored in the graph edges
 */
public interface IGraph<K extends Comparable<K>, E> {

	/**
	 * Inserts a new vertex with the specified key and element into the graph.
	 *
	 * @param key     the key of the vertex
	 * @param element the element to be stored in the vertex
	 */
	void insertVertex(K key, E element);

	/**
	 * Deletes the vertex with the specified key from the graph.
	 *
	 * @param keyVertex the key of the vertex to be deleted
	 */
	void deleteVertex(K keyVertex);

	/**
	 * Retrieves the vertex with the specified key from the graph.
	 *
	 * @param keyVertex the key of the vertex to be retrieved
	 * @return the vertex with the specified key, or {@code null} if not found
	 */
	Vertex<K, E> getVertex(K keyVertex);

	/**
	 * Inserts a new edge between the vertices with the specified keys, with the given weight, into the graph.
	 *
	 * @param keyVertex1 the key of the first vertex
	 * @param keyVertex2 the key of the second vertex
	 * @param weight     the weight of the edge
	 * @throws GraphException if the vertices are not found or an edge already exists between them
	 */
	void insertEdge(K keyVertex1, K keyVertex2, int weight) throws GraphException;

	/**
	 * Deletes the edge between the vertices with the specified keys from the graph.
	 *
	 * @param keyVertex1 the key of the first vertex
	 * @param keyVertex2 the key of the second vertex
	 * @throws GraphException if the vertices are not found or there is no edge between them
	 */
	void deleteEdge(K keyVertex1, K keyVertex2, int weight) throws GraphException;

	/**
	 * Checks whether there is an edge between the vertices with the specified keys in the graph.
	 *
	 * @param keyVertex1 the key of the first vertex
	 * @param keyVertex2 the key of the second vertex
	 * @return {@code true} if there is an edge between the vertices, {@code false} otherwise
	 * @throws GraphException if the vertices are not found
	 */
	boolean adjacent(K keyVertex1, K keyVertex2) throws GraphException;

	/**
	 * Performs a breadth-first search starting from the vertex with the specified key in the graph.
	 *
	 * @param keyVertex the key of the starting vertex
	 */
	void BFS(K keyVertex);

	/**
	 * Performs a depth-first search in the graph.
	 */
	void DFS();

	/**
	 * Finds the shortest paths from a specified source vertex to all other vertices in the graph using Dijkstra's algorithm.
	 *
	 * @param keyVertexSource the key or identifier of the source vertex
	 * @return an ArrayList<Integer> representing the shortest paths from the source vertex to all other vertices
	 */
	ArrayList<Integer> dijkstra(K keyVertexSource);


	/**
	 * Method that implements the Kruskal's algorithm to find the minimum spanning tree.
	 *
	 * @return ArrayList with the edges of the minimum spanning tree
	 */
	ArrayList<Edge<K, E>> kruskal();

}

