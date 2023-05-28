package com.example.epidemicsolution.control;

import com.example.epidemicsolution.controller.Map;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class EpidemicController implements Initializable {

    private Map map = Map.getInstance(null);

    @FXML
    private AnchorPane mainScreen;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainScreen.getChildren().add(map.getRadioButtonGroup());
    }

}