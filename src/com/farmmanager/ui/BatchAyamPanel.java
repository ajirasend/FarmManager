package com.farmmanager.ui;

import com.farmmanager.dao.BatchAyamDAO;
import com.farmmanager.dao.KandangDAO;
import com.farmmanager.model.BatchAyam;
import com.farmmanager.model.Kandang;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BatchAyamPanel extends JPanel {
    private final BatchAyamDAO dao = new BatchAyamDAO();
    private final KandangDAO kandangDAO = new KandangDAO();
    private DefaultTableModel model;
    private JTable table;
    private JTextField txtSearch;
    private List<BatchAyam> currentData;

    private static final String[] COLS = {"ID", "Kode Batch", "Kandang", "Tgl Masuk", "Jml Awal", "Jml Hidup", "Status"};

    public BatchAyamPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
        JButton btnTambah = new JButton("+ Tambah Batch");
        btnTambah.setBackground(new Color(24, 95, 165));
        btnTambah.setForeground(Color.WHITE);
        btnTambah.setOpaque(true);
        btnTambah.setContentAreaFilled(true);
        btnTambah.setBorderPainted(false);
        btnTambah.setFocusPainted(false);
        btnTambah.setPreferredSize(new Dimension(140, 28));
        JButton btnEdit = new JButton("✏ Edit");
        JButton btnHapus = new JButton("🗑 Hapus");
        btnHapus.setForeground(new Color(163, 45, 45));
        JButton btnRefresh = new JButton("↻ Refresh");
        txtSearch = new JTextField(18);
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

        table.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int r, int c) {
                super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                String s = v != null ? v.toString() : "";
                if (!sel) {
                    setBackground(s.equals("Aktif") ? new Color(234, 243, 222)
                        : s.equals("Selesai") ? new Color(230, 241, 251)
                        : Color.WHITE);
                    setForeground(s.equals("Aktif") ? new Color(59, 109, 17)
                        : s.equals("Selesai") ? new Color(24, 95, 165)
                        : Color.DARK_GRAY);
                }
                return this;
            }
        });

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
            BatchAyam selected = findCurrent(id);
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
            if (JOptionPane.showConfirmDialog(this, "Hapus batch ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
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

    private BatchAyam findCurrent(int id) {
        if (currentData == null) {
            loadData();
        }
        if (currentData == null) {
            return null;
        }
        for (BatchAyam batch : currentData) {
            if (batch.getBatchId() == id) {
                return batch;
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
        for (BatchAyam b : currentData) {
            model.addRow(new Object[]{b.getBatchId(), b.getKodeBatch(), b.getNamaKandang(), b.getTanggalMasuk(), b.getJumlahAwal(), b.getJumlahHidup(), b.getStatusBatch()});
        }
    }

    private void showDialog(BatchAyam existing) {
        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
            existing == null ? "Tambah Batch Ayam" : "Edit Batch Ayam", true);
        dlg.setSize(450, 340);
        dlg.setLocationRelativeTo(this);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 4, 4, 4);

        JTextField fKode = new JTextField(existing != null ? existing.getKodeBatch() : "");
        JTextField fTgl = new JTextField(existing != null ? existing.getTanggalMasuk() : "2026-06-21");
        JTextField fAwal = new JTextField(existing != null ? String.valueOf(existing.getJumlahAwal()) : "");
        JTextField fHidup = new JTextField(existing != null ? String.valueOf(existing.getJumlahHidup()) : "");
        JComboBox<String> cbStatus = new JComboBox<>(new String[]{"Aktif", "Afkir"});
        if (existing != null) {
            cbStatus.setSelectedItem(existing.getStatusBatch());
        }

        JComboBox<Kandang> cbKandang = new JComboBox<>();
        try {
            kandangDAO.getAll().forEach(cbKandang::addItem);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal load kandang");
        }
        if (existing != null) {
            for (int i = 0; i < cbKandang.getItemCount(); i++) {
                if (cbKandang.getItemAt(i).getKandangId() == existing.getKandangId()) {
                    cbKandang.setSelectedIndex(i);
                    break;
                }
            }
        }

        String[] labels = {"Kode Batch *", "Kandang *", "Tanggal Masuk (yyyy-MM-dd)", "Jumlah Awal *", "Jumlah Hidup", "Status"};
        Component[] inputs = {fKode, cbKandang, fTgl, fAwal, fHidup, cbStatus};
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
            if (fKode.getText().trim().isEmpty() || fAwal.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Kode Batch dan Jumlah Awal wajib diisi!");
                return;
            }
            try {
                int jumlahAwal = Integer.parseInt(fAwal.getText().trim());
                Kandang selectedKandang = (Kandang) cbKandang.getSelectedItem();
                if (selectedKandang == null) {
                    JOptionPane.showMessageDialog(dlg, "Pilih kandang dulu!");
                    return;
                }
                if (jumlahAwal > selectedKandang.getKapasitasMaksimal()) {
                    JOptionPane.showMessageDialog(dlg,
                        "⚠️ Gagal Simpan!\n\nJumlah awal ayam (" + jumlahAwal + ") melebihi\nkapasitas maksimal kandang (" +
                        selectedKandang.getKapasitasMaksimal() + ")!\n\n[Trigger: trg_CheckKapasitasKandang]",
                        "Pelanggaran Kapasitas", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                BatchAyam b = existing != null ? existing : new BatchAyam();
                b.setKodeBatch(fKode.getText().trim());
                b.setTanggalMasuk(fTgl.getText().trim());
                b.setJumlahAwal(jumlahAwal);
                b.setJumlahHidup(fHidup.getText().trim().isEmpty() ? jumlahAwal : Integer.parseInt(fHidup.getText().trim()));
                b.setStatusBatch((String) cbStatus.getSelectedItem());
                b.setKandangId(selectedKandang.getKandangId());
                b.setJenisAyamId(1);
                if (existing == null) {
                    dao.insert(b);
                } else {
                    dao.update(b);
                }
                dlg.dispose();
                loadData();
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(dlg, "Jumlah ayam harus berupa angka!");
            } catch (Exception ex) {
                String msg = ex.getMessage();
                if (msg != null && msg.contains("kapasitas")) {
                    JOptionPane.showMessageDialog(dlg, "⚠️ " + msg, "Trigger Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(dlg, "Gagal simpan:\n" + msg, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnCancel.addActionListener(e -> dlg.dispose());

        dlg.setLayout(new BorderLayout());
        dlg.add(form, BorderLayout.CENTER);
        dlg.add(btnPanel, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }
}
