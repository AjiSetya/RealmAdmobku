package com.blogspot.blogsetyaaji.realmadmob;

import io.realm.RealmObject;

/**
 * Created by AJISETYA on 8/19/2017.
 */

public class ModelMahasiswa extends RealmObject{
    public String getIdmahasiswa() {
        return idmahasiswa;
    }

    public void setIdmahasiswa(String idmahasiswa) {
        this.idmahasiswa = idmahasiswa;
    }

    public String getNamamahasiswa() {
        return namamahasiswa;
    }

    public void setNamamahasiswa(String namamahasiswa) {
        this.namamahasiswa = namamahasiswa;
    }

    public String getJurusanmahasiswa() {
        return jurusanmahasiswa;
    }

    public void setJurusanmahasiswa(String jurusanmahasiswa) {
        this.jurusanmahasiswa = jurusanmahasiswa;
    }

    String idmahasiswa, namamahasiswa, jurusanmahasiswa;
}
