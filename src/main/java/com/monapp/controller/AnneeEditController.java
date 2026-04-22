package com.monapp.controller;

import com.monapp.dao.impl.JdbcAnneeDao;
import com.monapp.model.Admin;
import com.monapp.model.Annee;
import com.monapp.service.AnneeService;
import com.monapp.service.impl.AnneeServiceImpl;
import com.monapp.util.DBConnectionPool;
import com.monapp.util.SceneNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AnneeEditController {

    @FXML private TextField nomField;

    private final AnneeService service = new AnneeServiceImpl(
            new JdbcAnneeDao(DBConnectionPool.getDataSource())
    )
            ;
    private Admin admin;
    private Annee annee;

    public void initData(Admin admin, Annee annee) {
        this.admin = admin;
        this.annee = annee;
        nomField.setText(annee.getNom());
    }

    @FXML
    private void onUpdate() {
        String nom = nomField.getText().trim();
        if (!nom.isEmpty()) {
            service.update(annee.getId(), nom);
        }
        onBack();
    }

    @FXML
    private void onDelete() {
        service.delete(annee.getId());
        onBack();
    }

    @FXML
    private void onBack() {
        SceneNavigator.go(
                nomField,
                "/fxml/annee_list.fxml",
                c -> ((AnneeListController) c).initData(admin)
        );
    }
}
