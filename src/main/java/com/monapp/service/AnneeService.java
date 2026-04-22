package com.monapp.service;

import com.monapp.model.Annee;
import java.util.List;

public interface AnneeService {
    List<Annee> getAll();
    void add(String nom);
    void update(int id, String nom);
    void delete(int id);

    Annee getById(int anneeId);
}
