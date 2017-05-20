package com.muhamadarief.skripsiku;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmadrosid.lib.drawroutemap.DrawMarker;
import com.ahmadrosid.lib.drawroutemap.DrawRouteMaps;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;
import com.muhamadarief.skripsiku.API.ApiService;
import com.muhamadarief.skripsiku.Model.PraktekDokter;
import com.muhamadarief.skripsiku.utils.ApiUtils;
import com.muhamadarief.skripsiku.utils.RxPermissions;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.subscriptions.CompositeSubscription;

import static com.google.android.gms.location.LocationServices.FusedLocationApi;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{

    public static final String TAG = "MainActivity";

    ArrayList<PraktekDokter> datapraktek = new ArrayList<PraktekDokter>();
    private List<Marker> markers = new ArrayList<>();

    private GoogleMap mMap;
    Marker mCurrLocationMarker;
    Marker mPrakterMarker;
    Circle circle;

    String[] REQUEST_TYPE_PERMISSION = {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION};
    CompositeSubscription subscription = new CompositeSubscription();

    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    Location mLastLocation;

    private Spinner spinner;
    private Button btnGo;
    private Button btnDaftar;
    private ApiService mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);*/

        getSupportActionBar().setElevation(0);

        buildGoogleApiClient();
        checkGpsService();

        mApiService = ApiUtils.getAPIService();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnGo = (Button) findViewById(R.id.btnRute);
        btnDaftar = (Button) findViewById(R.id.btnDaftar);

        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.bidang_keahlian, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                filterBySpesialis(i+1);
                btnGo.setVisibility(View.GONE);
                btnDaftar.setVisibility(View.GONE);
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //first
        LatLng samarinda = new LatLng(-0.495286, 117.143316);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(samarinda));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(13));

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }
            @Override
            public View getInfoContents(Marker marker) {
                //get Object
                PraktekDokter praktekDokter = (PraktekDokter) marker.getTag();

                // Getting view from the layout file info_window_layout
                View v = getLayoutInflater().inflate(R.layout.windowlayout, null);

                String spesialis = spinner.getSelectedItem().toString();

                TextView txt_nama_dokter = (TextView) v.findViewById(R.id.txt_nama_dokter);
                TextView txt_spesialis = (TextView) v.findViewById(R.id.txt_nama_spesialis);
                TextView txt_jadwal = (TextView) v.findViewById(R.id.txt_jadwal);
                TextView txt_jam = (TextView) v.findViewById(R.id.txt_jam);
                TextView txt_keterangan = (TextView) v.findViewById(R.id.txt_keterangan);


                if (marker.getTitle().equals("Current Position")){
                    txt_nama_dokter.setText(""+marker.getTitle());
                    txt_spesialis.setVisibility(View.GONE);
                    txt_jadwal.setVisibility(View.GONE);
                    txt_jam.setVisibility(View.GONE);
                    txt_keterangan.setVisibility(View.GONE);
                } else{
                    SpannableString nama_dokter = new SpannableString(""+praktekDokter.getNama_dokter());
                    nama_dokter.setSpan(new UnderlineSpan(), 0, nama_dokter.length(), 0);
                    txt_nama_dokter.setText(nama_dokter);
                    txt_spesialis.setText("Spesialis "+ spesialis);
                    txt_jadwal.setText("Praktek : " +praktekDokter.getJadwal());
                    txt_jam.setText("Jam : "+praktekDokter.getJam());
                    txt_keterangan.setText(praktekDokter.getKeterangan());
                }


                // Returning the view containing InfoWindow contents
                return v;
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                        PraktekDokter praktekDokter = (PraktekDokter) marker.getTag();

                        if (marker.getTitle().equals("Current Position")){
                            btnDaftar.setVisibility(View.GONE);
                            btnGo.setVisibility(View.GONE);
                        } else {
                            btnDaftar.setVisibility(View.VISIBLE);
                            btnDaftar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String spesialis = spinner.getSelectedItem().toString();
                                    Intent intent = new Intent(MainActivity.this, PendaftaranActivity.class);
                                    intent.putExtra("ID_PRAKTEK", praktekDokter.getId_praktek());
                                    intent.putExtra(PendaftaranActivity.EXTRA_NAMA_DOKTER, praktekDokter.getNama_dokter());
                                    intent.putExtra(PendaftaranActivity.EXTRA_TEMPAT_PRAKTEK, praktekDokter.getTempat_praktek());
                                    intent.putExtra(PendaftaranActivity.EXTRA_SPESIALIS, spesialis);
                                    startActivity(intent);
                                }
                            });

                            btnGo.setVisibility(View.VISIBLE);
                            btnGo.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    DrawRouteMaps.getInstance(MainActivity.this)
                                            .draw(mCurrLocationMarker.getPosition(), marker.getPosition(), mMap);

                                    LatLngBounds bounds = new LatLngBounds.Builder()
                                            .include(mCurrLocationMarker.getPosition())
                                            .include(marker.getPosition()).build();
                                    Point displaySize = new Point();
                                    getWindowManager().getDefaultDisplay().getSize(displaySize);
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, displaySize.x, 250, 30));
                                }
                            });
                        }


                LatLng from = new LatLng(mCurrLocationMarker.getPosition().latitude, mCurrLocationMarker.getPosition().longitude);
                LatLng to = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                Double distance = SphericalUtil.computeDistanceBetween(from,to);
                Double distanceinkm = distance/1000;
                DecimalFormat decimalFormat = new DecimalFormat("#.0");
                Toast.makeText(MainActivity.this, decimalFormat.format(distanceinkm) + " km", Toast.LENGTH_SHORT).show();
                marker.showInfoWindow();
                return true;
            }
        });

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        Log.d(TAG, "buildGoogleApiClient: true");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "GoogleApiClient is connected");
        startLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "lastLatitude: " +location.getLatitude());
        Log.d(TAG, "lastLongitude: " +location.getLongitude());

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.currentmarker));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13.0f));

        if(circle != null){
            circle.remove();
        }

        circle = mMap.addCircle(new CircleOptions()
                .center(mCurrLocationMarker.getPosition())
                .radius(4000)
                .strokeColor(Color.BLUE)
                .strokeWidth(2)
                .fillColor(Color.parseColor("#500084d3")));

        //just show marker in radius 1km
        for (Marker marker : markers) {
            if (SphericalUtil.computeDistanceBetween(latLng, marker.getPosition()) < 4000) {
                marker.setVisible(true);
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnenctionSuspended: " +i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: " +connectionResult.getErrorMessage());
    }


    public void checkGpsService() {
        LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showGPSDisabledAlertToUser();
        }
    }

    public void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Please active gps setting to continue.")
                .setCancelable(false)
                .setPositiveButton("Open Setting",
                        (dialog, id) -> {
                            Intent callGPSSettingIntent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            this.startActivity(callGPSSettingIntent);
                        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    protected void startLocationUpdates() {
        Log.d(TAG, "startLocationUpdates: true");
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            subscription.add(
                    RxPermissions.get(this)
                            .observe(REQUEST_TYPE_PERMISSION)
                            .subscribe(granted -> {
                                if (granted) {
                                    requestLocation();
                                } else {
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                                    alertDialogBuilder.setMessage("Permission no granted please give permission to continue.")
                                            .setCancelable(false)
                                            .setPositiveButton("OK",
                                                    (dialog, id) -> startLocationUpdates());
                                    AlertDialog alert = alertDialogBuilder.create();
                                    alert.show();
                                }
                            })
            );
            return;
        } else {
            requestLocation();
        }
    }

    private void requestLocation() {

        /*FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        mLastLocation = FusedLocationApi.getLastLocation(mGoogleApiClient);

        Log.d(TAG, "lastLatitude: " + mLastLocation.getLatitude());
        Log.d(TAG, "lastLongitude: " + mLastLocation.getLongitude()); */

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation == null){
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        else{
            //Place current location marker
            /*LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            mCurrLocationMarker = mMap.addMarker(markerOptions);
            Log.d(TAG, "marker added");
            //move map camera
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13.0f));*/

            FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

            Log.d(TAG, "lastLatitude: " + mLastLocation.getLatitude());
            Log.d(TAG, "lastLongitude: " + mLastLocation.getLongitude());
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
        subscription.unsubscribe();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mGoogleApiClient.connect();
    }

    private void getAllData(){
        // Execute the call asynchronously. Get a positive or negative callback.
        mApiService.getSemuaPraktek().enqueue(new Callback<List<PraktekDokter>>() {
            @Override
            public void onResponse(Call<List<PraktekDokter>> call, Response<List<PraktekDokter>> response) {
                // The network call was a success and we got a response
                int jumlah = response.body().size();

                Log.d(TAG, "response nya :" +response.body().toString());

                for(int i = 0; i < jumlah; i++){
                    PraktekDokter praktekDokter = new PraktekDokter(
                            response.body().get(i).getId_praktek(),
                            response.body().get(i).getNama_dokter(),
                            response.body().get(i).getId_spesialis(),
                            response.body().get(i).getTempat_praktek(),
                            response.body().get(i).getJadwal(),
                            response.body().get(i).getJam(),
                            response.body().get(i).getKeterangan(),
                            response.body().get(i).getLatitude(),
                            response.body().get(i).getLongitude()
                    );
                    datapraktek.add(praktekDokter);


                    Log.d(TAG, "data praktek 1 :" +datapraktek.get(i).getNama_dokter());
                }

                for(int i = 0; i < datapraktek.size(); i++){
                    mMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder))
                            .title(datapraktek.get(i).getNama_dokter())
                            .position(new LatLng(datapraktek.get(i).getLatitude(), datapraktek.get(i).getLongitude()))
                    );
                }

            }

            @Override
            public void onFailure(Call<List<PraktekDokter>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error Retrive Data from Server !!!", Toast.LENGTH_SHORT).show();
                Log.v(TAG, "onFailure: " +t);
            }
        });
    }

    private void filterBySpesialis(int id_spesialis){

        Log.d(TAG, "id spesialis: " +id_spesialis);

        mApiService.getBySpesialis(id_spesialis).enqueue(new Callback<List<PraktekDokter>>() {
            @Override
            public void onResponse(Call<List<PraktekDokter>> call, Response<List<PraktekDokter>> response) {
                int jumlah = response.body().size();

                if(datapraktek != null){
                    datapraktek.clear();
                }

                for(int i = 0; i < jumlah; i++){
                    PraktekDokter praktekDokter = new PraktekDokter(
                            response.body().get(i).getId_praktek(),
                            response.body().get(i).getNama_dokter(),
                            response.body().get(i).getId_spesialis(),
                            response.body().get(i).getTempat_praktek(),
                            response.body().get(i).getJadwal(),
                            response.body().get(i).getJam(),
                            response.body().get(i).getKeterangan(),
                            response.body().get(i).getLatitude(),
                            response.body().get(i).getLongitude()
                    );
                    datapraktek.add(praktekDokter);

                    Log.d(TAG, "data praktek  :" +datapraktek.get(i).getNama_dokter());
                }


                if (markers != null){
                    markers.clear();
                    mMap.clear();
                }

                for(int i = 0; i < datapraktek.size(); i++){
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(new LatLng(datapraktek.get(i).getLatitude(), datapraktek.get(i).getLongitude()));
                    markerOptions.title(datapraktek.get(i).getNama_dokter());
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.praktek));
                    markerOptions.visible(false);
                    PraktekDokter praktekDokter = datapraktek.get(i);
                    mPrakterMarker = mMap.addMarker(markerOptions);
                    mPrakterMarker.setTag(praktekDokter);
                    markers.add(mPrakterMarker);
                }

            }

            @Override
            public void onFailure(Call<List<PraktekDokter>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error Retrive Data from Server !!!", Toast.LENGTH_SHORT).show();
                Log.v(TAG, "onFailure: " +t);
            }
        });
    }

}
