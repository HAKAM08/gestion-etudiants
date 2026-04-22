package com.monapp.controller;

import com.monapp.dao.impl.JdbcAnneeDao;
import com.monapp.model.Admin;
import com.monapp.model.Annee;
import com.monapp.model.Etudiant;
import com.monapp.model.Groupe;
import com.monapp.service.AnneeService;
import com.monapp.service.EtudiantService;
import com.monapp.service.GroupeService;
import com.monapp.service.impl.AnneeServiceImpl;
import com.monapp.service.impl.EtudiantServiceImpl;
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

public class EtudiantListController {

    // ===== Table =====
    @FXML
    private TableView<Etudiant> table;
    @FXML
    private TableColumn<Etudiant, String> nomCol;
    @FXML
    private TableColumn<Etudiant, String> prenomCol;
    @FXML
    private TableColumn<Etudiant, String> anneeCol;
    @FXML
    private TableColumn<Etudiant, String> groupeCol;
    @FXML
    private TableColumn<Etudiant, Void> actionCol;

    // ===== Search and Filter Controls =====
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> anneeFilter;
    @FXML
    private ComboBox<String> groupeFilter;
    @FXML
    private Button btnReset;

    // ===== Buttons =====
    @FXML
    private Button btnAdd;

    // ===== Services =====
    private final EtudiantService etudiantService = new EtudiantServiceImpl();
    private final GroupeService groupeService = new GroupeServiceImpl();
    private final AnneeService anneeService = new AnneeServiceImpl(
            new JdbcAnneeDao(DBConnectionPool.getDataSource()));

    // ===== Cache (performance) =====
    private final Map<Integer, String> anneeCache = new HashMap<>();
    private final Map<Integer, String> groupeCache = new HashMap<>();

    // ===== Admin connecté =====
    private Admin admin;

    // ===== Filtered and Sorted Lists =====
    private ObservableList<Etudiant> allEtudiants;
    private FilteredList<Etudiant> filteredData;

    // ===== appelé depuis DashboardController =====
    public void initData(Admin admin) {
        this.admin = admin;
    }

    // ===== Initialisation =====
    @FXML
    public void initialize() {

        // --- Nom ---
        nomCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNom()));

        // --- Prénom ---
        prenomCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPrenom()));

        // --- Année ---
        anneeCol.setCellValueFactory(c -> {
            int anneeId = c.getValue().getAnneeId();

            if (!anneeCache.containsKey(anneeId)) {
                Annee a = anneeService.getById(anneeId);
                anneeCache.put(anneeId, a != null ? a.getNom() : "-");
            }

            return new SimpleStringProperty(anneeCache.get(anneeId));
        });

        // --- Groupe ---
        groupeCol.setCellValueFactory(c -> {
            int groupeId = c.getValue().getGroupeId();

            if (!groupeCache.containsKey(groupeId)) {
                Groupe g = groupeService.getById(groupeId);
                groupeCache.put(groupeId, g != null ? g.getNom() : "-");
            }

            return new SimpleStringProperty(groupeCache.get(groupeId));
        });

        // --- Actions ---
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
                    Etudiant etu = getTableView()
                            .getItems()
                            .get(getIndex());

                    SceneNavigator.go(
                            table,
                            "/fxml/etudiant_edit.fxml",
                            ctrl -> ((EtudiantEditController) ctrl).initData(etu));
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        // --- Load data and setup filtering ---
        allEtudiants = FXCollections.observableArrayList(etudiantService.getAll());
        filteredData = new FilteredList<>(allEtudiants, p -> true);

        // Wrap the FilteredList in a SortedList
        SortedList<Etudiant> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);

        // --- Populate filter ComboBoxes ---
        setupFilterComboBoxes();

        // --- Setup search and filter listeners ---
        searchField.textProperty().addListener((observable, oldValue, newValue) -> applyFilters());
        anneeFilter.valueProperty().addListener((observable, oldValue, newValue) -> applyFilters());
        groupeFilter.valueProperty().addListener((observable, oldValue, newValue) -> applyFilters());
    }

    // ===== Setup Filter ComboBoxes =====
    private void setupFilterComboBoxes() {
        // Populate Année filter
        ObservableList<String> annees = FXCollections.observableArrayList("Toutes les années");
        anneeService.getAll().forEach(a -> annees.add(a.getNom()));
        anneeFilter.setItems(annees);
        anneeFilter.setValue("Toutes les années");

        // Populate Groupe filter
        ObservableList<String> groupes = FXCollections.observableArrayList("Tous les groupes");
        groupeService.getAll().forEach(g -> groupes.add(g.getNom()));
        groupeFilter.setItems(groupes);
        groupeFilter.setValue("Tous les groupes");
    }

    // ===== Apply Filters =====
    private void applyFilters() {
        filteredData.setPredicate(etudiant -> {
            // Search filter
            String searchText = searchField.getText();
            boolean matchesSearch = true;
            if (searchText != null && !searchText.trim().isEmpty()) {
                String lowerCaseFilter = searchText.toLowerCase();
                matchesSearch = etudiant.getNom().toLowerCase().contains(lowerCaseFilter)
                        || etudiant.getPrenom().toLowerCase().contains(lowerCaseFilter);
            }

            // Année filter
            String selectedAnnee = anneeFilter.getValue();
            boolean matchesAnnee = true;
            if (selectedAnnee != null && !selectedAnnee.equals("Toutes les années")) {
                String etudiantAnnee = anneeCache.get(etudiant.getAnneeId());
                if (etudiantAnnee == null) {
                    Annee a = anneeService.getById(etudiant.getAnneeId());
                    etudiantAnnee = a != null ? a.getNom() : "-";
                    anneeCache.put(etudiant.getAnneeId(), etudiantAnnee);
                }
                matchesAnnee = etudiantAnnee.equals(selectedAnnee);
            }

            // Groupe filter
            String selectedGroupe = groupeFilter.getValue();
            boolean matchesGroupe = true;
            if (selectedGroupe != null && !selectedGroupe.equals("Tous les groupes")) {
                String etudiantGroupe = groupeCache.get(etudiant.getGroupeId());
                if (etudiantGroupe == null) {
                    Groupe g = groupeService.getById(etudiant.getGroupeId());
                    etudiantGroupe = g != null ? g.getNom() : "-";
                    groupeCache.put(etudiant.getGroupeId(), etudiantGroupe);
                }
                matchesGroupe = etudiantGroupe.equals(selectedGroupe);
            }

            return matchesSearch && matchesAnnee && matchesGroupe;
        });
    }

    // ===== Reset Filters =====
    @FXML
    private void resetFilters() {
        searchField.clear();
        anneeFilter.setValue("Toutes les années");
        groupeFilter.setValue("Tous les groupes");
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
                "/fxml/etudiant_add.fxml",
                ctrl -> {
                });
    }
}
