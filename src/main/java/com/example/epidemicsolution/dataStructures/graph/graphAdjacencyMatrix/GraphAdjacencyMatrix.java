package com.example.epidemicsolution.dataStructures.graph.graphAdjacencyMatrix;

import com.example.epidemicsolution.dataStructures.graph.Graph;
import com.example.epidemicsolution.dataStructures.graph.GraphType;
import com.example.epidemicsolution.dataStructures.graph.Vertex;
import com.example.epidemicsolution.exception.GraphException;
import com.example.epidemicsolution.dataStructures.graph.Color;

import java.util.*;
import java.util.stream.Collectors;

public class GraphAdjacencyMatrix<K extends Comparable<K>, E> extends Graph<K, E> {

	private final HashMap<K, Integer> vertexesIndex;
	private final HashMap<Integer, Vertex<K, E>> vertexes;
	private int currentVertexNumber;
	private static final int maxVertexes = 50;
	private final ArrayList<Integer>[][] adjacencyMatrix;

	public GraphAdjacencyMatrix(GraphType graphType) {
		this(maxVertexes, graphType);
	}

	public GraphAdjacencyMatrix(int maxVertex, GraphType graphType) {
		super(graphType);
		this.vertexes = new HashMap<>();
		this.currentVertexNumber = 0;
		this.vertexesIndex = new HashMap<>();
		adjacencyMatrix = new ArrayList[maxVertex][maxVertex];
		for (int i = 0; i < maxVertex; i++)
			for (int j = 0; j < maxVertex; j++)
				adjacencyMatrix[i][j] = new ArrayList<>();
	}

	@Override
	public void insertVertex(K key, E element) {
		if (vertexesIndex.get(key) == null) {
			vertexesIndex.put(key, currentVertexNumber);
			vertexes.put(currentVertexNumber++, new Vertex<>(key, element));
		}
	}

	@Override
	public void deleteVertex(K keyVertex) {
		Vertex<K, E> v = vertexes.remove(vertexNumber(keyVertex));
		if (v != null) {
			int vertexIndex = vertexesIndex.get(v.getKey());
			for (int i = 0; i < maxVertexes; i++) {
				adjacencyMatrix[vertexIndex][i] = new ArrayList<>();
				adjacencyMatrix[i][vertexIndex] = new ArrayList<>();
			}
		}
	}

	@Override
	public Vertex<K, E> getVertex(K keyVertex) {
		return vertexes.get(vertexNumber(keyVertex));
	}

	@Override
	public void insertEdge(K keyVertex1, K keyVertex2, int weight) throws GraphException {
		verifyExistence(keyVertex1, keyVertex2);
		int va = vertexesIndex.get(keyVertex1);
		int vb = vertexesIndex.get(keyVertex2);
		if (!loops && va == vb) throw new GraphException("This type of graph does not support loops.");
		if (!multipleEdges && adjacencyMatrix[va][vb].size() > 0)
			throw new GraphException("This type of graph does not support multiple edges.");
//		edges.add(new Edge<>(vertexes.get(va), vertexes.get(vb), weight));
		adjacencyMatrix[va][vb].add(weight);
		if (!isDirected) {
//			edges.add(new Edge<>(vertexes.get(vb), vertexes.get(va), weight));
			adjacencyMatrix[vb][va].add(weight);
		}
	}

	@Override
	public void deleteEdge(K keyVertex1, K keyVertex2, int weight) throws GraphException {
		verifyExistence(keyVertex1, keyVertex2);
		int va = vertexesIndex.get(keyVertex1);
		int vb = vertexesIndex.get(keyVertex2);
		if (adjacencyMatrix[va][vb].size() > 0) {
			adjacencyMatrix[va][vb].remove(weight);
			if (!isDirected) adjacencyMatrix[vb][va].remove(weight);
		}
	}

	@Override
	public boolean adjacent(K keyVertex1, K keyVertex2) throws GraphException {
		verifyExistence(keyVertex1, keyVertex2);
		return adjacencyMatrix[vertexNumber(keyVertex1)][vertexNumber(keyVertex2)].size() > 0;
	}

