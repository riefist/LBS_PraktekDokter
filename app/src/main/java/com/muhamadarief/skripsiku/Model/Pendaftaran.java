package com.muhamadarief.skripsiku.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Muhamad Arief on 21/04/2017.
 */

public class Pendaftaran extends BaseResponse{

    private PendaftaranData data;

    public Pendaftaran() {
    }

    public PendaftaranData getData() {
        return data;
    }

    public void setData(PendaftaranData data) {
        this.data = data;
    }
}
