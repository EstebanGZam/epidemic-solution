package com.example.epidemicsolution;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class EpidemicController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}