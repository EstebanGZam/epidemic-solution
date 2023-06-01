package com.example.epidemicsolution.dataStructures.graph.graphAdjacencyList;

import com.example.epidemicsolution.dataStructures.graph.*;
import com.example.epidemicsolution.exception.GraphException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The GraphAdjacencyMatrix class represents a graph data structure using an adjacency list.
 * It extends the Graph class and provides implementations for various graph operations.
 *
 * @param <K> the type of the vertex key
 * @param <E> the type of the vertex element
 */
public class GraphAdjacencyList<K extends Comparable<K>, E> extends Graph<K, E> {

    private final HashMap<K, VertexAdjacentList<K, E>> vertexes;

    /**
     * Constructs a graph with the specified type.
     *
     * @param graphType the type of the graph
     */
    public GraphAdjacencyList(GraphType graphType) {
        super(graphType);
        vertexes = new HashMap<>();
    }

    /**
     * Inserts a vertex with the given key and element into the graph.
     *
     * @param key     the key of the vertex
     * @param element the element of the vertex
     */
    @Override
    public void insertVertex(K key, E element) {
        if (vertexes.get(key) == null) vertexes.put(key, new VertexAdjacentList<>(key, element));
    }

    /**
     * Deletes the vertex with the specified key from the graph.
     *
     * @param keyVertex the key of the vertex to be deleted
     */
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

    /**
     * Returns the vertex with the specified key.
     *
     * @param keyVertex the key of the vertex to retrieve
     * @return the vertex with the specified key, or null if not found
     */
    @Override
    public Vertex<K, E> getVertex(K keyVertex) {
        return vertexes.get(keyVertex);
    }

    /**
     * Inserts an edge between the vertices with the specified keys and the given weight into the graph.
     *
     * @param key1   the key of the first vertex
     * @param key2   the key of the second vertex
     * @param weight the weight of the edge
     * @throws GraphException if the graph does not support loops or multiple edges and the insertion violates this rule
     */
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

    /**
     * Deletes the edge between two vertices with the specified keys from the graph.
     *
     * @param keyVertex1 the key of the first vertex
     * @param keyVertex2 the key of the second vertex
     * @throws GraphException if either vertex is not found
     */
    @Override
    public void deleteEdge(K keyVertex1, K keyVertex2) throws GraphException {
        VertexAdjacentList<K, E> v1 = vertexes.get(keyVertex1);
        VertexAdjacentList<K, E> v2 = vertexes.get(keyVertex2);
        if (v1 == null || v2 == null) throw new GraphException("Vertex not found");
        edges.removeIf(edge -> edge.destination().getKey().compareTo(keyVertex2) == 0
                || edge.destination().getKey().compareTo(keyVertex1) == 0);
        v1.getAdjacentList().removeIf(edge -> edge.destination().getKey().compareTo(keyVertex2) == 0);
        if (!isDirected)
            v2.getAdjacentList().removeIf(edge -> edge.destination().getKey().compareTo(keyVertex1) == 0);
    }

    /**
     * Checks if two vertices with the specified keys are adjacent in the graph.
     *
     * @param keyVertex1 the key of the first vertex
     * @param keyVertex2 the key of the second vertex
     * @return true if the vertices are adjacent, false otherwise
     * @throws GraphException if either vertex is not found
     */
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

    /**
     * Performs a Breadth-First Search starting from the vertex with the specified key.
     *
     * @param keyVertex the key of the starting vertex
     */
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

    /**
     * Performs Dijkstra's algorithm starting from the vertex with the specified key.
     *
     * @param keyVertexSource the key of the source vertex
     * @return an ArrayList of distances from the source vertex to each vertex in the graph
     * @throws GraphException if the source vertex is not found
     */
    @Override
    public ArrayList<Integer> dijkstra(K keyVertexSource) {
        if (vertexes.get(keyVertexSource) == null) {
            throw new GraphException("Source vertex not found.");
        }
        for (VertexAdjacentList<K, E> vertex : vertexes.values()) {
            if (vertex.getKey().compareTo(keyVertexSource) != 0)
                vertex.setDistance(Integer.MAX_VALUE);
            vertex.setPredecessor(null);
        }
        vertexes.get(keyVertexSource).setDistance(0);
        PriorityQueue<VertexAdjacentList<K, E>> priorityQueue = new PriorityQueue<>(Comparator.comparing(Vertex<K, E>::getDistance));
        for (VertexAdjacentList<K, E> vertex : vertexes.values()) {
            priorityQueue.offer(vertex);
        }
        while (!priorityQueue.isEmpty()) {
            VertexAdjacentList<K, E> u = priorityQueue.poll();
            for (Edge<K, E> edge : u.getAdjacentList()) {
                VertexAdjacentList<K, E> v = (VertexAdjacentList<K, E>) edge.destination();
                int alt = u.getDistance() + edge.weight();
                if (alt < v.getDistance()) {
                    priorityQueue.remove(v);
                    v.setDistance(alt);
                    v.setPredecessor(u);
                    priorityQueue.offer(v);
                }
            }
        }
        return vertexes.values().stream().map(Vertex::getDistance).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Returns the index of the vertex with the specified key in the vertexes HashMap
     *
     * @param keyVertex the key of the vertex
     * @return the index of the vertex, or -1 if not found
     */
    private int vertexNumber(K keyVertex) {
        int index = 0;
        for (K key : vertexes.keySet()) {
            if (key.equals(keyVertex)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    /**
     * Applies Kruskal's algorithm to find the minimum spanning tree of the graph.
     *
     * @return an ArrayList of edges in the minimum spanning tree.
     */
    @Override
    public ArrayList<Edge<K, E>> kruskal() {
        ArrayList<Edge<K, E>> A = new ArrayList<>();
        UnionFind unionFind = new UnionFind(vertexes.size());
        edges.sort(Comparator.comparingInt(Edge::weight));
        for (Edge<K, E> edge : edges) {
            int u = vertexNumber(edge.start().getKey());
            int v = vertexNumber(edge.destination().getKey());
            if (unionFind.find(u) != unionFind.find(v)) {
                A.add(edge);
                unionFind.union(u, v);
            }
        }
        return A;
    }

}
