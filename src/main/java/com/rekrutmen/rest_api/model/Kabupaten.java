package com.rekrutmen.rest_api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "kabupaten", schema = "wilayah")
public class Kabupaten {

    @Id
    private String kodeKabupaten;

    private String namaKabupaten;

    private String kodeProvinsi;

    private Integer isKota;

    // Getters and Setters
    public String getKodeKabupaten() {
        return kodeKabupaten;
    }

    public void setKodeKabupaten(String kodeKabupaten) {
        this.kodeKabupaten = kodeKabupaten;
    }

    public String getNamaKabupaten() {
        return namaKabupaten;
    }

    public void setNamaKabupaten(String namaKabupaten) {
        this.namaKabupaten = namaKabupaten;
    }

    public String getKodeProvinsi() {
        return kodeProvinsi;
    }

    public void setKodeProvinsi(String kodeProvinsi) {
        this.kodeProvinsi = kodeProvinsi;
    }

    public Integer getIsKota() {
        return isKota;
    }

    public void setIsKota(Integer isKota) {
        this.isKota = isKota;
    }
}
