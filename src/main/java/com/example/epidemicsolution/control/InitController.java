package com.example.epidemicsolution.control;

import com.example.epidemicsolution.EpidemicApplication;
import com.example.epidemicsolution.controller.Implementation;
import com.example.epidemicsolution.controller.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class InitController implements Initializable {

    @FXML
    private Button continueBTN;

    @FXML
    private ComboBox<Implementation> implementationCb;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Implementation> list = FXCollections.observableArrayList(Implementation.LIST, Implementation.MATRIX);
        implementationCb.setItems(list);
    }

    @FXML
    public void show(ActionEvent event) {
        Implementation implementation = implementationCb.getSelectionModel().getSelectedItem();
        if (implementation != null) {
            // Create the graph according to the selected implementation
            Map.getInstance(implementation);
            // Close the actual window and open the new window
            Stage stage = (Stage) this.continueBTN.getScene().getWindow();
            stage.close();
            EpidemicApplication.openWindow("epidemic-view.fxml");
        } else {
            // In case the user does not choose any option, an alert is displayed
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setTitle("Alert");
            alert.setContentText("Please choose an option.");
            alert.showAndWait();
        }
    }

}
