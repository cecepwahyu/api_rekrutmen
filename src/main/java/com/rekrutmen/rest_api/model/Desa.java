package com.rekrutmen.rest_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "desa", schema = "wilayah")
public class Desa {

    @Id
    private String kodeDesa;

    private String namaDesa;

    private Integer isKelurahan;

    private String kodeKecamatan;

    // Getters and Setters
    public String getKodeDesa() {
        return kodeDesa;
    }

    public void setKodeDesa(String kodeDesa) {
        this.kodeDesa = kodeDesa;
    }

    public String getNamaDesa() {
        return namaDesa;
    }

    public void setNamaDesa(String namaDesa) {
        this.namaDesa = namaDesa;
    }

    public Integer getIsKelurahan() {
        return isKelurahan;
    }

    public void setIsKelurahan(Integer isKelurahan) {
        this.isKelurahan = isKelurahan;
    }

    public String getKodeKecamatan() {
        return kodeKecamatan;
    }

    public void setKodeKecamatan(String kodeKecamatan) {
        this.kodeKecamatan = kodeKecamatan;
    }
}
