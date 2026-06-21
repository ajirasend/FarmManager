package com.farmmanager.dao;

import com.farmmanager.db.DBConnection;
import com.farmmanager.model.Farm;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FarmDAO {
    public List<Farm> getAll() throws SQLException {
        List<Farm> list = new ArrayList<>();
        String sql = "SELECT farm_id, nama_farm, lokasi, alamat FROM FARM ORDER BY nama_farm";
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Farm(
                    rs.getInt("farm_id"),
                    rs.getString("nama_farm"),
                    rs.getString("lokasi"),
                    rs.getString("alamat")
                ));
            }
        }
        return list;
    }

    public List<Farm> search(String keyword) throws SQLException {
        List<Farm> list = new ArrayList<>();
        String sql = "SELECT farm_id, nama_farm, lokasi, alamat FROM FARM WHERE nama_farm LIKE ? ORDER BY nama_farm";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Farm(
                        rs.getInt("farm_id"),
                        rs.getString("nama_farm"),
                        rs.getString("lokasi"),
                        rs.getString("alamat")
                    ));
                }
            }
        }
        return list;
    }

    public void insert(Farm f) throws SQLException {
        String sql = "INSERT INTO FARM (nama_farm, lokasi, alamat) VALUES (?, ?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, f.getNamaFarm());
            ps.setString(2, f.getLokasi());
            ps.setString(3, f.getTelepon());
            ps.executeUpdate();
        }
    }

    public void update(Farm f) throws SQLException {
        String sql = "UPDATE FARM SET nama_farm=?, lokasi=?, alamat=? WHERE farm_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, f.getNamaFarm());
            ps.setString(2, f.getLokasi());
            ps.setString(3, f.getTelepon());
            ps.setInt(4, f.getFarmId());
            ps.executeUpdate();
        }
    }

    public void delete(int farmId) throws SQLException {
        String sql = "DELETE FROM FARM WHERE farm_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, farmId);
            ps.executeUpdate();
        }
    }
}
