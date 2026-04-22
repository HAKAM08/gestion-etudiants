package com.monapp.service.impl;

import com.monapp.dao.AdminDao;
import com.monapp.dao.impl.JdbcAdminDao;
import com.monapp.model.Admin;
import com.monapp.service.AuthService;
import com.monapp.util.DBConnectionPool;
import org.mindrot.jbcrypt.BCrypt;

public class AuthServiceImpl implements AuthService {

    private final AdminDao adminDao;

    public AuthServiceImpl() {

        this.adminDao = new JdbcAdminDao(DBConnectionPool.getDataSource());
    }

    @Override
    public Admin login(String username, String password) {
        try {
            Admin admin = adminDao.findByCredentials(username,password);

            if (admin == null) return null;

            if (BCrypt.checkpw(password, admin.getPasswordHash())) {
                return admin;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
