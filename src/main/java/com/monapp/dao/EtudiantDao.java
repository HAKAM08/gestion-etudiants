package com.monapp.dao;

import com.monapp.model.Etudiant;
import java.util.List;

public interface EtudiantDao {

    List<Etudiant> findAll();
    void create(Etudiant e);
    void update(Etudiant e);
    void delete(int id);
}