	@Override
	public void BFS(K keyVertex) {
		for (Integer key : vertexes.keySet()) {
			Vertex<K, E> u = vertexes.get(key);
			u.setColor(Color.WHITE);
			u.setDistance(Integer.MAX_VALUE);
			u.setPredecessor(null);
		}
		Vertex<K, E> s = vertexes.get(vertexNumber(keyVertex));
		s.setColor(Color.GRAY);
		s.setDistance(0);

		Queue<Vertex<K, E>> queue = new LinkedList<>();
		queue.offer(s);

		while (!queue.isEmpty()) {
			Vertex<K, E> u = queue.poll();
			int uIndex = vertexNumber(u.getKey());
			for (int i = 0; i < maxVertexes; i++) {
				if (adjacencyMatrix[uIndex][i].size() > 0) {
					Vertex<K, E> v = vertexes.get(i);
					if (v.getColor() == Color.WHITE) {
						v.setColor(Color.GRAY);
						v.setDistance(u.getDistance() + 1);
						v.setPredecessor(u);
						queue.offer(v);
					}
				}
			}
			u.setColor(Color.BLACK);
		}
	}

	@Override
	public void DFS() {
		for (Integer key : vertexes.keySet()) {
			Vertex<K, E> u = vertexes.get(key);
			u.setColor(Color.WHITE);
			u.setPredecessor(null);
		}
		time = 0;
		for (Integer key : vertexes.keySet()) {
			Vertex<K, E> u = vertexes.get(key);
			if (u.getColor() == Color.WHITE) {
				dfsVisit(u);
			}
		}
	}

	private void dfsVisit(Vertex<K, E> u) {
		time++;
		u.setDiscoveryTime(time);
		u.setColor(Color.GRAY);

		int uIndex = vertexNumber(u.getKey());
		for (int i = 0; i < maxVertexes; i++) {
			if (adjacencyMatrix[uIndex][i].size() > 0) {
				Vertex<K, E> v = vertexes.get(i);
				if (v.getColor() == Color.WHITE) {
					v.setPredecessor(u);
					dfsVisit(v);
				}
			}
		}
		u.setColor(Color.BLACK);
		time++;
		u.setFinishTime(time);
	}

	private void verifyExistence(K keyVertex1, K keyVertex2) throws GraphException {
		Vertex<K, E> v1 = vertexes.get(vertexNumber(keyVertex1));
		Vertex<K, E> v2 = vertexes.get(vertexNumber(keyVertex2));
		if (v1 == null || v2 == null) throw new GraphException("Vertex not found");
	}

	private int vertexNumber(K keyVertex) {
		Integer index = vertexesIndex.get(keyVertex);
		return index == null ? -1 : index;
	}

	@Override
	public ArrayList<Integer> dijkstra(K keyVertexSource) {
		vertexes.get(vertexNumber(keyVertexSource)).setDistance(0);
		PriorityQueue<Vertex<K, E>> priorityQueue = new PriorityQueue<>(Comparator.comparing(Vertex<K, E>::getDistance));
		for (Vertex<K, E> vertex : vertexes.values()) {
			if (vertex.getKey().compareTo(keyVertexSource) != 0)
				vertex.setDistance(Integer.MAX_VALUE);
			vertex.setPredecessor(null);
			priorityQueue.offer(vertex);
		}
		while (!priorityQueue.isEmpty()) {
			Vertex<K, E> u = priorityQueue.poll();
			for (int i = 0; i < maxVertexes; i++) {
				int uIndex = vertexNumber(u.getKey());
				if (adjacencyMatrix[uIndex][i].size() > 0) {
					Vertex<K, E> v = vertexes.get(i);
					int alt = u.getDistance() + adjacencyMatrix[uIndex][i].get(0);
					if (alt < v.getDistance()) {
						v.setDistance(alt);
						v.setPredecessor(u);
						priorityQueue.offer(v);
					}
				}
			}
		}
		return vertexes.values().stream().map(Vertex::getDistance).collect(Collectors.toCollection(ArrayList::new));
	}

}
