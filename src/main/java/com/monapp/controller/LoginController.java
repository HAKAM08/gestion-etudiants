package com.monapp.controller;

import com.monapp.model.Admin;
import com.monapp.service.AuthService;
import com.monapp.service.impl.AuthServiceImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    private final AuthService authService = new AuthServiceImpl();

    @FXML
    private void onLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Remplis tous les champs");
            return;
        }

        Admin admin = authService.login(username, password);

        if (admin == null) {
            messageLabel.setText("Identifiants incorrects");
            return;
        }

        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/fxml/dashboard.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) usernameField.getScene().getWindow();

            DashboardController controller = loader.getController();
            controller.initData(admin);

            stage.setScene(scene);
            stage.setTitle("Dashboard Admin");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Erreur lors du chargement");
        }
    }
}
