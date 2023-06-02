module com.example.epidemicsolution {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	requires com.google.gson;

	exports com.example.epidemicsolution;
	exports com.example.epidemicsolution.controller;
	exports com.example.epidemicsolution.model;
	opens com.example.epidemicsolution to javafx.fxml;
	opens com.example.epidemicsolution.model to com.google.gson;
	opens com.example.epidemicsolution.controller to com.google.gson, javafx.fxml;

}