package com.farmmanager.ui;

import com.farmmanager.dao.FarmDAO;
import com.farmmanager.model.Farm;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FarmPanel extends JPanel {
    private final FarmDAO dao = new FarmDAO();
    private DefaultTableModel model;
    private JTable table;
    private JTextField txtSearch;
    private List<Farm> currentFarms;

    private static final String[] COLS = {"ID", "Nama Farm", "Lokasi", "Alamat"};

    public FarmPanel() {
        setLayout(new BorderLayout(0, 0));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
        JButton btnTambah = new JButton("+ Tambah Farm");
        btnTambah.setBackground(new Color(24, 95, 165));
        btnTambah.setForeground(Color.WHITE);
        btnTambah.setOpaque(true);
        btnTambah.setContentAreaFilled(true);
        btnTambah.setBorderPainted(false);
        btnTambah.setFocusPainted(false);
        btnTambah.setPreferredSize(new Dimension(130, 28));
        JButton btnEdit = new JButton("✏ Edit");
        JButton btnHapus = new JButton("🗑 Hapus");
        btnHapus.setForeground(new Color(163, 45, 45));
        JButton btnRefresh = new JButton("↻ Refresh");

        txtSearch = new JTextField(20);
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)));

        toolbar.add(btnTambah);
        toolbar.add(btnEdit);
        toolbar.add(btnHapus);
        toolbar.add(btnRefresh);
        toolbar.add(new JLabel("  Cari:"));
        toolbar.add(txtSearch);

        model = new DefaultTableModel(COLS, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        table.setFont(new Font("SansSerif", Font.PLAIN, 12));
        table.setSelectionBackground(new Color(210, 230, 255));
        table.getColumnModel().getColumn(0).setMaxWidth(50);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        add(toolbar, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        btnTambah.addActionListener(e -> showDialog(null));
        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Pilih data dulu!");
                return;
            }
            int id = (int) model.getValueAt(row, 0);
            Farm f = getCurrentFarms().stream().filter(x -> x.getFarmId() == id).findFirst().orElse(null);
            if (f != null) {
                showDialog(f);
            }
        });
        btnHapus.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Pilih data dulu!");
                return;
            }
            int id = (int) model.getValueAt(row, 0);
            String nama = (String) model.getValueAt(row, 1);
            int confirm = JOptionPane.showConfirmDialog(this,
                "Hapus farm \"" + nama + "\"?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    dao.delete(id);
                    loadData();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Gagal hapus:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnRefresh.addActionListener(e -> loadData());
        txtSearch.addActionListener(e -> searchData());

        loadData();
    }

    private List<Farm> getCurrentFarms() {
        if (currentFarms == null) {
            loadData();
        }
        return currentFarms;
    }

    private void loadData() {
        try {
            currentFarms = dao.getAll();
            refreshTable(currentFarms);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal load data:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchData() {
        try {
            currentFarms = dao.search(txtSearch.getText());
            refreshTable(currentFarms);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal cari:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshTable(List<Farm> list) {
        model.setRowCount(0);
        for (Farm f : list) {
            model.addRow(new Object[]{f.getFarmId(), f.getNamaFarm(), f.getLokasi(), f.getTelepon()});
        }
    }

    private void showDialog(Farm existing) {
        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
            existing == null ? "Tambah Farm" : "Edit Farm", true);
        dlg.setSize(380, 250);
        dlg.setLocationRelativeTo(this);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 4, 4, 4);

        JTextField fNama = new JTextField(existing != null ? existing.getNamaFarm() : "");
        JTextField fLokasi = new JTextField(existing != null ? existing.getLokasi() : "");
        JTextField fTelp = new JTextField(existing != null ? existing.getTelepon() : "");

        String[] labels = {"Nama Farm *", "Lokasi", "Alamat"};
        JTextField[] inputs = {fNama, fLokasi, fTelp};
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0;
            form.add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1; gbc.weightx = 1;
            form.add(inputs[i], gbc);
        }

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Simpan");
        btnSave.setBackground(new Color(24, 95, 165));
        btnSave.setForeground(Color.WHITE);
        JButton btnCancel = new JButton("Batal");
        btnPanel.add(btnCancel);
        btnPanel.add(btnSave);

        btnSave.addActionListener(e -> {
            if (fNama.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Nama Farm wajib diisi!");
                return;
            }
            Farm f = existing != null ? existing : new Farm();
            f.setNamaFarm(fNama.getText().trim());
            f.setLokasi(fLokasi.getText().trim());
            f.setTelepon(fTelp.getText().trim());
            try {
                if (existing == null) {
                    dao.insert(f);
                } else {
                    dao.update(f);
                }
                dlg.dispose();
                loadData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dlg, "Gagal simpan:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        btnCancel.addActionListener(e -> dlg.dispose());

        dlg.setLayout(new BorderLayout());
        dlg.add(form, BorderLayout.CENTER);
        dlg.add(btnPanel, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }
}
