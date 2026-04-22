package com.monapp.controller;

import com.monapp.model.Admin;
import com.monapp.util.SceneNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class DashboardController {

    @FXML private Label welcomeLabel;

    @FXML private Button btnAnnees;
    @FXML private Button btnGroupes;
    @FXML private Button btnEtudiants;

    private Admin admin;

    /**
     * Reçoit l'admin connecté depuis LoginController
     */
    public void initData(Admin admin) {
        this.admin = admin;

        if (admin != null) {
            welcomeLabel.setText("Bienvenue, " + admin.getFullName());
        } else {
            welcomeLabel.setText("Bienvenue");
        }
    }



    @FXML
    private void goToAnnees() {
        SceneNavigator.go(
                btnAnnees,
                "/fxml/annee_list.fxml",
                c -> ((AnneeListController) c).initData(admin)
        );
    }

    @FXML
    private void goToGroupes() {
        SceneNavigator.go(
                btnGroupes,
                "/fxml/groupe_list.fxml",
                c -> ((GroupeListController) c).initData(admin)
        );
    }

    @FXML
    private void goToEtudiants() {
        SceneNavigator.go(
                btnEtudiants,
                "/fxml/etudiant_list.fxml",
                c -> ((EtudiantListController) c).initData(admin)
        );
    }



    @FXML
    private void onLogout() {
        SceneNavigator.go(
                btnAnnees,
                "/fxml/login.fxml",
                c -> {}
        );
    }
}
