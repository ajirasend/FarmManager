package com.farmmanager.model;

public class PanenTelur {
    private int panenId;
    private String tanggalPanen;
    private int batchId;
    private int kandangId;
    private String namaKandang;
    private int karyawanId;
    private String namaKaryawan;
    private String catatan;
    private int totalButir;
    private double totalBeratKg;

    public PanenTelur() {}

    public int getPanenId() { return panenId; }
    public void setPanenId(int panenId) { this.panenId = panenId; }
    public String getTanggalPanen() { return tanggalPanen; }
    public void setTanggalPanen(String tanggalPanen) { this.tanggalPanen = tanggalPanen; }
    public int getBatchId() { return batchId; }
    public void setBatchId(int batchId) { this.batchId = batchId; }
    public int getKandangId() { return kandangId; }
    public void setKandangId(int kandangId) { this.kandangId = kandangId; }
    public String getNamaKandang() { return namaKandang; }
    public void setNamaKandang(String namaKandang) { this.namaKandang = namaKandang; }
    public int getKaryawanId() { return karyawanId; }
    public void setKaryawanId(int karyawanId) { this.karyawanId = karyawanId; }
    public String getNamaKaryawan() { return namaKaryawan; }
    public void setNamaKaryawan(String namaKaryawan) { this.namaKaryawan = namaKaryawan; }
    public String getCatatan() { return catatan; }
    public void setCatatan(String catatan) { this.catatan = catatan; }
    public int getTotalButir() { return totalButir; }
    public void setTotalButir(int totalButir) { this.totalButir = totalButir; }
    public double getTotalBeratKg() { return totalBeratKg; }
    public void setTotalBeratKg(double totalBeratKg) { this.totalBeratKg = totalBeratKg; }
}
