package com.monapp.service;

import com.monapp.model.Groupe;
import java.util.List;

public interface GroupeService {

    List<Groupe> getAll();
    List<Groupe> getByAnnee(int anneeId);
    void add(Groupe g);
    void update(Groupe g);
    void delete(int id);

    Groupe getById(int groupeId);
}
