package com.monapp.dao.impl;

import com.monapp.dao.AdminDao;
import com.monapp.model.Admin;

import javax.sql.DataSource;
import java.sql.*;

public class JdbcAdminDao implements AdminDao {

    private final DataSource ds;

    public JdbcAdminDao(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public Admin findByCredentials(String username , String password) {
        String sql =
                "SELECT id, username, password_hash, full_name " +
                        "FROM admin WHERE username = ?";

        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Admin admin = new Admin();
                admin.setId(rs.getInt("id"));
                admin.setUsername(rs.getString("username"));
                admin.setPasswordHash(rs.getString("password_hash"));
                admin.setFullName(rs.getString("full_name"));
                return admin;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
