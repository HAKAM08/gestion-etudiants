package com.monapp.service.impl;

import com.monapp.dao.AnneeDao;
import com.monapp.model.Annee;
import com.monapp.service.AnneeService;

import java.util.List;

public class AnneeServiceImpl implements AnneeService {

    private final AnneeDao dao;

    public AnneeServiceImpl(AnneeDao dao) {
        this.dao = dao;
    }

    @Override
    public List<Annee> getAll() {
        return dao.findAll();
    }

    @Override
    public void add(String nom) {
        dao.insert(nom);
    }

    @Override
    public void update(int id, String nom) {
        dao.update(id, nom);
    }

    @Override
    public void delete(int id) {
        dao.delete(id);
    }

    @Override
    public Annee getById(int id) {
        return dao.findById(id);   // ✅ clean delegation
    }
}
