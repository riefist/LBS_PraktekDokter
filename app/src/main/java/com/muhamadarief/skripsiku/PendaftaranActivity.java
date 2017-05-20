package com.muhamadarief.skripsiku;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.muhamadarief.skripsiku.API.ApiService;
import com.muhamadarief.skripsiku.Model.BaseResponse;
import com.muhamadarief.skripsiku.Model.Pendaftaran;
import com.muhamadarief.skripsiku.Model.PraktekDokter;
import com.muhamadarief.skripsiku.utils.ApiUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PendaftaranActivity extends AppCompatActivity {

    public static final String TAG = "PendaftaranActivity";

    public static final String EXTRA_NAMA_DOKTER = "nama_dokter";
    public static final String EXTRA_TEMPAT_PRAKTEK = "tempat_praktek";
    public static final String EXTRA_SPESIALIS = "spesialis";

    private Spinner spinner;
    private TextView txt_tanggal, txt_no_antri, txt_nama_dokter, txt_spesialis;
    private TextView txt_status_pendaftaran;
    private EditText edtNama, edtAlamat, edtNohp;

    private Button btnDaftar;
    private ApiService mApiService;

    private Calendar calendar;
    private DateFormat dateFormat;
    String formattedDate;
    private String id_praktek;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendaftaran);

        id_praktek = getIntent().getStringExtra("ID_PRAKTEK");
        String nama_dokter = getIntent().getStringExtra(EXTRA_NAMA_DOKTER);
        String tempat_praktek = getIntent().getStringExtra(EXTRA_TEMPAT_PRAKTEK);
        String spesialis = getIntent().getStringExtra(EXTRA_SPESIALIS);

        getSupportActionBar().setTitle(tempat_praktek);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Toast.makeText(this, ""+id_praktek, Toast.LENGTH_SHORT).show();

        mApiService = ApiUtils.getAPIService();

        spinner = (Spinner) findViewById(R.id.spinner_status);
        txt_nama_dokter = (TextView) findViewById(R.id.txt_nama_dokter);
        txt_spesialis = (TextView) findViewById(R.id.txt_spesialis);
        txt_tanggal = (TextView) findViewById(R.id.txt_tanggal);
        txt_no_antri = (TextView) findViewById(R.id.txt_no_antri);
        edtNama = (EditText) findViewById(R.id.edt_nama);
        edtAlamat = (EditText) findViewById(R.id.edt_alamat);
        edtNohp = (EditText) findViewById(R.id.edt_nohp);
        btnDaftar = (Button) findViewById(R.id.btnDaftar);
        txt_status_pendaftaran = (TextView) findViewById(R.id.txt_status_pendaftaran);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status_pasien, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        calendar = Calendar.getInstance();
        dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        formattedDate = df.format(calendar.getTime());

        txt_nama_dokter.setText(nama_dokter);
        txt_spesialis.setText("Spesialis " +spesialis);
        txt_tanggal.setText(dateFormat.format(calendar.getTime()));

        getNoAntri(id_praktek, formattedDate);
        cekStatusPendaftaran(id_praktek);


        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!validateForm()){
                    return;
                }

                ProgressDialog progressDialog = new ProgressDialog(PendaftaranActivity.this);
                progressDialog.setTitle("Proses Pendaftaran");
                progressDialog.setMessage("Please Wait....");

                progressDialog.show();

                String nama = edtNama.getText().toString();
                String alamat = edtAlamat.getText().toString();
                String nohp = edtNohp.getText().toString();
                String status = spinner.getSelectedItem().toString();

                daftarBerobat("", id_praktek, formattedDate, nama, alamat, nohp, status, txt_no_antri.getText().toString());

                progressDialog.dismiss();
            }
        });

    }

    private void daftarBerobat(String id, String id_praktek, String tanggal, String nama, String alamat,
                               String nohp, String status, String no_antrian){

        mApiService.daftarBerobat(id, id_praktek, tanggal, nama, alamat, nohp,
                status, no_antrian).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(PendaftaranActivity.this, "Pendaftaran Berhasil.", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "post submitted to API." + response.body().toString());
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "Unable to submit post to API: " +t);

            }
        });

    }

    private void cekStatusPendaftaran(String id_praktek){
        mApiService.cekStatus(id_praktek).enqueue(new Callback<PraktekDokter.Status>() {
            @Override
            public void onResponse(Call<PraktekDokter.Status> call, Response<PraktekDokter.Status> response) {
                PraktekDokter.Status praktekDokter = (PraktekDokter.Status) response.body();

                String stat = String.valueOf(praktekDokter.getData().isStatus());
                Toast.makeText(PendaftaranActivity.this, ""+stat, Toast.LENGTH_SHORT).show();

                if (praktekDokter != null){
                    if (!praktekDokter.isError()){
                        if (praktekDokter.getData().isStatus()){
                            txt_status_pendaftaran.setText("Buka");
                            txt_status_pendaftaran.setTextColor(Color.GREEN);
                            edtNama.setEnabled(true);
                            edtNohp.setEnabled(true);
                            edtAlamat.setEnabled(true);
                            spinner.setEnabled(true);
                            btnDaftar.setEnabled(true);

                        } else {
                            txt_status_pendaftaran.setText("Tutup");
                            txt_status_pendaftaran.setTextColor(Color.RED);
                            edtNama.setEnabled(false);
                            edtNohp.setEnabled(false);
                            edtAlamat.setEnabled(false);
                            spinner.setEnabled(false);
                            btnDaftar.setEnabled(false);
                        }
                    }
                }

                Log.d(TAG, praktekDokter.getMessage());

            }

            @Override
            public void onFailure(Call<PraktekDokter.Status> call, Throwable t) {

            }
        });
    }

    private void getNoAntri(String id_praktek, String tanggal){

        mApiService.getNoAntri(id_praktek, tanggal).enqueue(new Callback<Pendaftaran>() {
            @Override
            public void onResponse(Call<Pendaftaran> call, Response<Pendaftaran> response) {

                Pendaftaran pendaftaran = (Pendaftaran) response.body();

                if(pendaftaran.isError()){
                    int no_antri = 1;
                    txt_no_antri.setText("" +no_antri);
                } else {

                Log.d(TAG, "no antri terakhir: " +pendaftaran.getData().getNo_antrian());

                int last_no_antri = Integer.parseInt(pendaftaran.getData().getNo_antrian());

                int no_antri = last_no_antri + 1;

                txt_no_antri.setText(""+no_antri);
                }
            }

            @Override
            public void onFailure(Call<Pendaftaran> call, Throwable t) {
                Log.d(TAG, "Gagal dapat no antri: " +t);
            }
        });
    }

    private boolean validateForm(){
        boolean result =  true;
        if(TextUtils.isEmpty(edtNama.getText().toString())){
            edtNama.setError("Required");
            result = false;
        } else {
            edtNama.setError(null);
        }

        if(TextUtils.isEmpty(edtAlamat.getText().toString())){
            edtAlamat.setError("Required");
            result = false;
        } else {
            edtAlamat.setError(null);
        }

        if(TextUtils.isEmpty(edtNohp.getText().toString())){
            edtNohp.setError("Required");
            result = false;
        } else {
            edtNohp.setError(null);
        }

        return  result;
    }


}
