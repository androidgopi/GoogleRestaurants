package com.android.googlerestaurants;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    RecyclerView recyclerView;
    ImageAdapter imageAdapter;
    APIInterface apiInterface;
    List<LocationsModel> locationsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        init();

    }

    public void init() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (isNetworkAvailable()) {
            getData();
        } else {
            Toast.makeText(getApplicationContext(), " Please Check internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    public void getData() {
        apiInterface = ApiClient.getClient().create(APIInterface.class);
        retrofit2.Call<LocationsModel> call = apiInterface.getLocations("church");
        call.enqueue(new Callback<LocationsModel>() {
            @Override
            public void onResponse(retrofit2.Call<LocationsModel> call, Response<LocationsModel> response) {
                Log.e("status code", " " + response.code());
                if (response.body() != null) {
                    Log.e("listSize", "" + response.body().getResponse().size());
                    locationsList = response.body().getResponse();
                    locList();
                    imageAdapter = new ImageAdapter(getApplicationContext(), locationsList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setAdapter(imageAdapter);

                }
            }

            @Override
            public void onFailure(retrofit2.Call<LocationsModel> call, Throwable t) {

                Log.e("Error meg", " " + t.getMessage());

            }
        });
    }

    public void locList() {

        for (int i = 0; i < 5; i++) {

            // Latitude & Longitude
            Double latitude = Double.valueOf(locationsList.get(i).getLatitude());
            Double longitude = Double.valueOf((locationsList.get(i).getLongitude()));
            MarkerOptions marker = new MarkerOptions();
            marker.position(new LatLng(latitude, longitude)).title(locationsList.get(i).getName());
            //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 5));
            mMap.addMarker(marker);
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {

                    Toast.makeText(getApplicationContext(), "Marker click" + marker.getTitle(), Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < locationsList.size(); i++) {
                        if (marker.getTitle().equalsIgnoreCase(locationsList.get(i).getName())) {
                            recyclerView.scrollToPosition(i);
                            break;
                        }
                    }
                    return false;
                }
            });
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(locationsList.get(0).getLatitude()),Double.parseDouble(locationsList.get(0).getLongitude())), 10));
           // mMap.animateCamera( CameraUpdateFactory.zoomTo( 17.0f ) );

        }

    }

    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
