package com.farmmanager.dao;

import com.farmmanager.db.DBConnection;
import com.farmmanager.model.GradeTelur;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GradeTelurDAO {
    public List<GradeTelur> getAll() throws SQLException {
        List<GradeTelur> list = new ArrayList<>();
        String sql = "SELECT grade_id, nama_grade, deskripsi, harga_standar FROM GRADE_TELUR ORDER BY grade_id";

        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new GradeTelur(
                    rs.getInt("grade_id"),
                    rs.getString("nama_grade"),
                    rs.getString("deskripsi"),
                    rs.getDouble("harga_standar")
                ));
            }
        }

        return list;
    }
}