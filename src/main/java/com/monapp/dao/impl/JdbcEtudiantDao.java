package com.monapp.dao.impl;

import com.monapp.dao.EtudiantDao;
import com.monapp.model.Etudiant;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcEtudiantDao implements EtudiantDao {

    private final DataSource ds;

    public JdbcEtudiantDao(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public List<Etudiant> findAll() {
        List<Etudiant> list = new ArrayList<>();
        String sql = "SELECT * FROM etudiant";

        try (Connection c = ds.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Etudiant e = new Etudiant();
                e.setId(rs.getInt("id"));
                e.setMatricule(rs.getString("matricule"));
                e.setNom(rs.getString("nom"));
                e.setPrenom(rs.getString("prenom"));
                e.setEmail(rs.getString("email"));
                e.setAnneeId(rs.getInt("annee_id"));
                e.setGroupeId(rs.getInt("groupe_id"));
                e.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                list.add(e);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public void create(Etudiant e) {
        String sql = """
            INSERT INTO etudiant 
            (matricule, nom, prenom, email, annee_id, groupe_id)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, e.getMatricule());
            ps.setString(2, e.getNom());
            ps.setString(3, e.getPrenom());
            ps.setString(4, e.getEmail());
            ps.setInt(5, e.getAnneeId());
            ps.setInt(6, e.getGroupeId());
            ps.executeUpdate();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void update(Etudiant e) {
        String sql = """
            UPDATE etudiant SET
            matricule=?, nom=?, prenom=?, email=?,
            annee_id=?, groupe_id=?
            WHERE id=?
        """;

        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, e.getMatricule());
            ps.setString(2, e.getNom());
            ps.setString(3, e.getPrenom());
            ps.setString(4, e.getEmail());
            ps.setInt(5, e.getAnneeId());
            ps.setInt(6, e.getGroupeId());
            ps.setInt(7, e.getId());
            ps.executeUpdate();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        try (Connection c = ds.getConnection();
             PreparedStatement ps =
                     c.prepareStatement("DELETE FROM etudiant WHERE id=?")) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
