package com.muhamadarief.skripsiku.API;

import com.muhamadarief.skripsiku.Model.BaseResponse;
import com.muhamadarief.skripsiku.Model.Pendaftaran;
import com.muhamadarief.skripsiku.Model.PraktekDokter;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Muhamad Arief on 02/04/2017.
 */

public interface ApiService {

    @GET("lihat_data.php")
    Call<List<PraktekDokter>> getSemuaPraktek();

    @GET("spesialis.php")
    Call<List<PraktekDokter>> getBySpesialis(@Query("id_spesialis") int id_spesialis);

    @FormUrlEncoded
    @POST("pendaftaran.php")
    Call<ResponseBody> daftarBerobat(@Field("id") String id, @Field("id_praktek") String id_praktek, @Field("tanggal") String tanggal,
                                    @Field("nama") String nama, @Field("alamat") String alamat, @Field("nohp") String nohp,
                                    @Field("status") String status, @Field("no_antrian") String no_antrian);

    @GET("pendaftaran.php")
    Call<Pendaftaran> getNoAntri(
            @Query("id_praktek") String id_praktek,
            @Query("tanggal") String tanggal);

    @GET("cek_status.php")
    Call<PraktekDokter.Status> cekStatus(
            @Query("id_praktek") String id_praktek);


}
