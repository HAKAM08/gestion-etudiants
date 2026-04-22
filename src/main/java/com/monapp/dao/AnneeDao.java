package com.monapp.dao;

import com.monapp.model.Annee;
import java.util.List;

public interface AnneeDao {
    List<Annee> findAll();
    void insert(String nom);
    void update(int id, String nom);
    void delete(int id);

    Annee findById(int id);
}
