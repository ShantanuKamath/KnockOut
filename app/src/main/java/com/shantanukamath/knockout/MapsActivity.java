package com.shantanukamath.knockout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, ConnectionCallbacks, OnConnectionFailedListener {


    private GoogleMap mMap;
    LatLng marker;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    String postal;
    TextView hName;
    LatLng latLng;
    Marker mCurrLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_detail);

        hName = (TextView) findViewById(R.id.HotelName);
        TextView addrHotel = (TextView) findViewById(R.id.AddrHotel);
        Intent i = getIntent();
        hName.setText(i.getSerializableExtra("name").toString());
        String coordinates = i.getSerializableExtra("coordinates").toString();
        postal = i.getSerializableExtra("postal").toString();
        addrHotel.setText(getAddressByPostalCode(postal));

        int comma = coordinates.indexOf(",");
        double Lat = Double.parseDouble(coordinates.substring(0, comma));
        double Long = Double.parseDouble(coordinates.substring(comma + 1, coordinates.length()));
        marker = new LatLng(Long, Lat);
        Log.v("coordinates", Lat + "" + Long + "");


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addMarker(new MarkerOptions().position(marker).title(hName.getText().toString())).showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, (float) 12.0));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(true);

        buildGoogleApiClient();

        mGoogleApiClient.connect();
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }


    public String getAddressByPostalCode(String val_pcode) {

        Geocoder geocoder = new Geocoder(this.getApplicationContext(), Locale.getDefault());
        String addr ="";
        try {

            List<Address> addresses1 = geocoder.getFromLocationName(val_pcode, 1);

            Address obj1 = addresses1.get(0);

            List<Address> addresses = geocoder.getFromLocation(obj1.getLatitude(), obj1.getLongitude(), 1);
            Address obj = addresses.get(0);
            if (obj.getSubThoroughfare()!=null)
                addr += obj.getSubThoroughfare() + " ";
            if (obj.getPremises()!=null )
                addr += obj.getPremises() + " ";
            if (obj.getThoroughfare()!=null)
                addr += obj.getThoroughfare() + " ";
                addr += val_pcode+".";


        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return addr;
    }

}
