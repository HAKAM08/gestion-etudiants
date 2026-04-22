package com.monapp.dao;

import com.monapp.model.Annee;
import com.monapp.model.Groupe;
import java.util.List;

public interface GroupeDao {

    List<Groupe> findAll();
    List<Groupe> findByAnnee(int anneeId);
    void create(Groupe groupe);
    void update(Groupe groupe);
    void delete(int id);
    Groupe findById(int id);
}
