package com.farmmanager.model;

public class BatchAyam {
    private int batchId;
    private String kodeBatch;
    private String tanggalMasuk;
    private int jumlahAwal;
    private int jumlahHidup;
    private String statusBatch;
    private int kandangId;
    private String namaKandang;
    private int jenisAyamId;

    public BatchAyam() {}

    public int getBatchId() { return batchId; }
    public void setBatchId(int batchId) { this.batchId = batchId; }

    public String getKodeBatch() { return kodeBatch; }
    public void setKodeBatch(String kodeBatch) { this.kodeBatch = kodeBatch; }

    public String getTanggalMasuk() { return tanggalMasuk; }
    public void setTanggalMasuk(String tanggalMasuk) { this.tanggalMasuk = tanggalMasuk; }

    public int getJumlahAwal() { return jumlahAwal; }
    public void setJumlahAwal(int jumlahAwal) { this.jumlahAwal = jumlahAwal; }

    public int getJumlahHidup() { return jumlahHidup; }
    public void setJumlahHidup(int jumlahHidup) { this.jumlahHidup = jumlahHidup; }

    public String getStatusBatch() { return statusBatch; }
    public void setStatusBatch(String statusBatch) { this.statusBatch = statusBatch; }

    public int getKandangId() { return kandangId; }
    public void setKandangId(int kandangId) { this.kandangId = kandangId; }

    public String getNamaKandang() { return namaKandang; }
    public void setNamaKandang(String namaKandang) { this.namaKandang = namaKandang; }

    public int getJenisAyamId() { return jenisAyamId; }
    public void setJenisAyamId(int jenisAyamId) { this.jenisAyamId = jenisAyamId; }

    @Override
    public String toString() {
        String kandang = namaKandang != null ? namaKandang : "Kandang";
        String batch = kodeBatch != null ? kodeBatch : "Batch";
        return kandang + " - " + batch;
    }
}