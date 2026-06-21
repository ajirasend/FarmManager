package com.farmmanager.model;

public class Kandang {
    private int kandangId;
    private String namaKandang;
    private int kapasitasMaksimal;
    private String tipeKandang;
    private int farmId;
    private String namaFarm;

    public Kandang() {}

    public Kandang(int kandangId, String namaKandang, int kapasitasMaksimal, String tipeKandang, int farmId, String namaFarm) {
        this.kandangId = kandangId;
        this.namaKandang = namaKandang;
        this.kapasitasMaksimal = kapasitasMaksimal;
        this.tipeKandang = tipeKandang;
        this.farmId = farmId;
        this.namaFarm = namaFarm;
    }

    public int getKandangId() { return kandangId; }
    public void setKandangId(int kandangId) { this.kandangId = kandangId; }
    public String getNamaKandang() { return namaKandang; }
    public void setNamaKandang(String namaKandang) { this.namaKandang = namaKandang; }
    public int getKapasitasMaksimal() { return kapasitasMaksimal; }
    public void setKapasitasMaksimal(int kapasitasMaksimal) { this.kapasitasMaksimal = kapasitasMaksimal; }
    public String getTipeKandang() { return tipeKandang; }
    public void setTipeKandang(String tipeKandang) { this.tipeKandang = tipeKandang; }
    public int getFarmId() { return farmId; }
    public void setFarmId(int farmId) { this.farmId = farmId; }
    public String getNamaFarm() { return namaFarm; }
    public void setNamaFarm(String namaFarm) { this.namaFarm = namaFarm; }

    @Override
    public String toString() { return namaKandang; }
}
