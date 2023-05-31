package com.example.epidemicsolution.controller;

import com.example.epidemicsolution.dataStructures.graph.Edge;
import com.example.epidemicsolution.dataStructures.graph.GraphType;
import com.example.epidemicsolution.dataStructures.graph.IGraph;
import com.example.epidemicsolution.dataStructures.graph.graphAdjacencyList.GraphAdjacencyList;
import com.example.epidemicsolution.dataStructures.graph.graphAdjacencyMatrix.GraphAdjacencyMatrix;
import com.google.gson.Gson;
import javafx.scene.Cursor;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Map {

	private static Map instance;
	private IGraph<Integer, City> map;
	private HashMap<Integer, RadioButton> radioButtons;
	private HashMap<Integer, Line> lines;

	private Map(Implementation implementation) {
		map = (implementation == Implementation.LIST) ? new GraphAdjacencyList<>(GraphType.SIMPLE) : new GraphAdjacencyMatrix<>(GraphType.SIMPLE);
		radioButtons = new HashMap<>();
		lines = new HashMap<>();
		loadCity();
		loadRoute();
	}

	public static Map getInstance(Implementation implementation) {
		if (instance == null) instance = new Map(implementation);
		return instance;
	}

	public void loadCity() {
		Gson gson = new Gson();
		File projectDir = new File(System.getProperty("user.dir"));
		File dataDirectory = new File(projectDir + "\\data");
		File citiesInformation = new File(dataDirectory + "\\cities.json");
		if (!dataDirectory.exists()) {
			dataDirectory.mkdir();
		}
		try {
			if (!citiesInformation.exists()) {
				citiesInformation.createNewFile();
			}
			String json = new String(java.nio.file.Files.readAllBytes(citiesInformation.toPath()));
			City[] citiesJson = gson.fromJson(json, City[].class);
			for (City city : citiesJson) {
				map.insertVertex(city.getId(), city);
				// Create and configure the RadioButton
				RadioButton radioButton = new RadioButton();
				// Set the position of the RadioButton using the "x" and "y" coordinates
				radioButton.setLayoutX(city.getX());
				radioButton.setLayoutY(city.getY());
				radioButton.setCursor(Cursor.HAND);
				// Text hover with the name of the city
				Tooltip tooltip = new Tooltip(city.getCity());
				Tooltip.install(radioButton, tooltip);
				// Add the RadioButton to the ObservableList
				radioButtons.put(city.getId(), radioButton);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadRoute() {
		Gson gson = new Gson();
		File projectDir = new File(System.getProperty("user.dir"));
		File dataDirectory = new File(projectDir + "\\data");
		File routesInformation = new File(dataDirectory + "\\routes.json");
		if (!dataDirectory.exists()) {
			dataDirectory.mkdir();
		}
		try {
			if (!routesInformation.exists()) {
				routesInformation.createNewFile();
			}
			String json = new String(java.nio.file.Files.readAllBytes(routesInformation.toPath()));
			Route[] routesJson = gson.fromJson(json, Route[].class);
			for (Route route : routesJson) {
				map.insertEdge(route.getCity1(), route.getCity2(), route.getSuppliesUsed());
				// Get the information about the route
				RadioButton city1 = radioButtons.get(route.getCity1());
				RadioButton city2 = radioButtons.get(route.getCity2());
				// Create and configure the Line
				Line line = new Line(city1.getLayoutX() + 8, city1.getLayoutY() + 8, city2.getLayoutX() + 8, city2.getLayoutY() + 8);
				// Set hover text on the line
				line.setCursor(Cursor.HAND);
				String tooltipText = String.valueOf(route.getSuppliesUsed());
				Tooltip tooltip = new Tooltip(tooltipText);
				Tooltip.install(line, tooltip);
				// Set line color
				line.setStroke(Color.WHITE);
				// Set line thickness
				line.setStrokeWidth(1.5);
				// Add the Line to the ObservableList
				lines.put(route.getCity1() + route.getCity2(), line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getNameCity(RadioButton r) {
		return map.getVertex((int) r.getLayoutX() * (int) r.getLayoutY()).getElement().getCity();
	}

	public boolean updateRoute(RadioButton r1, RadioButton r2, int suppliesUsed) {
		// Get the id of each city
		int idCity1 = (int) r1.getLayoutX() * (int) r1.getLayoutY();
		int idCity2 = (int) r2.getLayoutX() * (int) r2.getLayoutY();
		// Check that they are adjacent vertices
		if (map.adjacent(idCity1, idCity2)) {
			// Update the suppliesUsed value of the route between the cities
			map.deleteEdge(idCity1, idCity2);
			map.insertEdge(idCity1, idCity2, suppliesUsed);
			// Update the tooltip of the line with the new value of suppliesUsed
			Line line = lines.get(idCity1 + idCity2);
			String tooltipText = String.valueOf(suppliesUsed);
			Tooltip tooltip = new Tooltip(tooltipText);
			Tooltip.install(line, tooltip);
			return true;
		}
		return false;
	}

	public int distanceBetweenTwoCities(RadioButton r1, RadioButton r2) {
		// Obtain the id of each city
		int idCity1 = (int) r1.getLayoutX() * (int) r1.getLayoutY();
		int idCity2 = (int) r2.getLayoutX() * (int) r2.getLayoutY();
		// Used BFS
		map.BFS(idCity1);
		// Return the distance between the city2 and the city1
		return map.getVertex(idCity2).getDistance() - 1;
	}

	public void evacuationRoute(RadioButton r1, RadioButton r2) {
		// Obtain the id of each city
		int idCity1 = (int) r1.getLayoutX() * (int) r1.getLayoutY();
		int idCity2 = (int) r2.getLayoutX() * (int) r2.getLayoutY();
		// Used Dijkstra
		map.dijkstra(idCity1);
		// Color the shortest path between cities in white
		int currentVertex = idCity2;
		while (map.getVertex(currentVertex).getPredecessor() != null) {
			int predecessorVertex = map.getVertex(currentVertex).getPredecessor().getKey();
			int edgeKey = currentVertex + predecessorVertex;
			Line line = lines.get(edgeKey);
			if (line != null) {
				line.setStrokeWidth(1.5); // Set line thickness
				line.setStroke(Color.BLACK); // Set the stroke color to white
			}
			currentVertex = predecessorVertex;
		}
	}

	public void logisticPlanning() {
		// Used Kruskal
		ArrayList<Edge<Integer, City>> kruskalResult = map.kruskal();
		// Color the Kruskal path
		for (Edge<Integer, City> edge : kruskalResult) {
			int city1 = edge.start().getKey();
			int city2 = edge.destination().getKey();
			int edgeKey = city1 + city2;
			Line line = lines.get(edgeKey);
			if (line != null) {
				line.setStrokeWidth(1.5); // Set the thickness of the line
				line.setStroke(Color.BLACK); // Set the stroke color to white
			}
		}
	}

	public void resetColorLines() {
		for (Line line : lines.values()) {
			line.setStroke(Color.WHITE);
			line.setStrokeWidth(1.5);
		}
	}

	public HashMap<Integer, RadioButton> getRadioButtons() {
		return radioButtons;
	}

	public void setRadioButtons(HashMap<Integer, RadioButton> radioButtons) {
		this.radioButtons = radioButtons;
	}

	public HashMap<Integer, Line> getLines() {
		return lines;
	}

	public void setLines(HashMap<Integer, Line> lines) {
		this.lines = lines;
	}

}