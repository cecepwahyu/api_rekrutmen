package com.rekrutmen.rest_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "provinsi", schema = "wilayah")
public class Provinsi {

    @Id
    private String kodeProvinsi;
    private String namaProvinsi;

    // Getters and Setters
    public String getKodeProvinsi() {
        return kodeProvinsi;
    }

    public void setKodeProvinsi(String kodeProvinsi) {
        this.kodeProvinsi = kodeProvinsi;
    }

    public String getNamaProvinsi() {
        return namaProvinsi;
    }

    public void setNamaProvinsi(String namaProvinsi) {
        this.namaProvinsi = namaProvinsi;
    }
}
