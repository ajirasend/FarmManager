package com.farmmanager.dao;

import com.farmmanager.db.DBConnection;
import com.farmmanager.model.Kandang;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class KandangDAO {
    private static final String SELECT_BASE =
        "SELECT k.kandang_id, k.kode_kandang AS nama_kandang, k.kapasitas_maksimal, k.tipe_kandang, k.farm_id, f.nama_farm " +
        "FROM KANDANG k JOIN FARM f ON k.farm_id = f.farm_id ";

    private Kandang map(ResultSet rs) throws SQLException {
        return new Kandang(
            rs.getInt("kandang_id"),
            rs.getString("nama_kandang"),
            rs.getInt("kapasitas_maksimal"),
            rs.getString("tipe_kandang"),
            rs.getInt("farm_id"),
            rs.getString("nama_farm")
        );
    }

    public List<Kandang> getAll() throws SQLException {
        List<Kandang> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(SELECT_BASE + "ORDER BY k.kode_kandang")) {
            while (rs.next()) {
                list.add(map(rs));
            }
        }
        return list;
    }

    public List<Kandang> search(String keyword) throws SQLException {
        List<Kandang> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_BASE + "WHERE k.kode_kandang LIKE ? ORDER BY k.kode_kandang")) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        }
        return list;
    }

    public void insert(Kandang k) throws SQLException {
        String sql = "INSERT INTO KANDANG (kode_kandang, kapasitas_maksimal, tipe_kandang, farm_id) VALUES (?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, k.getNamaKandang());
            ps.setInt(2, k.getKapasitasMaksimal());
            ps.setString(3, k.getTipeKandang());
            ps.setInt(4, k.getFarmId());
            ps.executeUpdate();
        }
    }

    public void update(Kandang k) throws SQLException {
        String sql = "UPDATE KANDANG SET kode_kandang=?, kapasitas_maksimal=?, tipe_kandang=?, farm_id=? WHERE kandang_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, k.getNamaKandang());
            ps.setInt(2, k.getKapasitasMaksimal());
            ps.setString(3, k.getTipeKandang());
            ps.setInt(4, k.getFarmId());
            ps.setInt(5, k.getKandangId());
            ps.executeUpdate();
        }
    }

    public void delete(int kandangId) throws SQLException {
        String sql = "DELETE FROM KANDANG WHERE kandang_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, kandangId);
            ps.executeUpdate();
        }
    }
}
