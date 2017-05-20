package com.muhamadarief.skripsiku.utils;

import com.muhamadarief.skripsiku.API.ApiService;

/**
 * Created by Muhamad Arief on 21/04/2017.
 */

public class ApiUtils {

    private ApiUtils() {

    }

    public static final String BASE_URL = "http://192.168.43.73/Skripsi_PraktekDokter/";

    public static ApiService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(ApiService.class);
    }
}
