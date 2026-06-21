package com.farmmanager.model;

public class Farm {
    private int farmId;
    private String namaFarm;
    private String lokasi;
    private String telepon;

    public Farm() {}

    public Farm(int farmId, String namaFarm, String lokasi, String telepon) {
        this.farmId = farmId;
        this.namaFarm = namaFarm;
        this.lokasi = lokasi;
        this.telepon = telepon;
    }

    public int getFarmId() { return farmId; }
    public void setFarmId(int farmId) { this.farmId = farmId; }
    public String getNamaFarm() { return namaFarm; }
    public void setNamaFarm(String namaFarm) { this.namaFarm = namaFarm; }
    public String getLokasi() { return lokasi; }
    public void setLokasi(String lokasi) { this.lokasi = lokasi; }
    public String getTelepon() { return telepon; }
    public void setTelepon(String telepon) { this.telepon = telepon; }

    @Override
    public String toString() { return namaFarm; }
}
