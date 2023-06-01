package com.example.epidemicsolution.dataStructures.graph.graphAdjacencyMatrix;

import com.example.epidemicsolution.dataStructures.graph.*;
import com.example.epidemicsolution.exception.GraphException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The GraphAdjacencyMatrix class represents a graph data structure using an adjacency matrix.
 * It extends the Graph class and provides implementations for various graph operations.
 *
 * @param <K> the type of the vertex key
 * @param <E> the type of the vertex element
 */
public class GraphAdjacencyMatrix<K extends Comparable<K>, E> extends Graph<K, E> {

    private final HashMap<K, Vertex<K, E>> vertexes;
    private static final int maxVertexes = 50;
    private final ArrayList<Integer>[][] adjacencyMatrix;

    /**
     * Creates a new instance of the GraphAdjacencyMatrix class with the specified graph type.
     *
     * @param graphType the type of the graph
     */
    public GraphAdjacencyMatrix(GraphType graphType) {
        this(maxVertexes, graphType);
    }

    /**
     * Constructs a GraphAdjacencyMatrix object with the specified maximum number of vertices and graph type.
     *
     * @param maxVertex the maximum number of vertices allowed in the graph
     * @param graphType the type of the graph
     */
    public GraphAdjacencyMatrix(int maxVertex, GraphType graphType) {
        super(graphType);
        this.vertexes = new HashMap<>();
        adjacencyMatrix = new ArrayList[maxVertex][maxVertex];
        for (int i = 0; i < maxVertex; i++)
            for (int j = 0; j < maxVertex; j++)
                adjacencyMatrix[i][j] = new ArrayList<>();
    }

    /**
     * Inserts a vertex with the specified key and element into the graph.
     *
     * @param key     the key of the vertex
     * @param element the element of the vertex
     */
    @Override
    public void insertVertex(K key, E element) {
        if (vertexes.get(key) == null) {
            vertexes.put(key, new Vertex<>(key, element));
            vertexesIndex.put(key, currentVertexNumber++);
        }
    }

