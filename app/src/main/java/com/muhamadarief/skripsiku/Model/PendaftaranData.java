package com.muhamadarief.skripsiku.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Muhamad Arief on 19/05/2017.
 */

public class PendaftaranData {

    @SerializedName("id")
    private String id;
    @SerializedName("id_praktek")
    private String id_praktek;
    @SerializedName("tanggal")
    private String tanggal;
    @SerializedName("nama")
    private String nama;
    @SerializedName("alamat")
    private String alamat;
    @SerializedName("nohp")
    private String nohp;
    @SerializedName("status")
    private String status;
    @SerializedName("no_antrian")
    private String no_antrian;

    public PendaftaranData() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_praktek() {
        return id_praktek;
    }

    public void setId_praktek(String id_praktek) {
        this.id_praktek = id_praktek;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNohp() {
        return nohp;
    }

    public void setNohp(String nohp) {
        this.nohp = nohp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNo_antrian() {
        return no_antrian;
    }

    public void setNo_antrian(String no_antrian) {
        this.no_antrian = no_antrian;
    }
}
