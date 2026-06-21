package com.farmmanager.ui;

import com.farmmanager.dao.FarmDAO;
import com.farmmanager.dao.KandangDAO;
import com.farmmanager.model.Farm;
import com.farmmanager.model.Kandang;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class KandangPanel extends JPanel {
    private final KandangDAO dao = new KandangDAO();
    private final FarmDAO farmDAO = new FarmDAO();
    private DefaultTableModel model;
    private JTable table;
    private JTextField txtSearch;
    private List<Kandang> currentData;

    private static final String[] COLS = {"ID", "Nama Kandang", "Farm", "Kapasitas", "Tipe"};

    public KandangPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
        JButton btnTambah = new JButton("+ Tambah Kandang");
        btnTambah.setBackground(new Color(24, 95, 165));
        btnTambah.setForeground(Color.WHITE);
        btnTambah.setOpaque(true);
        btnTambah.setContentAreaFilled(true);
        btnTambah.setBorderPainted(false);
        btnTambah.setFocusPainted(false);
        btnTambah.setPreferredSize(new Dimension(145, 28));
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
        table.getColumnModel().getColumn(3).setMaxWidth(100);

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
            Kandang selected = findCurrent(id);
            if (selected != null) {
                showDialog(selected);
            }
        });
        btnHapus.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Pilih data dulu!");
                return;
            }
            int id = (int) model.getValueAt(row, 0);
            if (JOptionPane.showConfirmDialog(this, "Hapus kandang ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                try {
                    dao.delete(id);
                    loadData();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Gagal hapus:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnRefresh.addActionListener(e -> loadData());
        txtSearch.addActionListener(e -> {
            try {
                currentData = dao.search(txtSearch.getText());
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        loadData();
    }

    private Kandang findCurrent(int id) {
        if (currentData == null) {
            loadData();
        }
        if (currentData == null) {
            return null;
        }
        for (Kandang kandang : currentData) {
            if (kandang.getKandangId() == id) {
                return kandang;
            }
        }
        return null;
    }

    private void loadData() {
        try {
            currentData = dao.getAll();
            refreshTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal load:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshTable() {
        model.setRowCount(0);
        for (Kandang k : currentData) {
            model.addRow(new Object[]{k.getKandangId(), k.getNamaKandang(), k.getNamaFarm(), k.getKapasitasMaksimal(), k.getTipeKandang()});
        }
    }

    private void showDialog(Kandang existing) {
        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
            existing == null ? "Tambah Kandang" : "Edit Kandang", true);
        dlg.setSize(420, 300);
        dlg.setLocationRelativeTo(this);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 4, 4, 4);

        JTextField fNama = new JTextField(existing != null ? existing.getNamaKandang() : "");
        JTextField fKap = new JTextField(existing != null ? String.valueOf(existing.getKapasitasMaksimal()) : "");
        JComboBox<String> cbTipe = new JComboBox<>(new String[]{"Baterai", "Lantai"});
        if (existing != null) {
            cbTipe.setSelectedItem(existing.getTipeKandang());
        }

        JComboBox<Farm> cbFarm = new JComboBox<>();
        try {
            farmDAO.getAll().forEach(cbFarm::addItem);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal load farm:\n" + ex.getMessage());
        }
        if (existing != null) {
            for (int i = 0; i < cbFarm.getItemCount(); i++) {
                if (cbFarm.getItemAt(i).getFarmId() == existing.getFarmId()) {
                    cbFarm.setSelectedIndex(i);
                    break;
                }
            }
        }

        String[] labels = {"Nama Kandang *", "Farm *", "Kapasitas Maks *", "Tipe"};
        Component[] inputs = {fNama, cbFarm, fKap, cbTipe};
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
            if (fNama.getText().trim().isEmpty() || fKap.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Nama dan Kapasitas wajib diisi!");
                return;
            }
            try {
                Farm selectedFarm = (Farm) cbFarm.getSelectedItem();
                if (selectedFarm == null) {
                    JOptionPane.showMessageDialog(dlg, "Pilih farm dulu!");
                    return;
                }
                Kandang k = existing != null ? existing : new Kandang();
                k.setNamaKandang(fNama.getText().trim());
                k.setKapasitasMaksimal(Integer.parseInt(fKap.getText().trim()));
                k.setTipeKandang((String) cbTipe.getSelectedItem());
                k.setFarmId(selectedFarm.getFarmId());
                if (existing == null) {
                    dao.insert(k);
                } else {
                    dao.update(k);
                }
                dlg.dispose();
                loadData();
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(dlg, "Kapasitas harus berupa angka!");
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
