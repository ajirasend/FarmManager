package com.farmmanager.ui;

import com.farmmanager.db.DBConnection;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Farm Manager - Sistem Manajemen Peternakan Ayam");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setMinimumSize(new Dimension(900, 550));
        setLocationRelativeTo(null);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(24, 95, 165));
        header.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        JLabel title = new JLabel("🐔  Farm Manager");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Sistem Informasi Peternakan Ayam & Telur");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitle.setForeground(new Color(200, 220, 255));

        JPanel titlePanel = new JPanel(new BorderLayout(0, 2));
        titlePanel.setOpaque(false);
        titlePanel.add(title, BorderLayout.NORTH);
        titlePanel.add(subtitle, BorderLayout.SOUTH);
        header.add(titlePanel, BorderLayout.WEST);

        JLabel dbStatus = new JLabel("● Terhubung ke SQL Server");
        dbStatus.setFont(new Font("SansSerif", Font.PLAIN, 11));
        dbStatus.setForeground(new Color(150, 255, 180));
        header.add(dbStatus, BorderLayout.EAST);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabs.addTab("🏠  Farm", new FarmPanel());
        tabs.addTab("🏗  Kandang", new KandangPanel());
        tabs.addTab("👤  Karyawan", new KaryawanPanel());
        tabs.addTab("🐣  Batch Ayam", new BatchAyamPanel());
        tabs.addTab("🥚  Panen Telur", new PanenTelurPanel());

        add(header, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);

        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        statusBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
        statusBar.setBackground(new Color(245, 245, 245));
        JLabel statusLabel = new JLabel("Siap | Database: FarmDB | SQL Server");
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        statusLabel.setForeground(Color.GRAY);
        statusBar.add(statusLabel);
        add(statusBar, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // gunakan default
        }

        SwingUtilities.invokeLater(() -> {
            if (!DBConnection.testConnection()) {
                int opt = JOptionPane.showConfirmDialog(null,
                    "Koneksi database gagal.\nTetap buka aplikasi (mode offline)?",
                    "Koneksi Gagal", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (opt != JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
            new MainFrame().setVisible(true);
        });
    }
}
