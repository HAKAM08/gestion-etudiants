package com.monapp.dao.impl;

import com.monapp.dao.GroupeDao;
import com.monapp.model.Annee;
import com.monapp.model.Groupe;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcGroupeDao implements GroupeDao {

    private final DataSource ds;

    public JdbcGroupeDao(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public List<Groupe> findAll() {
        List<Groupe> list = new ArrayList<>();
        String sql = "SELECT * FROM groupe";

        try (Connection c = ds.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Groupe g = new Groupe();
                g.setId(rs.getInt("id"));
                g.setAnneeId(rs.getInt("annee_id"));
                g.setNom(rs.getString("nom"));
                g.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                list.add(g);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Groupe> findByAnnee(int anneeId) {
        List<Groupe> list = new ArrayList<>();
        String sql = "SELECT * FROM groupe WHERE annee_id=?";

        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, anneeId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Groupe g = new Groupe();
                g.setId(rs.getInt("id"));
                g.setAnneeId(rs.getInt("annee_id"));
                g.setNom(rs.getString("nom"));
                g.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                list.add(g);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void create(Groupe g) {
        String sql = "INSERT INTO groupe (annee_id, nom) VALUES (?, ?)";

        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, g.getAnneeId());
            ps.setString(2, g.getNom());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Groupe g) {
        String sql = "UPDATE groupe SET nom=?, annee_id=? WHERE id=?";

        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, g.getNom());
            ps.setInt(2, g.getAnneeId());
            ps.setInt(3, g.getId());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        try (Connection c = ds.getConnection();
             PreparedStatement ps =
                     c.prepareStatement("DELETE FROM groupe WHERE id=?")) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Groupe findById(int id) {
        String sql = "SELECT id, nom, created_at FROM groupe WHERE id = ?";

        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Groupe g = new Groupe();
                    g.setId(rs.getInt("id"));
                    g.setNom(rs.getString("nom"));
                    g.setCreatedAt(
                            rs.getTimestamp("created_at").toLocalDateTime()
                    );
                    return g;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
