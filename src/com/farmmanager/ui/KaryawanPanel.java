package com.farmmanager.ui;

import com.farmmanager.dao.FarmDAO;
import com.farmmanager.dao.KaryawanDAO;
import com.farmmanager.model.Farm;
import com.farmmanager.model.Karyawan;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class KaryawanPanel extends JPanel {
    private final KaryawanDAO dao = new KaryawanDAO();
    private final FarmDAO farmDAO = new FarmDAO();
    private DefaultTableModel model;
    private JTable table;
    private JTextField txtSearch;
    private List<Karyawan> currentData;

    private static final String[] COLS = {
        "ID", "Nama Karyawan", "No Telepon", "Alamat", "Tanggal Mulai", "Status", "Peran", "Farm"
    };

    public KaryawanPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));

        JButton btnTambah = new JButton("+ Tambah Karyawan");
        btnTambah.setBackground(new Color(24, 95, 165));
        btnTambah.setForeground(Color.WHITE);
        btnTambah.setOpaque(true);
        btnTambah.setContentAreaFilled(true);
        btnTambah.setBorderPainted(false);
        btnTambah.setFocusPainted(false);
        btnTambah.setPreferredSize(new Dimension(155, 28));

        JButton btnNonaktif = new JButton("Nonaktifkan");

        JButton btnHapus = new JButton("🗑 Hapus");
        btnHapus.setForeground(new Color(163, 45, 45));

        JButton btnRefresh = new JButton("↻ Refresh");

        txtSearch = new JTextField(20);
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));

        toolbar.add(btnTambah);
        toolbar.add(btnNonaktif);
        toolbar.add(btnHapus);
        toolbar.add(btnRefresh);
        toolbar.add(new JLabel("  Cari:"));
        toolbar.add(txtSearch);

        model = new DefaultTableModel(COLS, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
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

        btnTambah.addActionListener(e -> showDialog());
        btnNonaktif.addActionListener(e -> nonaktifkanSelected());
        btnHapus.addActionListener(e -> hapusSelected());
        btnRefresh.addActionListener(e -> loadData());
        txtSearch.addActionListener(e -> searchData());

        loadData();
    }

    private void showDialog() {
        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Tambah Karyawan", true);
        dlg.setSize(460, 360);
        dlg.setLocationRelativeTo(this);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 4, 4, 4);

        JTextField fNama = new JTextField();
        JTextField fTelepon = new JTextField();
        JTextField fAlamat = new JTextField();
        JTextField fTanggalMulai = new JTextField(LocalDate.now().toString());
        JCheckBox cbAktif = new JCheckBox("Aktif", true);
        JComboBox<String> cbPeran = new JComboBox<>(new String[]{
            "Manajer Farm", "Mandor Kandang", "Pekerja Harian"
        });
        JComboBox<Farm> cbFarm = new JComboBox<>();

        try {
            farmDAO.getAll().forEach(cbFarm::addItem);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dlg, "Gagal load farm:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        String[] labels = {
            "Nama Karyawan *", "No Telepon", "Alamat", "Tanggal Mulai *", "Status Aktif", "Peran *", "Farm *"
        };
        Component[] inputs = {
            fNama, fTelepon, fAlamat, fTanggalMulai, cbAktif, cbPeran, cbFarm
        };

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0;
            form.add(new JLabel(labels[i]), gbc);

            gbc.gridx = 1;
            gbc.weightx = 1;
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
                JOptionPane.showMessageDialog(dlg, "Nama Karyawan wajib diisi!");
                return;
            }

            if (fTanggalMulai.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Tanggal mulai wajib diisi!");
                return;
            }

            Farm selectedFarm = (Farm) cbFarm.getSelectedItem();
            if (selectedFarm == null) {
                JOptionPane.showMessageDialog(dlg, "Pilih farm dulu!");
                return;
            }

            try {
                Karyawan k = new Karyawan();
                k.setNamaKaryawan(fNama.getText().trim());
                k.setNoTelepon(fTelepon.getText().trim());
                k.setAlamat(fAlamat.getText().trim());
                k.setTanggalMulai(fTanggalMulai.getText().trim());
                k.setStatusAktif(cbAktif.isSelected());
                k.setPeran((String) cbPeran.getSelectedItem());
                k.setFarmId(selectedFarm.getFarmId());

                dao.insert(k);

                dlg.dispose();
                loadData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dlg, "Gagal simpan karyawan:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancel.addActionListener(e -> dlg.dispose());

        dlg.setLayout(new BorderLayout());
        dlg.add(form, BorderLayout.CENTER);
        dlg.add(btnPanel, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    private void nonaktifkanSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih karyawan dulu!");
            return;
        }

        int id = (int) model.getValueAt(row, 0);
        String nama = (String) model.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
            "Nonaktifkan karyawan \"" + nama + "\"?",
            "Konfirmasi Nonaktif",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                dao.nonaktifkan(id);
                loadData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal nonaktifkan karyawan:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void hapusSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih karyawan dulu!");
            return;
        }

        int id = (int) model.getValueAt(row, 0);
        String nama = (String) model.getValueAt(row, 1);
        String status = (String) model.getValueAt(row, 5);

        String pesan = "Hapus permanen karyawan \"" + nama + "\"?";
        if ("Nonaktif".equals(status)) {
            pesan += "\n\nData panen telur yang terkait dengan karyawan ini juga akan dihapus.";
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            pesan,
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                dao.delete(id);
                loadData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal hapus karyawan:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadData() {
        try {
            currentData = dao.getAll();
            refreshTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal load karyawan:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchData() {
        try {
            currentData = dao.search(txtSearch.getText());
            refreshTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal cari karyawan:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshTable() {
        model.setRowCount(0);

        for (Karyawan k : currentData) {
            model.addRow(new Object[]{
                k.getKaryawanId(),
                k.getNamaKaryawan(),
                k.getNoTelepon(),
                k.getAlamat(),
                k.getTanggalMulai(),
                k.isStatusAktif() ? "Aktif" : "Nonaktif",
                k.getPeran(),
                k.getNamaFarm()
            });
        }
    }
}