    /**
     * Deletes the vertex with the specified key from the graph.
     *
     * @param keyVertex the key of the vertex to be deleted
     */
    @Override
    public void deleteVertex(K keyVertex) {
        Vertex<K, E> v = vertexes.remove(keyVertex);
        if (v != null) {
            int vertexIndex = vertexNumber(keyVertex);
            for (int i = 0; i < maxVertexes; i++) {
                adjacencyMatrix[vertexIndex][i] = new ArrayList<>();
                adjacencyMatrix[i][vertexIndex] = new ArrayList<>();
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
     * @param keyVertex1 the key of the first vertex
     * @param keyVertex2 the key of the second vertex
     * @param weight     the weight of the edge
     * @throws GraphException if the graph does not support loops or multiple edges and the insertion violates this rule
     */
    @Override
    public void insertEdge(K keyVertex1, K keyVertex2, int weight) throws GraphException {
        verifyExistence(keyVertex1, keyVertex2);
        int va = vertexNumber(keyVertex1);
        int vb = vertexNumber(keyVertex2);
        if (!loops && va == vb) throw new GraphException("This type of graph does not support loops.");
        if (!multipleEdges && adjacencyMatrix[va][vb].size() > 0)
            throw new GraphException("This type of graph does not support multiple edges.");
        adjacencyMatrix[va][vb].add(weight);
        Collections.sort(adjacencyMatrix[va][vb]);
        edges.add(new Edge<>(getVertex(keyVertex1), getVertex(keyVertex2), weight));
        if (!isDirected) {
            adjacencyMatrix[vb][va].add(weight);
            Collections.sort(adjacencyMatrix[vb][va]);
            edges.add(new Edge<>(getVertex(keyVertex2), getVertex(keyVertex1), weight));
        }
    }

    /**
     * Deletes the edge between the vertices with the specified keys from the graph.
     *
     * @param keyVertex1 the key of the first vertex
     * @param keyVertex2 the key of the second vertex
     * @throws GraphException if the edge does not exist between the specified vertices
     */
    @Override
    public void deleteEdge(K keyVertex1, K keyVertex2) throws GraphException {
        verifyExistence(keyVertex1, keyVertex2);
        int va = vertexNumber(keyVertex1);
        int vb = vertexNumber(keyVertex2);
        if (adjacencyMatrix[va][vb].size() > 0) {
            adjacencyMatrix[va][vb].remove(0);
            edges.removeIf(edge -> edge.start().getKey().compareTo(keyVertex1) == 0 && edge.destination().getKey().compareTo(keyVertex2) == 0);
            if (!isDirected) {
                adjacencyMatrix[vb][va].remove(0);
                edges.removeIf(edge -> edge.start().getKey().compareTo(keyVertex2) == 0 && edge.destination().getKey().compareTo(keyVertex1) == 0);
            }
        }
    }

    /**
     * Checks whether there is an edge between the vertices with the specified keys.
     *
     * @param keyVertex1 the key of the first vertex
     * @param keyVertex2 the key of the second vertex
     * @return true if there is an edge between the vertices, false otherwise
     * @throws GraphException if either of the vertices does not exist in the graph
     */
    @Override
    public boolean adjacent(K keyVertex1, K keyVertex2) throws GraphException {
        verifyExistence(keyVertex1, keyVertex2);
        return adjacencyMatrix[vertexNumber(keyVertex1)][vertexNumber(keyVertex2)].size() > 0;
    }

    /**
     * Performs a Breadth-First Search starting from the vertex with the specified key.
     *
     * @param keyVertex the key of the starting vertex
     */
    @Override
    public void BFS(K keyVertex) {
        for (Vertex<K, E> u : vertexes.values()) {
            u.setColor(Color.WHITE);
            u.setDistance(Integer.MAX_VALUE);
            u.setPredecessor(null);
        }
        Vertex<K, E> s = vertexes.get(keyVertex);
        s.setColor(Color.GRAY);
        s.setDistance(0);
        Queue<Vertex<K, E>> queue = new LinkedList<>();
        queue.offer(s);
        while (!queue.isEmpty()) {
            Vertex<K, E> u = queue.poll();
            for (Vertex<K, E> vertex : vertexes.values()) {
                if (adjacent(u.getKey(), vertex.getKey()) && vertex.getColor() == Color.WHITE) {
                    vertex.setColor(Color.GRAY);
                    vertex.setDistance(u.getDistance() + 1);
                    vertex.setPredecessor(u);
                    queue.offer(vertex);
                }
            }
            u.setColor(Color.BLACK);
        }
    }

    /**
     * Verifies the existence of the specified vertices in the graph.
     *
     * @param keyVertex1 the key of the first vertex
     * @param keyVertex2 the key of the second vertex
     * @throws GraphException if either of the vertices does not exist in the graph
     */
    private void verifyExistence(K keyVertex1, K keyVertex2) throws GraphException {
        Vertex<K, E> v1 = vertexes.get(keyVertex1);
        Vertex<K, E> v2 = vertexes.get(keyVertex2);
        if (v1 == null || v2 == null) throw new GraphException("Vertex not found");
    }

    /**
     * Finds the shortest paths from a specified source vertex to all other vertices in the graph using Dijkstra's algorithm.
     *
     * @param keyVertexSource the key or identifier of the source vertex
     * @return an ArrayList representing the shortest paths from the source vertex to all other vertices
     * @throws GraphException if the source vertex does not exist in the graph
     */
    @Override
    public ArrayList<Integer> dijkstra(K keyVertexSource) {
        if (vertexes.get(keyVertexSource) == null) {
            throw new GraphException("Source vertex not found.");
        }
        for (Vertex<K, E> vertex : vertexes.values()) {
            if (vertex.getKey().compareTo(keyVertexSource) != 0)
                vertex.setDistance(Integer.MAX_VALUE);
            vertex.setPredecessor(null);
        }
        vertexes.get(keyVertexSource).setDistance(0);
        PriorityQueue<Vertex<K, E>> priorityQueue = new PriorityQueue<>(Comparator.comparing(Vertex<K, E>::getDistance));
        for (Vertex<K, E> vertex : vertexes.values()) {
            priorityQueue.offer(vertex);
        }
        while (!priorityQueue.isEmpty()) {
            Vertex<K, E> u = priorityQueue.poll();
            for (Vertex<K, E> vertex : vertexes.values()) {
                if (adjacent(u.getKey(), vertex.getKey())) {
                    int alt = u.getDistance() + adjacencyMatrix[vertexNumber(u.getKey())][vertexNumber(vertex.getKey())].get(0);
                    if (alt < vertex.getDistance()) {
                        vertex.setDistance(alt);
                        vertex.setPredecessor(u);
                        priorityQueue.offer(vertex);
                    }
                }
            }
        }
        return vertexes.values().stream().map(Vertex::getDistance).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Method which returns the vertex number corresponding to a given vertex key.
     *
     * @param keyVertex the key of the vertex
     * @return the vertex number corresponding to the key, or -1 if the key does not exist in the graph.
     */
    private int vertexNumber(K keyVertex) {
        Integer index = vertexesIndex.get(keyVertex);
        return index == null ? -1 : index;
    }

    /**
     * Applies Kruskal's algorithm to find the minimum spanning tree of the graph.
     *
     * @return the list of edges in the minimum spanning tree.
     */
    @Override
    public ArrayList<Edge<K, E>> kruskal() {
        ArrayList<Edge<K, E>> A = new ArrayList<>();
        UnionFind unionFind = new UnionFind(maxVertexes);
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
