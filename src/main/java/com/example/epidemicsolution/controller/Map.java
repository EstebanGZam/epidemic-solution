package com.example.epidemicsolution.controller;

import com.example.epidemicsolution.dataStructures.graph.GraphType;
import com.example.epidemicsolution.dataStructures.graph.IGraph;
import com.example.epidemicsolution.dataStructures.graph.graphAdjacencyList.GraphAdjacencyList;
import com.example.epidemicsolution.dataStructures.graph.graphAdjacencyMatrix.GraphAdjacencyMatrix;
import com.google.gson.Gson;
import javafx.scene.Group;
import javafx.scene.control.RadioButton;

import java.io.File;
import java.io.IOException;

public class Map {

    private static Map instance;
    private IGraph<Integer, City> map;
    private Group radioButtonGroup = new Group(); // Create a group to store the RadioButtons

    private Map(Implementation implementation) {
        map = (implementation == Implementation.LIST) ? new GraphAdjacencyList<>(GraphType.SIMPLE) : new GraphAdjacencyMatrix<>(GraphType.SIMPLE);
        loadCity();
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
                // Add the RadioButton to the Group
                radioButtonGroup.getChildren().add(radioButton);
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
                //
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Group getRadioButtonGroup() {
        return radioButtonGroup;
    }

    public void setRadioButtonGroup(Group radioButtonGroup) {
        this.radioButtonGroup = radioButtonGroup;
    }
}
