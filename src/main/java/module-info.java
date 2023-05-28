module com.example.epidemicsolution {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    exports com.example.epidemicsolution;
    exports com.example.epidemicsolution.control;
    opens com.example.epidemicsolution to javafx.fxml;
    opens com.example.epidemicsolution.control to javafx.fxml;
    opens com.example.epidemicsolution.controller to com.google.gson;

}