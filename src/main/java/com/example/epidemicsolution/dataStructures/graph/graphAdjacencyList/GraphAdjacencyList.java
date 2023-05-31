package com.example.epidemicsolution.dataStructures.graph.graphAdjacencyList;

import com.example.epidemicsolution.dataStructures.graph.*;
import com.example.epidemicsolution.exception.GraphException;

import java.util.*;
import java.util.stream.Collectors;

public class GraphAdjacencyList<K extends Comparable<K>, E> extends Graph<K, E> {
	private final HashMap<K, VertexAdjacentList<K, E>> vertexes;

	public GraphAdjacencyList(GraphType graphType) {
		super(graphType);
		vertexes = new HashMap<>();
	}

	@Override
	public void insertVertex(K key, E element) {
		if (vertexes.get(key) == null) vertexes.put(key, new VertexAdjacentList<>(key, element));
	}

	@Override
	public void deleteVertex(K keyVertex) {
		VertexAdjacentList<K, E> v = vertexes.remove(keyVertex);
		if (v != null) {
			for (K key : vertexes.keySet()) {
				VertexAdjacentList<K, E> vertex = vertexes.get(key);
				LinkedList<Edge<K, E>> edgesList = vertex.getAdjacentList();
				edgesList.removeIf(edge -> edge.destination().getKey().compareTo(keyVertex) == 0);
			}
		}
	}

	@Override
	public Vertex<K, E> getVertex(K keyVertex) {
		return vertexes.get(keyVertex);
	}

	@Override
	public void insertEdge(K key1, K key2, int weight) throws GraphException {
		VertexAdjacentList<K, E> v1 = vertexes.get(key1);
		VertexAdjacentList<K, E> v2 = vertexes.get(key2);
		if (v1 == null || v2 == null) throw new GraphException("Vertex not found");

		if (!loops && key1.compareTo(key2) == 0) throw new GraphException("This type of graph does not support loops.");
		Edge<K, E> edge1 = new Edge<>(v1, v2, weight);
		if (!multipleEdges && v1.getAdjacentList().contains(edge1))
			throw new GraphException("This type of graph does not support multiple edges.");
		v1.getAdjacentList().add(edge1);
		edges.add(edge1);
		if (!isDirected) {
			Edge<K, E> edge2 = new Edge<>(v2, v1, weight);
			v2.getAdjacentList().add(edge2);
			edges.add(edge2);
		}
	}

	@Override
	public void deleteEdge(K keyVertex1, K keyVertex2, int weight) throws GraphException {
		VertexAdjacentList<K, E> v1 = vertexes.get(keyVertex1);
		VertexAdjacentList<K, E> v2 = vertexes.get(keyVertex2);
		if (v1 == null || v2 == null) throw new GraphException("Vertex not found");
		edges.removeIf(edge -> edge.destination().getKey().compareTo(keyVertex2) == 0
				|| edge.destination().getKey().compareTo(keyVertex1) == 0 && edge.weight() == weight);
		v1.getAdjacentList().removeIf(edge -> edge.destination().getKey().compareTo(keyVertex2) == 0 && edge.weight() == weight);
		if (!isDirected)
			v2.getAdjacentList().removeIf(edge -> edge.destination().getKey().compareTo(keyVertex1) == 0 && edge.weight() == weight);
	}

	@Override
	public boolean adjacent(K keyVertex1, K keyVertex2) throws GraphException {
		VertexAdjacentList<K, E> v1 = vertexes.get(keyVertex1);
		VertexAdjacentList<K, E> v2 = vertexes.get(keyVertex2);
		if (v1 == null || v2 == null) throw new GraphException("Vertex not found");
		boolean found = false;
		LinkedList<Edge<K, E>> adjacencyList = v1.getAdjacentList();
		for (int i = 0; i < adjacencyList.size() && !found; i++) {
			found = adjacencyList.get(i).destination().getKey().compareTo(keyVertex2) == 0;
		}
		return found;
	}

	@Override
	public void BFS(K keyVertex) {
		for (K key : vertexes.keySet()) {
			VertexAdjacentList<K, E> u = vertexes.get(key);
			u.setColor(Color.WHITE);
			u.setDistance(Integer.MAX_VALUE);
			u.setPredecessor(null);
		}
		VertexAdjacentList<K, E> s = vertexes.get(keyVertex);
		s.setColor(Color.GRAY);
		s.setDistance(0);

		Queue<VertexAdjacentList<K, E>> queue = new LinkedList<>();
		queue.offer(s);

		while (!queue.isEmpty()) {
			VertexAdjacentList<K, E> u = queue.poll();
			for (Edge<K, E> edge : u.getAdjacentList()) {
				VertexAdjacentList<K, E> v = (VertexAdjacentList<K, E>) edge.destination();
				if (v.getColor() == Color.WHITE) {
					v.setColor(Color.GRAY);
					v.setDistance(u.getDistance() + 1);
					v.setPredecessor(u);
					queue.offer(v);
				}
			}
			u.setColor(Color.BLACK);
		}
	}

	@Override
	public void DFS() {
		for (K key : vertexes.keySet()) {
			VertexAdjacentList<K, E> u = vertexes.get(key);
			u.setColor(Color.WHITE);
			u.setPredecessor(null);
		}
		time = 0;
		for (K key : vertexes.keySet()) {
			VertexAdjacentList<K, E> u = vertexes.get(key);
			if (u.getColor() == Color.WHITE) {
				dfsVisit(u);
			}
		}
	}

	private void dfsVisit(VertexAdjacentList<K, E> u) {
		time++;
		u.setDiscoveryTime(time);
		u.setColor(Color.GRAY);
		for (Edge<K, E> edge : u.getAdjacentList()) {
			VertexAdjacentList<K, E> v = (VertexAdjacentList<K, E>) edge.destination();
			if (v.getColor() == Color.WHITE) {
				v.setPredecessor(u);
				dfsVisit(v);
			}
		}
		u.setColor(Color.BLACK);
		time++;
		u.setFinishTime(time);
	}

	@Override
	public ArrayList<Integer> dijkstra(K keyVertexSource) {
		vertexes.get(keyVertexSource).setDistance(0);
		PriorityQueue<VertexAdjacentList<K, E>> priorityQueue = new PriorityQueue<>(Comparator.comparing(Vertex<K, E>::getDistance));
		for (VertexAdjacentList<K, E> vertex : vertexes.values()) {
			if (vertex.getKey().compareTo(keyVertexSource) != 0)
				vertex.setDistance(Integer.MAX_VALUE);
			vertex.setPredecessor(null);
			priorityQueue.offer(vertex);
		}
		while (!priorityQueue.isEmpty()) {
			VertexAdjacentList<K, E> u = priorityQueue.poll();
			for (Edge<K, E> edge : u.getAdjacentList()) {
				VertexAdjacentList<K, E> v = (VertexAdjacentList<K, E>) edge.destination();
				int alt = u.getDistance() + edge.weight();
				if (alt < v.getDistance()) {
					v.setDistance(alt);
					v.setPredecessor(u);
					priorityQueue.offer(v);
				}
			}
		}
		return vertexes.values().stream().map(Vertex::getDistance).collect(Collectors.toCollection(ArrayList::new));
	}

	public ArrayList<Edge<K, E>> kruskal() {
		return super.kruskal(vertexes.size());
	}

}
