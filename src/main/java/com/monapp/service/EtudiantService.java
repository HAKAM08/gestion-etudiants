package com.monapp.service;

import com.monapp.model.Etudiant;
import java.util.List;

public interface EtudiantService {

    List<Etudiant> getAll();
    void add(Etudiant e);
    void update(Etudiant e);
    void delete(int id);
}
