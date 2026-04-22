package com.monapp.controller;

import com.monapp.dao.impl.JdbcAnneeDao;
import com.monapp.model.Admin;
import com.monapp.model.Annee;
import com.monapp.model.Groupe;
import com.monapp.service.AnneeService;
import com.monapp.service.GroupeService;
import com.monapp.service.impl.AnneeServiceImpl;
import com.monapp.service.impl.GroupeServiceImpl;
import com.monapp.util.DBConnectionPool;
import com.monapp.util.SceneNavigator;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.HashMap;
import java.util.Map;

public class GroupeListController {

    @FXML
    private TableView<Groupe> table;
    @FXML
    private TableColumn<Groupe, String> nomCol;
    @FXML
    private TableColumn<Groupe, String> anneeCol;
    @FXML
    private TableColumn<Groupe, Void> actionCol;
    @FXML
    private Button btnAdd;

    // ===== Search and Filter Controls =====
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> anneeFilter;
    @FXML
    private Button btnReset;

    private final GroupeService groupeService = new GroupeServiceImpl();
    private final AnneeService anneeService = new AnneeServiceImpl(
            new JdbcAnneeDao(DBConnectionPool.getDataSource()));

    private Admin admin;

    // cache pour éviter appels DB répétitifs
    private final Map<Integer, String> anneeCache = new HashMap<>();

    // ===== Filtered and Sorted Lists =====
    private ObservableList<Groupe> allGroupes;
    private FilteredList<Groupe> filteredData;

    // ===== appelé depuis DashboardController =====
    public void initData(Admin admin) {
        this.admin = admin;
    }

    @FXML
    public void initialize() {

        // ===== Nom du groupe =====
        nomCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNom()));

        // ===== Année (via anneeId) =====
        anneeCol.setCellValueFactory(c -> {
            int anneeId = c.getValue().getAnneeId();

            if (!anneeCache.containsKey(anneeId)) {
                Annee a = anneeService.getById(anneeId);
                anneeCache.put(anneeId, a != null ? a.getNom() : "-");
            }

            return new SimpleStringProperty(anneeCache.get(anneeId));
        });

        // ===== Actions =====
        actionCol.setCellFactory(col -> new TableCell<>() {

            private final Button btn = new Button("✏ Modifier");

            {
                btn.setStyle("""
                        -fx-background-color:#3b82f6;
                        -fx-text-fill:white;
                        -fx-font-size:13;
                        -fx-font-weight:600;
                        -fx-padding:6 14;
                        -fx-background-radius:6;
                        """);

                btn.setOnAction(e -> {
                    Groupe g = getTableView()
                            .getItems()
                            .get(getIndex());

                    SceneNavigator.go(
                            table,
                            "/fxml/groupe_edit.fxml",
                            ctrl -> ((GroupeEditController) ctrl).initData(g));
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        // ===== Charger les données et setup filtering =====
        allGroupes = FXCollections.observableArrayList(groupeService.getAll());
        filteredData = new FilteredList<>(allGroupes, p -> true);

        // Wrap the FilteredList in a SortedList
        SortedList<Groupe> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);

        // --- Populate filter ComboBox ---
        setupFilterComboBox();

        // --- Setup search and filter listeners ---
        searchField.textProperty().addListener((observable, oldValue, newValue) -> applyFilters());
        anneeFilter.valueProperty().addListener((observable, oldValue, newValue) -> applyFilters());
    }

    // ===== Setup Filter ComboBox =====
    private void setupFilterComboBox() {
        ObservableList<String> annees = FXCollections.observableArrayList("Toutes les années");
        anneeService.getAll().forEach(a -> annees.add(a.getNom()));
        anneeFilter.setItems(annees);
        anneeFilter.setValue("Toutes les années");
    }

    // ===== Apply Filters =====
    private void applyFilters() {
        filteredData.setPredicate(groupe -> {
            // Search filter
            String searchText = searchField.getText();
            boolean matchesSearch = true;
            if (searchText != null && !searchText.trim().isEmpty()) {
                String lowerCaseFilter = searchText.toLowerCase();
                matchesSearch = groupe.getNom().toLowerCase().contains(lowerCaseFilter);
            }

            // Année filter
            String selectedAnnee = anneeFilter.getValue();
            boolean matchesAnnee = true;
            if (selectedAnnee != null && !selectedAnnee.equals("Toutes les années")) {
                String groupeAnnee = anneeCache.get(groupe.getAnneeId());
                if (groupeAnnee == null) {
                    Annee a = anneeService.getById(groupe.getAnneeId());
                    groupeAnnee = a != null ? a.getNom() : "-";
                    anneeCache.put(groupe.getAnneeId(), groupeAnnee);
                }
                matchesAnnee = groupeAnnee.equals(selectedAnnee);
            }

            return matchesSearch && matchesAnnee;
        });
    }

    // ===== Reset Filters =====
    @FXML
    private void resetFilters() {
        searchField.clear();
        anneeFilter.setValue("Toutes les années");
    }

    // ===== Navigation =====
    @FXML
    private void goToDashboard() {
        SceneNavigator.go(
                table,
                "/fxml/dashboard.fxml",
                c -> ((DashboardController) c).initData(admin));
    }

    @FXML
    private void goToAdd() {
        SceneNavigator.go(
                table,
                "/fxml/groupe_add.fxml",
                ctrl -> {
                });
    }
}
