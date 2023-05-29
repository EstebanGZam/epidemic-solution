package com.example.epidemicsolution.control;

import com.example.epidemicsolution.controller.Map;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class EpidemicController implements Initializable {

    private final Map map = Map.getInstance(null);
    private int selectedCount = 0; // Count of selected radio buttons
    private RadioButton selectedRadioButton1;
    private RadioButton selectedRadioButton2;

    @FXML
    private AnchorPane mainScreen;

    @FXML
    private TextField city1;

    @FXML
    private TextField city2;

    @FXML
    private TextField suppliesUsedTf;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Add the Lines to the AnchorPane
        mainScreen.getChildren().addAll(map.getLines().values());
        // Add the RadioButton to the AnchorPane
        mainScreen.getChildren().addAll(map.getRadioButtons().values());
        // Set the event for each radio button
        map.getRadioButtons().values().forEach(radioButton ->
                radioButton.setOnAction(this::threadRadioButton));
        // Disable text fields
        city1.setEditable(false);
        city2.setEditable(false);
    }

    @FXML
    void updateRoute(ActionEvent event) {
        if (selectedRadioButton1 == null || selectedRadioButton2 == null) {
            showAlert("Please select two cities.");
            return;
        }
        if (suppliesUsedTf.getText() == null) {
            showAlert("Please enter a number of medical supplements.");
            return;
        }
        try {
            int suppliesUsed = Integer.parseInt(suppliesUsedTf.getText());
            boolean updateRoute = map.updateRoute(selectedRadioButton1, selectedRadioButton2, suppliesUsed);
            if (!updateRoute) {
                showAlert("Please select two adjacent cities.");
            }
        } catch (NumberFormatException ex) {
            showAlert("Please enter a number of medical supplements.");
        }
    }

    @FXML
    void distanceBetweenTwoCities(ActionEvent event) {
        if (selectedRadioButton1 != null && selectedRadioButton2 != null) {
            int distanceBetweenTwoCities = map.distanceBetweenTwoCities(selectedRadioButton1, selectedRadioButton2);
            System.out.println(distanceBetweenTwoCities);
        } else {
            showAlert("Please select two cities.");
        }
    }

    private void threadRadioButton(ActionEvent event) {
        RadioButton selectedRadioButton = (RadioButton) event.getSource();
        Thread thread = new Thread(() -> {
            if (selectedRadioButton.isSelected()) {
                if (selectedCount < 2) {
                    selectedCount++;
                    if (selectedRadioButton1 == null) {
                        selectedRadioButton1 = selectedRadioButton;
                        city1.setText(map.getNameCity(selectedRadioButton)); // Set the city selected to the text field
                    } else if (selectedRadioButton2 == null) {
                        selectedRadioButton2 = selectedRadioButton;
                        city2.setText(map.getNameCity(selectedRadioButton)); // Set the city selected to the text field
                    }
                } else {
                    selectedRadioButton.setSelected(false); // Deselect the radio button
                }
            } else {
                selectedCount--;
                if (selectedRadioButton1 == selectedRadioButton) {
                    selectedRadioButton1 = null;
                    city1.setText(null);
                } else if (selectedRadioButton2 == selectedRadioButton) {
                    selectedRadioButton2 = null;
                    city2.setText(null);
                }
            }
        });
        thread.start(); // Start the thread to perform the operations in the background
    }

    private void showAlert(String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Error");
        alert.setTitle("Alert");
        alert.setContentText(contentText);
        alert.showAndWait();
    }

}