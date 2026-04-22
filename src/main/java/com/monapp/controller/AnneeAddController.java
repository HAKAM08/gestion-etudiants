package com.monapp.controller;

import com.monapp.dao.impl.JdbcAnneeDao;
import com.monapp.model.Admin;
import com.monapp.service.AnneeService;
import com.monapp.service.impl.AnneeServiceImpl;
import com.monapp.util.DBConnectionPool;
import com.monapp.util.SceneNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AnneeAddController {

    @FXML private TextField nomField;

    private final AnneeService service = new AnneeServiceImpl(
            new JdbcAnneeDao(DBConnectionPool.getDataSource())
    )
            ;
    private Admin admin;

    public void initData(Admin admin) {
        this.admin = admin;
    }

    @FXML
    private void onSave() {
        String nom = nomField.getText().trim();
        if (!nom.isEmpty()) {
            service.add(nom);
        }
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
