package com.mobilcontrol.dev.gpsmaster;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import com.android.volley.Request;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap = null;
    private Double longitude = null;
    private Double latitude = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        getLocation();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(longitude != null && latitude != null) {
            LatLng position = new LatLng(longitude, latitude);
            mMap.addMarker(new MarkerOptions().position(position).title("Device Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        }
    }

    @Override
    public void onBackPressed() {
        Intent ite = new Intent(MapsActivity.this.getApplicationContext(), MainActivity.class);
        startActivity(ite);
    }

    public void getLocation(){
        MyRequest locationRequest = new MyRequest(MapsActivity.this, Request.Method.GET,
                Config.baseAddress+"/mobil-control/api/gps-coordinate/"){

            @Override
            public void OnSuccess(int responseCode, String result) {
                JSONObject coordinate;

                try {
                    coordinate = new JSONObject(result);
                    longitude = coordinate.getDouble("longitude");
                    latitude = coordinate.getDouble("latitude");

                    if(mMap != null) {
                        LatLng position = new LatLng(longitude, latitude);
                        mMap.addMarker(new MarkerOptions().position(position).title("Device Location"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        locationRequest.processRequest();
    }
}
