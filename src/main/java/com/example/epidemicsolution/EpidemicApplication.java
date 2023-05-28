package com.example.epidemicsolution;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class EpidemicApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        openWindow("init-view.fxml");
    }

    public static void openWindow(String fxml) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(EpidemicApplication.class.getResource(fxml));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Epidemic");
            stage.getIcons().add(new Image("file:" + EpidemicApplication.class.getResource("image/logo.png").getPath()));
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }

}