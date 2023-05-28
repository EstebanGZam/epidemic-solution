module com.example.epidemicsolution {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens com.example.epidemicsolution to javafx.fxml;
    exports com.example.epidemicsolution;
}