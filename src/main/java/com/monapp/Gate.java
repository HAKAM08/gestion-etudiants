package com.monapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Gate extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(
                FXMLLoader.load(getClass().getResource("/fxml/login.fxml"))
        );
        stage.setScene(scene);
        stage.setTitle("Gestion Étudiants");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
