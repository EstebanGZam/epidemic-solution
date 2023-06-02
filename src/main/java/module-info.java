module com.example.epidemicsolution {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    exports com.example.epidemicsolution;
    exports com.example.epidemicsolution.controller;
    opens com.example.epidemicsolution to javafx.fxml;
    opens com.example.epidemicsolution.model to com.google.gson;
    opens com.example.epidemicsolution.controller to com.google.gson, javafx.fxml;

}