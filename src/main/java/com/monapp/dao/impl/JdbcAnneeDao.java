package com.monapp.dao.impl;

import com.monapp.dao.AnneeDao;
import com.monapp.model.Annee;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JdbcAnneeDao implements AnneeDao {

    private final DataSource ds;

    public JdbcAnneeDao(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public List<Annee> findAll() {
        List<Annee> list = new ArrayList<>();
        String sql = "SELECT id, nom, created_at FROM annee ORDER BY id";

        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                LocalDateTime createdAt =
                        rs.getTimestamp("created_at").toLocalDateTime();

                list.add(new Annee(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        createdAt
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void insert(String nom) {
        String sql = "INSERT INTO annee(nom) VALUES (?)";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nom);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(int id, String nom) {
        String sql = "UPDATE annee SET nom=? WHERE id=?";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nom);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM annee WHERE id=?";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Annee findById(int id) {
        String sql = "SELECT id, nom, created_at FROM annee WHERE id = ?";

        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Annee a = new Annee();
                    a.setId(rs.getInt("id"));
                    a.setNom(rs.getString("nom"));
                    a.setCreatedAt(
                            rs.getTimestamp("created_at").toLocalDateTime()
                    );
                    return a;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
