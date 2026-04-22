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

public class EtudiantEditController {

    @FXML private TextField matriculeField;
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private ComboBox<Annee> anneeBox;
    @FXML private ComboBox<Groupe> groupeBox;

    private Etudiant etudiant;

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

        anneeBox.setOnAction(e -> {
            Annee a = anneeBox.getValue();
            if (a != null) {
                groupeBox.getItems()
                        .setAll(groupeService.getByAnnee(a.getId()));
            }
        });
    }

    public void initData(Etudiant e) {
        this.etudiant = e;

        matriculeField.setText(e.getMatricule());
        nomField.setText(e.getNom());
        prenomField.setText(e.getPrenom());
        emailField.setText(e.getEmail());

        anneeBox.getItems().stream()
                .filter(a -> a.getId() == e.getAnneeId())
                .findFirst()
                .ifPresent(a -> {
                    anneeBox.setValue(a);
                    groupeBox.getItems()
                            .setAll(groupeService.getByAnnee(a.getId()));
                });

        groupeBox.getItems().stream()
                .filter(g -> g.getId() == e.getGroupeId())
                .findFirst()
                .ifPresent(groupeBox::setValue);
    }

    @FXML
    private void onUpdate() {
        etudiant.setMatricule(matriculeField.getText());
        etudiant.setNom(nomField.getText());
        etudiant.setPrenom(prenomField.getText());
        etudiant.setEmail(emailField.getText());
        etudiant.setAnneeId(anneeBox.getValue().getId());
        etudiant.setGroupeId(groupeBox.getValue().getId());

        etudiantService.update(etudiant);

        SceneNavigator.go(
                matriculeField,
                "/fxml/etudiant_list.fxml",
                ctrl -> {}
        );
    }

    @FXML
    private void onDelete() {
        etudiantService.delete(etudiant.getId());

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
