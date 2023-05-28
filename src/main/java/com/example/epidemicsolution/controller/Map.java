package com.example.epidemicsolution.controller;

import com.example.epidemicsolution.dataStructures.graph.GraphType;
import com.example.epidemicsolution.dataStructures.graph.IGraph;
import com.example.epidemicsolution.dataStructures.graph.graphAdjacencyList.GraphAdjacencyList;
import com.example.epidemicsolution.dataStructures.graph.graphAdjacencyMatrix.GraphAdjacencyMatrix;
import com.google.gson.Gson;
import javafx.scene.control.RadioButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.io.File;
import java.io.IOException;
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
        if (instance == null) {
            instance = new Map(implementation);
        }
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
                // Add the Line to the ObservableList
                lines.put(route.getCity1() + route.getCity2(), line);
            }
        } catch (IOException e) {
            e.printStackTrace();
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
