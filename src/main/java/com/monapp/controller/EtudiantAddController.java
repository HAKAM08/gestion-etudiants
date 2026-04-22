package com.monapp.controller;

import com.monapp.dao.impl.JdbcAnneeDao;
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
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class EtudiantAddController {

    @FXML private TextField matriculeField;
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private ComboBox<Annee> anneeBox;
    @FXML private ComboBox<Groupe> groupeBox;

    private final EtudiantService etudiantService = new EtudiantServiceImpl();
    private final AnneeService anneeService = new AnneeServiceImpl(new JdbcAnneeDao(DBConnectionPool.getDataSource()));
    private final GroupeService groupeService = new GroupeServiceImpl();

    @FXML
    public void initialize() {
        anneeBox.getItems().setAll(anneeService.getAll());

        anneeBox.setCellFactory(c -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Annee a, boolean empty) {
                super.updateItem(a, empty);
                setText(empty || a == null ? null : a.getNom());
            }
        });
        anneeBox.setButtonCell(anneeBox.getCellFactory().call(null));

        groupeBox.setCellFactory(c -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Groupe g, boolean empty) {
                super.updateItem(g, empty);
                setText(empty || g == null ? null : g.getNom());
            }
        });
        groupeBox.setButtonCell(groupeBox.getCellFactory().call(null));

        // quand l'année change → charger les groupes correspondants
        anneeBox.setOnAction(e -> {
            Annee a = anneeBox.getValue();
            if (a != null) {
                groupeBox.getItems()
                        .setAll(groupeService.getByAnnee(a.getId()));
            }
        });
    }

    @FXML
    private void onSave() {
        if (matriculeField.getText().isEmpty()
                || nomField.getText().isEmpty()
                || prenomField.getText().isEmpty()
                || emailField.getText().isEmpty()
                || anneeBox.getValue() == null
                || groupeBox.getValue() == null) {
            return;
        }

        Etudiant e = new Etudiant();
        e.setMatricule(matriculeField.getText());
        e.setNom(nomField.getText());
        e.setPrenom(prenomField.getText());
        e.setEmail(emailField.getText());
        e.setAnneeId(anneeBox.getValue().getId());
        e.setGroupeId(groupeBox.getValue().getId());

        etudiantService.add(e);

        SceneNavigator.go(
                matriculeField,
                "/fxml/etudiant_list.fxml",
                ctrl -> {}
        );
    }

    @FXML
    private void onBack() {
        SceneNavigator.go(
                matriculeField,
                "/fxml/etudiant_list.fxml",
                ctrl -> {}
        );
    }
}
