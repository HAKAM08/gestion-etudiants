package com.monapp.service.impl;

import com.monapp.dao.GroupeDao;
import com.monapp.dao.impl.JdbcGroupeDao;
import com.monapp.model.Groupe;
import com.monapp.service.GroupeService;
import com.monapp.util.DBConnectionPool;

import java.util.List;

public class GroupeServiceImpl implements GroupeService {

    private final GroupeDao dao =
            new JdbcGroupeDao(DBConnectionPool.getDataSource());

    @Override
    public List<Groupe> getAll() {
        return dao.findAll();
    }

    @Override
    public List<Groupe> getByAnnee(int anneeId) {
        return dao.findByAnnee(anneeId);
    }

    @Override
    public void add(Groupe g) {
        dao.create(g);
    }

    @Override
    public void update(Groupe g) {
        dao.update(g);
    }

    @Override
    public void delete(int id) {
        dao.delete(id);
    }

    @Override
    public Groupe getById(int groupeId) {
        return dao.findById(groupeId);
    }
}
