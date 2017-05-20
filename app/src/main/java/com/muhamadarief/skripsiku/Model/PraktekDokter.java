package com.muhamadarief.skripsiku.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Muhamad Arief on 02/04/2017.
 */

public class PraktekDokter {

    @SerializedName("id_praktek")
    private String id_praktek;

    @SerializedName("nama_dokter")
    private String nama_dokter;

    @SerializedName("id_spesialis")
    private String id_spesialis;

    @SerializedName("tempat_praktek")
    private String tempat_praktek;

    @SerializedName("jadwal")
    private String jadwal;

    @SerializedName("jam")
    private String jam;

    @SerializedName("keterangan")
    private String keterangan;

    @SerializedName("latitude")
    private Double latitude;

    @SerializedName("longitude")
    private Double longitude;

    @SerializedName("status")
    private boolean status;


    public PraktekDokter() {
    }

    public PraktekDokter(String id_praktek, String nama_dokter, String id_spesialis, String tempat_praktek, String jadwal, String jam, String keterangan, Double latitude, Double longitude) {
        this.id_praktek = id_praktek;
        this.nama_dokter = nama_dokter;
        this.id_spesialis = id_spesialis;
        this.tempat_praktek = tempat_praktek;
        this.jadwal = jadwal;
        this.jam = jam;
        this.keterangan = keterangan;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId_praktek() {
        return id_praktek;
    }

    public void setId_praktek(String id_praktek) {
        this.id_praktek = id_praktek;
    }

    public String getNama_dokter() {
        return nama_dokter;
    }

    public void setNama_dokter(String nama_dokter) {
        this.nama_dokter = nama_dokter;
    }

    public String getId_spesialis() {
        return id_spesialis;
    }

    public void setId_spesialis(String id_spesialis) {
        this.id_spesialis = id_spesialis;
    }

    public String getTempat_praktek() {
        return tempat_praktek;
    }

    public void setTempat_praktek(String tempat_praktek) {
        this.tempat_praktek = tempat_praktek;
    }

    public String getJadwal() {
        return jadwal;
    }

    public void setJadwal(String jadwal) {
        this.jadwal = jadwal;
    }

    public String getJam() {
        return jam;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }




    public class Status extends BaseResponse{
        public StatusData data;

        public StatusData getData() {
            return data;
        }

        public void setData(StatusData data) {
            this.data = data;
        }
    }

    public class StatusData{

        @SerializedName("id_praktek")
        private String id_praktek;

        @SerializedName("nama_dokter")
        private String nama_dokter;

        @SerializedName("id_spesialis")
        private String id_spesialis;

        @SerializedName("tempat_praktek")
        private String tempat_praktek;

        @SerializedName("jadwal")
        private String jadwal;

        @SerializedName("jam")
        private String jam;

        @SerializedName("keterangan")
        private String keterangan;

        @SerializedName("latitude")
        private Double latitude;

        @SerializedName("longitude")
        private Double longitude;

        @SerializedName("status")
        private boolean status;

        public StatusData() {
        }

        public String getId_praktek() {
            return id_praktek;
        }

        public void setId_praktek(String id_praktek) {
            this.id_praktek = id_praktek;
        }

        public String getNama_dokter() {
            return nama_dokter;
        }

        public void setNama_dokter(String nama_dokter) {
            this.nama_dokter = nama_dokter;
        }

        public String getId_spesialis() {
            return id_spesialis;
        }

        public void setId_spesialis(String id_spesialis) {
            this.id_spesialis = id_spesialis;
        }

        public String getTempat_praktek() {
            return tempat_praktek;
        }

        public void setTempat_praktek(String tempat_praktek) {
            this.tempat_praktek = tempat_praktek;
        }

        public String getJadwal() {
            return jadwal;
        }

        public void setJadwal(String jadwal) {
            this.jadwal = jadwal;
        }

        public String getJam() {
            return jam;
        }

        public void setJam(String jam) {
            this.jam = jam;
        }

        public String getKeterangan() {
            return keterangan;
        }

        public void setKeterangan(String keterangan) {
            this.keterangan = keterangan;
        }

        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }
    }
}
