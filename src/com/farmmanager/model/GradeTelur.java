package com.farmmanager.model;

public class GradeTelur {
    private int gradeId;
    private String namaGrade;
    private String deskripsi;
    private double hargaStandar;

    public GradeTelur() {}

    public GradeTelur(int gradeId, String namaGrade, String deskripsi, double hargaStandar) {
        this.gradeId = gradeId;
        this.namaGrade = namaGrade;
        this.deskripsi = deskripsi;
        this.hargaStandar = hargaStandar;
    }

    public int getGradeId() { return gradeId; }
    public void setGradeId(int gradeId) { this.gradeId = gradeId; }

    public String getNamaGrade() { return namaGrade; }
    public void setNamaGrade(String namaGrade) { this.namaGrade = namaGrade; }

    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }

    public double getHargaStandar() { return hargaStandar; }
    public void setHargaStandar(double hargaStandar) { this.hargaStandar = hargaStandar; }

    @Override
    public String toString() {
        return namaGrade + " - " + deskripsi;
    }
}