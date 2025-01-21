package com.rekrutmen.rest_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "kecamatan", schema = "wilayah")
public class Kecamatan {

    @Id
    private String kodeKecamatan;

    private String namaKecamatan;

    private String kodeKabupaten;

    // Getters and Setters
    public String getKodeKecamatan() {
        return kodeKecamatan;
    }

    public void setKodeKecamatan(String kodeKecamatan) {
        this.kodeKecamatan = kodeKecamatan;
    }

    public String getNamaKecamatan() {
        return namaKecamatan;
    }

    public void setNamaKecamatan(String namaKecamatan) {
        this.namaKecamatan = namaKecamatan;
    }

    public String getKodeKabupaten() {
        return kodeKabupaten;
    }

    public void setKodeKabupaten(String kodeKabupaten) {
        this.kodeKabupaten = kodeKabupaten;
    }
}
