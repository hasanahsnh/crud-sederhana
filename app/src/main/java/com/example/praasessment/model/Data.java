package com.example.praasessment.model;

public class Data {
    private String id, nim, nama, ttl, jenis_kelamin, alamat;

    public Data() {

    }

    public Data(String id, String nim, String nama, String ttl, String jenis_kelamin, String alamat) {
        this.id = id;
        this.nim = nim;
        this.nama = nama;
        this.ttl = ttl;
        this.jenis_kelamin = jenis_kelamin;
        this.alamat = alamat;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTtl() {
        return ttl;
    }

    public void setTtl(String ttl) {
        this.ttl = ttl;
    }

    public String getJenis_kelamin() {
        return jenis_kelamin;
    }

    public void setJenis_kelamin(String jenis_kelamin) {
        this.jenis_kelamin = jenis_kelamin;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
}
