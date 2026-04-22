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

public class GroupeEditController {

    @FXML private TextField nomField;
    @FXML private ComboBox<Annee> anneeBox;

    private Groupe groupe;

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

    public void initData(Groupe g) {
        this.groupe = g;
        nomField.setText(g.getNom());

        anneeBox.getItems().stream()
                .filter(a -> a.getId() == g.getAnneeId())
                .findFirst()
                .ifPresent(anneeBox::setValue);
    }

    @FXML
    private void onUpdate() {
        groupe.setNom(nomField.getText());
        groupe.setAnneeId(anneeBox.getValue().getId());

        groupeService.update(groupe);

        SceneNavigator.go(
                nomField,
                "/fxml/groupe_list.fxml",
                ctrl -> {}
        );
    }

    @FXML
    private void onDelete() {
        groupeService.delete(groupe.getId());

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
