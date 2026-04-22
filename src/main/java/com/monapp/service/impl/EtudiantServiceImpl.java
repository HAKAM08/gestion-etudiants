package com.monapp.service.impl;

import com.monapp.dao.EtudiantDao;
import com.monapp.dao.impl.JdbcEtudiantDao;
import com.monapp.model.Etudiant;
import com.monapp.service.EtudiantService;
import com.monapp.util.DBConnectionPool;

import java.util.List;

public class EtudiantServiceImpl implements EtudiantService {

    private final EtudiantDao dao =
            new JdbcEtudiantDao(DBConnectionPool.getDataSource());

    @Override
    public List<Etudiant> getAll() {
        return dao.findAll();
    }

    @Override
    public void add(Etudiant e) {
        dao.create(e);
    }

    @Override
    public void update(Etudiant e) {
        dao.update(e);
    }

    @Override
    public void delete(int id) {
        dao.delete(id);
    }
}
