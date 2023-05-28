package dataStructures.graph.graphAdjacencyMatrix;

import dataStructures.graph.Edge;
import dataStructures.graph.Graph;
import dataStructures.graph.GraphType;
import dataStructures.graph.Vertex;
import exception.GraphException;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class GraphAdjacencyMatrix<K extends Comparable<K>, E> extends Graph<K, E> {
	private final HashMap<K, Integer> vertexesIndex;
	private final HashMap<Integer, Vertex<K, E>> vertexes;
	private int currentVertexNumber;
	private static final int maxVertexes = 50;
	private final int[][] adjacencyMatrix;

	public GraphAdjacencyMatrix(GraphType graphType) {
		this(maxVertexes, graphType);
	}

	public GraphAdjacencyMatrix(int maxVertex, GraphType graphType) {
		super(graphType);
		this.vertexes = new HashMap<>();
		this.currentVertexNumber = 0;
		this.vertexesIndex = new HashMap<>();
		adjacencyMatrix = new int[maxVertex][maxVertex];
		for (int i = 0; i < maxVertex; i++)
			for (int j = 0; j < maxVertex; j++)
				adjacencyMatrix[i][j] = 0;
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
				adjacencyMatrix[vertexIndex][i] = 0;
				adjacencyMatrix[i][vertexIndex] = 0;
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
		if (!multipleEdges && adjacencyMatrix[va][vb] > 0)
			throw new GraphException("This type of graph does not support multiple edges.");
		edges.add(new Edge<>(vertexes.get(va), vertexes.get(vb), weight));
		adjacencyMatrix[va][vb]++;
		if (!isDirected) {
			edges.add(new Edge<>(vertexes.get(vb), vertexes.get(va), weight));
			adjacencyMatrix[vb][va]++;
		}
	}

	@Override
	public void deleteEdge(K keyVertex1, K keyVertex2) throws GraphException {
		verifyExistence(keyVertex1, keyVertex2);
		int va = vertexesIndex.get(keyVertex1);
		int vb = vertexesIndex.get(keyVertex2);
		if (adjacencyMatrix[va][vb] > 0) {
			edges.removeIf(edge -> edge.destination().getKey().compareTo(keyVertex2) == 0
					|| edge.destination().getKey().compareTo(keyVertex1) == 0);
			adjacencyMatrix[va][vb]--;
			if (!isDirected) adjacencyMatrix[vb][va]--;
		}
	}

	@Override
	public boolean adjacent(K keyVertex1, K keyVertex2) throws GraphException {
		verifyExistence(keyVertex1, keyVertex2);
		return adjacencyMatrix[vertexNumber(keyVertex1)][vertexNumber(keyVertex2)] > 0;
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
				if (adjacencyMatrix[uIndex][i] > 0) {
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
			if (adjacencyMatrix[uIndex][i] > 0) {
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

}
