package com.monapp.controller;

import com.monapp.dao.impl.JdbcAnneeDao;
import com.monapp.model.Admin;
import com.monapp.model.Annee;
import com.monapp.service.AnneeService;
import com.monapp.service.impl.AnneeServiceImpl;
import com.monapp.util.DBConnectionPool;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;

public class AnneeController {

    @FXML private TextField nomField;
    @FXML private TableView<Annee> table;
    @FXML private TableColumn<Annee, Integer> idCol;
    @FXML private TableColumn<Annee, String> nomCol;
    @FXML private TableColumn<Annee, String> createdAtCol;

    private AnneeService service;
    private ObservableList<Annee> data;

    // ✅ garder l'Admin
    private Admin admin;

    public void initData(Admin admin) {
        this.admin = admin;
    }

    @FXML
    public void initialize() {
        service = new AnneeServiceImpl(
                new JdbcAnneeDao(DBConnectionPool.getDataSource())
        );

        idCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleIntegerProperty(
                        c.getValue().getId()
                ).asObject()
        );

        nomCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getNom()
                )
        );

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        createdAtCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getCreatedAt().format(formatter)
                )
        );

        loadData();

        table.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, selected) -> {
                    if (selected != null) {
                        nomField.setText(selected.getNom());
                    }
                }
        );
    }

    private void loadData() {
        data = FXCollections.observableArrayList(service.getAll());
        table.setItems(data);
    }

    @FXML
    private void onAdd() {
        String nom = nomField.getText().trim();
        if (nom.isEmpty()) {
            showAlert("Erreur", "Nom obligatoire");
            return;
        }
        service.add(nom);
        nomField.clear();
        loadData();
    }

    @FXML
    private void onUpdate() {
        Annee selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Erreur", "Sélectionne une année");
            return;
        }
        service.update(selected.getId(), nomField.getText().trim());
        loadData();
    }

    @FXML
    private void onDelete() {
        Annee selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Erreur", "Sélectionne une année");
            return;
        }
        service.delete(selected.getId());
        nomField.clear();
        loadData();
    }

    @FXML
    private void onBackToDashboard() {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/fxml/dashboard.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) table.getScene().getWindow();

            DashboardController controller = loader.getController();
            controller.initData(admin); // ✅ Admin conservé

            stage.setScene(scene);
            stage.setTitle("Dashboard Admin");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
