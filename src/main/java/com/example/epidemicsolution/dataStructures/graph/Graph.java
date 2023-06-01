package com.example.epidemicsolution.dataStructures.graph;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * The {@code Graph} class represents an abstract graph data structure.
 * <p>
 * It implements the {@code IGraph} interface.
 *
 * @param <K> the type of the key used in the graph vertices, must extend {@code Comparable}
 * @param <E> the type of the data stored in the graph edges
 */

public abstract class Graph<K extends Comparable<K>, E> implements IGraph<K, E> {

    protected LinkedList<Edge<K, E>> edges; // List of edges in the graph.
    protected int time, currentVertexNumber; // The current time value used for graph operations.
    protected final boolean isDirected, multipleEdges, loops; // Specifies whether the graph is directed.

    protected final HashMap<K, Integer> vertexesIndex;

    /**
     * Constructs a new graph of the specified type.
     *
     * @param graphType the type of the graph
     */
    protected Graph(GraphType graphType) {
        edges = new LinkedList<>();
        this.vertexesIndex = new HashMap<>();
        currentVertexNumber = 0;
        isDirected = switch (graphType) {
            case SIMPLE, MULTIGRAPH, PSEUDOGRAPH -> false;
            case DIRECTED, MULTIGRAPH_DIRECTED -> true;
        };
        multipleEdges = switch (graphType) {
            case SIMPLE, DIRECTED -> false;
            case MULTIGRAPH, PSEUDOGRAPH, MULTIGRAPH_DIRECTED -> true;
        };
        loops = switch (graphType) {
            case SIMPLE, MULTIGRAPH -> false;
            case DIRECTED, PSEUDOGRAPH, MULTIGRAPH_DIRECTED -> true;
        };
    }

}
