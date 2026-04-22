package com.monapp.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class SceneNavigator {

    public static void go(Node node, String fxml, Consumer<Object> init) {
        try {
            FXMLLoader loader =
                    new FXMLLoader(SceneNavigator.class.getResource(fxml));
            Scene scene = new Scene(loader.load());

            init.accept(loader.getController());

            Stage stage = (Stage) node.getScene().getWindow();
            stage.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
