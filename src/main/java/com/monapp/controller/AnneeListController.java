package com.monapp.controller;

import com.monapp.dao.impl.JdbcAnneeDao;
import com.monapp.model.Admin;
import com.monapp.model.Annee;
import com.monapp.service.AnneeService;
import com.monapp.service.impl.AnneeServiceImpl;
import com.monapp.util.DBConnectionPool;
import com.monapp.util.SceneNavigator;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class AnneeListController {

    @FXML
    private TableView<Annee> table;
    @FXML
    private TableColumn<Annee, String> nomCol;
    @FXML
    private TableColumn<Annee, Void> actionCol;

    // ===== Search Control =====
    @FXML
    private TextField searchField;

    private final AnneeService service = new AnneeServiceImpl(
            new JdbcAnneeDao(DBConnectionPool.getDataSource()));
    private Admin admin;

    // ===== Filtered and Sorted Lists =====
    private ObservableList<Annee> allAnnees;
    private FilteredList<Annee> filteredData;

    public void initData(Admin admin) {
        this.admin = admin;
        allAnnees = FXCollections.observableArrayList(service.getAll());
        filteredData = new FilteredList<>(allAnnees, p -> true);

        SortedList<Annee> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);

        // Setup search listener
        searchField.textProperty().addListener((observable, oldValue, newValue) -> applyFilter());
    }

    @FXML
    private void goToDashboard() {
        SceneNavigator.go(
                table,
                "/fxml/dashboard.fxml",
                c -> ((DashboardController) c).initData(admin));
    }

    @FXML
    public void initialize() {

        nomCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNom()));

        actionCol.setCellFactory(col -> new TableCell<>() {

            private final Button btn = new Button("✏ Modifier");

            {
                btn.setOnAction(e -> {
                    Annee annee = getTableView().getItems().get(getIndex());
                    goToEdit(annee);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    // ===== Apply Filter =====
    private void applyFilter() {
        filteredData.setPredicate(annee -> {
            String searchText = searchField.getText();
            if (searchText == null || searchText.trim().isEmpty()) {
                return true;
            }
            String lowerCaseFilter = searchText.toLowerCase();
            return annee.getNom().toLowerCase().contains(lowerCaseFilter);
        });
    }

    @FXML
    private void goBackToDashboard() {
        SceneNavigator.go(
                table,
                "/fxml/dashboard.fxml",
                c -> ((DashboardController) c).initData(admin));
    }

    @FXML
    private void goToAdd() {
        SceneNavigator.go(
                table,
                "/fxml/annee_add.fxml",
                c -> ((AnneeAddController) c).initData(admin));
    }

    private void goToEdit(Annee annee) {
        SceneNavigator.go(
                table,
                "/fxml/annee_edit.fxml",
                c -> ((AnneeEditController) c).initData(admin, annee));
    }
}
