package com.farmmanager.dao;

import com.farmmanager.db.DBConnection;
import com.farmmanager.model.PanenTelur;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PanenTelurDAO {
    private static final String SELECT_BASE =
        "SELECT pt.panen_id, CONVERT(VARCHAR,pt.tanggal_panen,23) AS tanggal_panen, " +
        "pt.kandang_id, k.kode_kandang AS nama_kandang, pt.karyawan_id, kar.nama_karyawan, " +
        "SUM(dpt.jumlah_butir) AS total_butir, SUM(dpt.berat_total_kg) AS total_berat " +
        "FROM PANEN_TELUR pt " +
        "JOIN KANDANG k ON pt.kandang_id = k.kandang_id " +
        "JOIN KARYAWAN kar ON pt.karyawan_id = kar.karyawan_id " +
        "JOIN DETAIL_PANEN_TELUR dpt ON pt.panen_id = dpt.panen_id ";

    private static final String GROUP_BY =
        "GROUP BY pt.panen_id, pt.tanggal_panen, pt.kandang_id, k.kode_kandang, pt.karyawan_id, kar.nama_karyawan ";

    private PanenTelur map(ResultSet rs) throws SQLException {
        PanenTelur p = new PanenTelur();
        p.setPanenId(rs.getInt("panen_id"));
        p.setTanggalPanen(rs.getString("tanggal_panen"));
        p.setKandangId(rs.getInt("kandang_id"));
        p.setNamaKandang(rs.getString("nama_kandang"));
        p.setKaryawanId(rs.getInt("karyawan_id"));
        p.setNamaKaryawan(rs.getString("nama_karyawan"));
        try {
            p.setCatatan(rs.getString("catatan"));
        } catch (SQLException ignored) {
            p.setCatatan(null);
        }
        p.setTotalButir(rs.getInt("total_butir"));
        p.setTotalBeratKg(rs.getDouble("total_berat"));
        return p;
    }

    public List<PanenTelur> getAll() throws SQLException {
        List<PanenTelur> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(SELECT_BASE + GROUP_BY + "ORDER BY pt.tanggal_panen DESC")) {
            while (rs.next()) {
                list.add(map(rs));
            }
        }
        return list;
    }

    public List<PanenTelur> search(String keyword) throws SQLException {
        List<PanenTelur> list = new ArrayList<>();
        String sql = SELECT_BASE + "WHERE k.kode_kandang LIKE ? " + GROUP_BY + "ORDER BY pt.tanggal_panen DESC";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        }
        return list;
    }

    public void insert(PanenTelur p, int jumlahButir, double beratKg, int gradeId) throws SQLException {
        try (Connection c = DBConnection.getConnection()) {
            c.setAutoCommit(false);
            try {
                int panenId;
                try (PreparedStatement ps = c.prepareStatement(
                        "INSERT INTO PANEN_TELUR (tanggal_panen, kandang_id, batch_id, karyawan_id, catatan) VALUES (?,?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, p.getTanggalPanen());
                    ps.setInt(2, p.getKandangId());
                    ps.setInt(3, p.getBatchId());
                    ps.setInt(4, p.getKaryawanId());
                    ps.setString(5, p.getCatatan());
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (!keys.next()) {
                            throw new SQLException("Gagal mengambil ID panen baru.");
                        }
                        panenId = keys.getInt(1);
                    }
                }
                try (PreparedStatement ps2 = c.prepareStatement(
                        "INSERT INTO DETAIL_PANEN_TELUR (panen_id, grade_id, jumlah_butir, berat_total_kg) VALUES (?,?,?,?)")) {
                    ps2.setInt(1, panenId);
                    ps2.setInt(2, gradeId);
                    ps2.setInt(3, jumlahButir);
                    ps2.setDouble(4, beratKg);
                    ps2.executeUpdate();
                }
                c.commit();
            } catch (SQLException e) {
                c.rollback();
                throw e;
            }
        }
    }

    public void delete(int panenId) throws SQLException {
        try (Connection c = DBConnection.getConnection()) {
            c.setAutoCommit(false);
            try {
                try (PreparedStatement ps = c.prepareStatement("DELETE FROM DETAIL_PANEN_TELUR WHERE panen_id=?")) {
                    ps.setInt(1, panenId);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = c.prepareStatement("DELETE FROM PANEN_TELUR WHERE panen_id=?")) {
                    ps.setInt(1, panenId);
                    ps.executeUpdate();
                }
                c.commit();
            } catch (SQLException e) {
                c.rollback();
                throw e;
            }
        }
    }
}
