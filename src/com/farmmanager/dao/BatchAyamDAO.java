package com.farmmanager.dao;

import com.farmmanager.db.DBConnection;
import com.farmmanager.model.BatchAyam;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BatchAyamDAO {
    private static final String SELECT_BASE =
        "SELECT b.batch_id, b.kode_batch, CONVERT(VARCHAR,b.tanggal_masuk,23) AS tanggal_masuk, " +
        "b.jumlah_awal, b.jumlah_hidup, b.status_batch, b.kandang_id, b.jenis_ayam_id, k.kode_kandang AS nama_kandang " +
        "FROM BATCH_AYAM b JOIN KANDANG k ON b.kandang_id = k.kandang_id ";

    private BatchAyam map(ResultSet rs) throws SQLException {
        BatchAyam b = new BatchAyam();
        b.setBatchId(rs.getInt("batch_id"));
        b.setKodeBatch(rs.getString("kode_batch"));
        b.setTanggalMasuk(rs.getString("tanggal_masuk"));
        b.setJumlahAwal(rs.getInt("jumlah_awal"));
        b.setJumlahHidup(rs.getInt("jumlah_hidup"));
        b.setStatusBatch(rs.getString("status_batch"));
        b.setKandangId(rs.getInt("kandang_id"));
        b.setNamaKandang(rs.getString("nama_kandang"));
        b.setJenisAyamId(rs.getInt("jenis_ayam_id"));
        return b;
    }

    public List<BatchAyam> getAll() throws SQLException {
        List<BatchAyam> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(SELECT_BASE + "ORDER BY b.tanggal_masuk DESC")) {
            while (rs.next()) {
                list.add(map(rs));
            }
        }
        return list;
    }

    public List<BatchAyam> search(String keyword) throws SQLException {
        List<BatchAyam> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_BASE + "WHERE b.kode_batch LIKE ? ORDER BY b.tanggal_masuk DESC")) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        }
        return list;
    }

    public void insert(BatchAyam b) throws SQLException {
        String sql = "INSERT INTO BATCH_AYAM (kode_batch, tanggal_masuk, jumlah_awal, jumlah_hidup, status_batch, kandang_id, jenis_ayam_id) VALUES (?,?,?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, b.getKodeBatch());
            ps.setString(2, b.getTanggalMasuk());
            ps.setInt(3, b.getJumlahAwal());
            ps.setInt(4, b.getJumlahHidup());
            ps.setString(5, b.getStatusBatch());
            ps.setInt(6, b.getKandangId());
            ps.setInt(7, b.getJenisAyamId());
            ps.executeUpdate();
        }
    }

    public void update(BatchAyam b) throws SQLException {
        String sql = "UPDATE BATCH_AYAM SET kode_batch=?, tanggal_masuk=?, jumlah_awal=?, jumlah_hidup=?, status_batch=?, kandang_id=?, jenis_ayam_id=? WHERE batch_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, b.getKodeBatch());
            ps.setString(2, b.getTanggalMasuk());
            ps.setInt(3, b.getJumlahAwal());
            ps.setInt(4, b.getJumlahHidup());
            ps.setString(5, b.getStatusBatch());
            ps.setInt(6, b.getKandangId());
            ps.setInt(7, b.getJenisAyamId());
            ps.setInt(8, b.getBatchId());
            ps.executeUpdate();
        }
    }

    public void delete(int batchId) throws SQLException {
        String sql = "DELETE FROM BATCH_AYAM WHERE batch_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, batchId);
            ps.executeUpdate();
        }
    }
}
