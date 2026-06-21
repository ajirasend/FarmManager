package com.farmmanager.dao;

import com.farmmanager.db.DBConnection;
import com.farmmanager.model.Karyawan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class KaryawanDAO {
    private Karyawan map(ResultSet rs) throws SQLException {
        return new Karyawan(
            rs.getInt("karyawan_id"),
            rs.getString("nama_karyawan"),
            rs.getString("no_telepon"),
            rs.getString("alamat"),
            rs.getString("tanggal_mulai"),
            rs.getBoolean("status_aktif"),
            rs.getString("peran"),
            rs.getInt("farm_id"),
            rs.getString("nama_farm")
        );
    }

    public List<Karyawan> getAll() throws SQLException {
        List<Karyawan> list = new ArrayList<>();
        String sql =
            "SELECT k.karyawan_id, k.nama_karyawan, k.no_telepon, k.alamat, " +
            "CONVERT(VARCHAR, k.tanggal_mulai, 23) AS tanggal_mulai, " +
            "k.status_aktif, k.peran, k.farm_id, f.nama_farm " +
            "FROM KARYAWAN k JOIN FARM f ON k.farm_id = f.farm_id " +
            "ORDER BY k.karyawan_id";

        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(map(rs));
            }
        }

        return list;
    }

    public List<Karyawan> search(String keyword) throws SQLException {
        List<Karyawan> list = new ArrayList<>();
        String sql =
            "SELECT k.karyawan_id, k.nama_karyawan, k.no_telepon, k.alamat, " +
            "CONVERT(VARCHAR, k.tanggal_mulai, 23) AS tanggal_mulai, " +
            "k.status_aktif, k.peran, k.farm_id, f.nama_farm " +
            "FROM KARYAWAN k JOIN FARM f ON k.farm_id = f.farm_id " +
            "WHERE k.nama_karyawan LIKE ? " +
            "OR k.no_telepon LIKE ? " +
            "OR k.alamat LIKE ? " +
            "OR k.peran LIKE ? " +
            "OR f.nama_farm LIKE ? " +
            "OR CAST(k.karyawan_id AS VARCHAR) LIKE ? " +
            "ORDER BY k.karyawan_id";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            String q = "%" + keyword + "%";
            ps.setString(1, q);
            ps.setString(2, q);
            ps.setString(3, q);
            ps.setString(4, q);
            ps.setString(5, q);
            ps.setString(6, q);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        }

        return list;
    }

    public void insert(Karyawan k) throws SQLException {
        String sql =
            "INSERT INTO KARYAWAN " +
            "(nama_karyawan, no_telepon, alamat, tanggal_mulai, status_aktif, peran, farm_id) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, k.getNamaKaryawan());
            ps.setString(2, k.getNoTelepon());
            ps.setString(3, k.getAlamat());
            ps.setString(4, k.getTanggalMulai());
            ps.setBoolean(5, k.isStatusAktif());
            ps.setString(6, k.getPeran());
            ps.setInt(7, k.getFarmId());
            ps.executeUpdate();
        }
    }

    public void nonaktifkan(int karyawanId) throws SQLException {
        String sql = "UPDATE KARYAWAN SET status_aktif = 0 WHERE karyawan_id=?";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, karyawanId);
            ps.executeUpdate();
        }
    }

    public void delete(int karyawanId) throws SQLException {
        try (Connection c = DBConnection.getConnection()) {
            boolean aktif = isAktif(c, karyawanId);
            boolean punyaPanen = hasPanenTelur(c, karyawanId);

            if (aktif && punyaPanen) {
                throw new SQLException("Karyawan ini masih memiliki data panen telur.\nNonaktifkan dulu sebelum dihapus.");
            }

            c.setAutoCommit(false);

            try {
                try (PreparedStatement ps = c.prepareStatement(
                        "DELETE FROM DETAIL_PANEN_TELUR WHERE panen_id IN " +
                        "(SELECT panen_id FROM PANEN_TELUR WHERE karyawan_id=?)")) {
                    ps.setInt(1, karyawanId);
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = c.prepareStatement(
                        "DELETE FROM PANEN_TELUR WHERE karyawan_id=?")) {
                    ps.setInt(1, karyawanId);
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = c.prepareStatement(
                        "DELETE FROM KARYAWAN WHERE karyawan_id=?")) {
                    ps.setInt(1, karyawanId);
                    ps.executeUpdate();
                }

                c.commit();
            } catch (SQLException e) {
                c.rollback();
                throw e;
            }
        }
    }

    private boolean isAktif(Connection c, int karyawanId) throws SQLException {
        String sql = "SELECT status_aktif FROM KARYAWAN WHERE karyawan_id=?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, karyawanId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getBoolean("status_aktif");
            }
        }
    }

    private boolean hasPanenTelur(Connection c, int karyawanId) throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM PANEN_TELUR WHERE karyawan_id=?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, karyawanId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt("total") > 0;
            }
        }
    }
}