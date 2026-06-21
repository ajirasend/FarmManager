package com.farmmanager.ui;

import com.farmmanager.dao.BatchAyamDAO;
import com.farmmanager.dao.GradeTelurDAO;
import com.farmmanager.dao.KaryawanDAO;
import com.farmmanager.dao.PanenTelurDAO;
import com.farmmanager.model.BatchAyam;
import com.farmmanager.model.GradeTelur;
import com.farmmanager.model.Karyawan;
import com.farmmanager.model.PanenTelur;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class PanenTelurPanel extends JPanel {
    private final PanenTelurDAO dao = new PanenTelurDAO();
    private final BatchAyamDAO batchDAO = new BatchAyamDAO();
    private final KaryawanDAO karyawanDAO = new KaryawanDAO();
    private final GradeTelurDAO gradeDAO = new GradeTelurDAO();

    private DefaultTableModel model;
    private JTable table;
    private JTextField txtSearch;
    private List<PanenTelur> currentData;

    private static final String[] COLS = {"ID", "Tanggal", "Kandang", "Karyawan", "Total Butir", "Berat (kg)"};

    public PanenTelurPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
        JButton btnTambah = new JButton("+ Catat Panen");
        btnTambah.setBackground(new Color(24, 95, 165));
        btnTambah.setForeground(Color.WHITE);
        btnTambah.setOpaque(true);
        btnTambah.setContentAreaFilled(true);
        btnTambah.setBorderPainted(false);
        btnTambah.setFocusPainted(false);
        btnTambah.setPreferredSize(new Dimension(140, 28));

        JButton btnHapus = new JButton("🗑 Hapus");
        btnHapus.setForeground(new Color(163, 45, 45));

        JButton btnRefresh = new JButton("↻ Refresh");

        txtSearch = new JTextField(18);
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));

        toolbar.add(btnTambah);
        toolbar.add(btnHapus);
        toolbar.add(btnRefresh);
        toolbar.add(new JLabel("  Cari Kandang:"));
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

        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 6));
        summaryPanel.setBackground(new Color(240, 248, 255));
        summaryPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        JLabel lblTotal = new JLabel("Total Panen: -");
        lblTotal.setFont(new Font("SansSerif", Font.BOLD, 12));
        summaryPanel.add(lblTotal);

        add(toolbar, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(summaryPanel, BorderLayout.SOUTH);

        btnTambah.addActionListener(e -> showDialog(lblTotal));
        btnHapus.addActionListener(e -> hapusSelected(lblTotal));
        btnRefresh.addActionListener(e -> loadData(lblTotal));
        txtSearch.addActionListener(e -> searchData(lblTotal));

        loadData(lblTotal);
    }

    private void showDialog(JLabel lblTotal) {
        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Catat Panen Telur", true);
        dlg.setSize(460, 360);
        dlg.setLocationRelativeTo(this);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 4, 4, 4);

        JTextField fTgl = new JTextField(LocalDate.now().toString());
        JTextField fButir = new JTextField();
        JTextField fBerat = new JTextField();
        JTextField fCatatan = new JTextField();

        JComboBox<BatchAyam> cbBatch = new JComboBox<>();
        JComboBox<Karyawan> cbKaryawan = new JComboBox<>();
        JComboBox<GradeTelur> cbGrade = new JComboBox<>();

        try {
            batchDAO.getAll().forEach(cbBatch::addItem);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dlg, "Gagal load batch:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        try {
            karyawanDAO.getAll().forEach(k -> {
                if (k.isStatusAktif()) {
                    cbKaryawan.addItem(k);
                }
            });
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dlg, "Gagal load karyawan:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        try {
            gradeDAO.getAll().forEach(cbGrade::addItem);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dlg, "Gagal load grade telur:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        String[] labels = {
            "Tanggal Panen (yyyy-MM-dd)",
            "Kandang / Batch *",
            "Karyawan *",
            "Grade Telur *",
            "Jumlah Butir *",
            "Berat Total (kg)",
            "Catatan"
        };

        Component[] inputs = {
            fTgl,
            cbBatch,
            cbKaryawan,
            cbGrade,
            fButir,
            fBerat,
            fCatatan
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
            if (fTgl.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Tanggal panen wajib diisi!");
                return;
            }

            if (fButir.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Jumlah butir wajib diisi!");
                return;
            }

            BatchAyam selectedBatch = (BatchAyam) cbBatch.getSelectedItem();
            if (selectedBatch == null) {
                JOptionPane.showMessageDialog(dlg, "Pilih batch dulu!");
                return;
            }

            Karyawan selectedKaryawan = (Karyawan) cbKaryawan.getSelectedItem();
            if (selectedKaryawan == null) {
                JOptionPane.showMessageDialog(dlg, "Pilih karyawan dulu!");
                return;
            }

            GradeTelur selectedGrade = (GradeTelur) cbGrade.getSelectedItem();
            if (selectedGrade == null) {
                JOptionPane.showMessageDialog(dlg, "Pilih grade telur dulu!");
                return;
            }

            try {
                int butir = Integer.parseInt(fButir.getText().trim());
                double berat = fBerat.getText().trim().isEmpty()
                    ? butir * 0.06
                    : Double.parseDouble(fBerat.getText().trim());

                PanenTelur p = new PanenTelur();
                p.setTanggalPanen(fTgl.getText().trim());
                p.setBatchId(selectedBatch.getBatchId());
                p.setKandangId(selectedBatch.getKandangId());
                p.setKaryawanId(selectedKaryawan.getKaryawanId());
                p.setCatatan(fCatatan.getText().trim());

                dao.insert(p, butir, berat, selectedGrade.getGradeId());

                dlg.dispose();
                loadData(lblTotal);
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(dlg, "Jumlah butir dan berat harus berupa angka!");
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

    private void hapusSelected(JLabel lblTotal) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih data dulu!");
            return;
        }

        int id = (int) model.getValueAt(row, 0);

        int confirm = JOptionPane.showConfirmDialog(this,
            "Hapus catatan panen ini?\nDetail panen juga akan dihapus.",
            "Konfirmasi",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                dao.delete(id);
                loadData(lblTotal);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal hapus:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadData(JLabel lblTotal) {
        try {
            currentData = dao.getAll();
            refreshTable(lblTotal);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal load:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchData(JLabel lblTotal) {
        try {
            currentData = dao.search(txtSearch.getText());
            refreshTable(lblTotal);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal cari:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshTable(JLabel lblTotal) {
        model.setRowCount(0);

        int totalButir = 0;
        double totalBerat = 0;

        for (PanenTelur p : currentData) {
            model.addRow(new Object[]{
                p.getPanenId(),
                p.getTanggalPanen(),
                p.getNamaKandang(),
                p.getNamaKaryawan(),
                p.getTotalButir(),
                String.format("%.1f", p.getTotalBeratKg())
            });

            totalButir += p.getTotalButir();
            totalBerat += p.getTotalBeratKg();
        }

        lblTotal.setText(String.format("Total: %d sesi panen  |  %,d butir  |  %.1f kg",
            currentData.size(), totalButir, totalBerat));
    }
}