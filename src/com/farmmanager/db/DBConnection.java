package com.farmmanager.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class DBConnection {
    private static final String SERVER = "MSI";
    private static final String PORT = "1433";
    private static final String DATABASE = "FarmManager";
    private static final String USER = "farmapp";
    private static final String PASSWORD = "kamipahlawan";

    private static final String URL =
        "jdbc:sqlserver://" + SERVER + ":" + PORT +
        ";databaseName=" + DATABASE +
        ";encrypt=false;trustServerCertificate=true";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver SQL Server tidak ditemukan!", e);
        }
    }

    public static boolean testConnection() {
        try (Connection c = getConnection()) {
            return c != null && !c.isClosed();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Koneksi database gagal!\n\n" + e.getMessage() +
                "\n\nPeriksa konfigurasi di DBConnection.java",
                "Error Koneksi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}