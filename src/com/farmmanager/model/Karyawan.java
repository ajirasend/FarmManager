package com.farmmanager.model;

public class Karyawan {
    private int karyawanId;
    private String namaKaryawan;
    private String noTelepon;
    private String alamat;
    private String tanggalMulai;
    private boolean statusAktif;
    private String peran;
    private int farmId;
    private String namaFarm;

    public Karyawan() {}

    public Karyawan(int karyawanId, String namaKaryawan, String noTelepon, String alamat,
                    String tanggalMulai, boolean statusAktif, String peran, int farmId, String namaFarm) {
        this.karyawanId = karyawanId;
        this.namaKaryawan = namaKaryawan;
        this.noTelepon = noTelepon;
        this.alamat = alamat;
        this.tanggalMulai = tanggalMulai;
        this.statusAktif = statusAktif;
        this.peran = peran;
        this.farmId = farmId;
        this.namaFarm = namaFarm;
    }

    public int getKaryawanId() { return karyawanId; }
    public void setKaryawanId(int karyawanId) { this.karyawanId = karyawanId; }

    public String getNamaKaryawan() { return namaKaryawan; }
    public void setNamaKaryawan(String namaKaryawan) { this.namaKaryawan = namaKaryawan; }

    public String getNoTelepon() { return noTelepon; }
    public void setNoTelepon(String noTelepon) { this.noTelepon = noTelepon; }

    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }

    public String getTanggalMulai() { return tanggalMulai; }
    public void setTanggalMulai(String tanggalMulai) { this.tanggalMulai = tanggalMulai; }

    public boolean isStatusAktif() { return statusAktif; }
    public void setStatusAktif(boolean statusAktif) { this.statusAktif = statusAktif; }

    public String getPeran() { return peran; }
    public void setPeran(String peran) { this.peran = peran; }

    public int getFarmId() { return farmId; }
    public void setFarmId(int farmId) { this.farmId = farmId; }

    public String getNamaFarm() { return namaFarm; }
    public void setNamaFarm(String namaFarm) { this.namaFarm = namaFarm; }

    @Override
    public String toString() {
        return namaKaryawan;
    }
}