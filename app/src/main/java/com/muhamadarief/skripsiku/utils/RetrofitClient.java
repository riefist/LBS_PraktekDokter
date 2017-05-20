package com.muhamadarief.skripsiku.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Muhamad Arief on 21/04/2017.
 */

public class RetrofitClient {

    private static Retrofit retrofit = null;




    public static Retrofit getClient(String baseUrl) {
        if (retrofit==null) {

            Gson gson = new GsonBuilder().serializeNulls().create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}
