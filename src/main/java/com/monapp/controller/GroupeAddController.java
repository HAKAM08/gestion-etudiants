package com.monapp.controller;

import com.monapp.dao.impl.JdbcAnneeDao;
import com.monapp.model.Annee;
import com.monapp.model.Groupe;
import com.monapp.service.AnneeService;
import com.monapp.service.GroupeService;
import com.monapp.service.impl.AnneeServiceImpl;
import com.monapp.service.impl.GroupeServiceImpl;
import com.monapp.util.DBConnectionPool;
import com.monapp.util.SceneNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class GroupeAddController {

    @FXML private TextField nomField;
    @FXML private ComboBox<Annee> anneeBox;

    private final GroupeService groupeService = new GroupeServiceImpl();
    private final AnneeService anneeService = new AnneeServiceImpl(new JdbcAnneeDao(DBConnectionPool.getDataSource()));

    @FXML
    public void initialize() {
        anneeBox.getItems().setAll(anneeService.getAll());
        anneeBox.setCellFactory(c ->
                new javafx.scene.control.ListCell<>() {
                    @Override
                    protected void updateItem(Annee a, boolean empty) {
                        super.updateItem(a, empty);
                        setText(empty || a == null ? null : a.getNom());
                    }
                });
        anneeBox.setButtonCell(anneeBox.getCellFactory().call(null));
    }

    @FXML
    private void onSave() {
        if (nomField.getText().isEmpty() || anneeBox.getValue() == null)
            return;

        Groupe g = new Groupe();
        g.setNom(nomField.getText());
        g.setAnneeId(anneeBox.getValue().getId());

        groupeService.add(g);

        SceneNavigator.go(
                nomField,
                "/fxml/groupe_list.fxml",
                ctrl -> {}
        );
    }

    @FXML
    private void onBack() {
        SceneNavigator.go(
                nomField,
                "/fxml/groupe_list.fxml",
                ctrl -> {}
        );
    }
}
