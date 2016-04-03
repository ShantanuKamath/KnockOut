package com.shantanukamath.knockout;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class SuggestFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    LatLng marker;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    String postal;
    TextView hName;
    LatLng latLng;
    Marker mCurrLocation;



    private OnFragmentInteractionListener mListener;

    public static SuggestFragment newInstance(String param1, String param2) {
        SuggestFragment fragment = new SuggestFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    public SuggestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_suggest, container, false);

        hName = (TextView) v.findViewById(R.id.suggested);
        String activityName = randomSchedule();
        hName.setText("Visit the " + activityName);
        String coordinates =  getLatLong(activityName);
        int comma = coordinates.indexOf(",");
        double Lat = Double.parseDouble(coordinates.substring(0, comma));
        double Long = Double.parseDouble(coordinates.substring(comma + 1, coordinates.length()));
        marker = new LatLng(Long, Lat);
        Log.v("coordinates", Lat + "" + Long + "");


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.maptwo);
        mapFragment.getMapAsync(this);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        Button reload = (Button) v.findViewById(R.id.reloadBtn);
        reload.setOnClickListener(new View.OnClickListener() {
                                      public void onClick(View v) {
                                          Fragment fragment = null;
                                          Class fragmentClass = SuggestFragment.class;
                                          try {
                                              fragment = (Fragment) fragmentClass.newInstance();
                                          } catch (Exception e) {
                                              e.printStackTrace();
                                          }
                                          FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                          fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

                                      }
                                  }

        );
        return v;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addMarker(new MarkerOptions().position(marker).title(hName.getText().toString().substring(9,hName.getText().toString().length()))).showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, (float) 12.0));
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(true);

        buildGoogleApiClient();

        mGoogleApiClient.connect();
    }

    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

        Geocoder geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
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
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return addr;
    }

    String randomSchedule() {
        String schedule= "";
        Random r = new Random();
        for (int i = 0; i < 15; i++) {
            try {
                JSONObject hotelData = new JSONObject(loadJSONFromAsset("monuments.json"));
                JSONObject obj = hotelData.getJSONObject("Document");
                JSONArray hotelDetails = obj.getJSONArray("Placemark");
                int j = Math.abs(r.nextInt()) % hotelDetails.length();
                JSONObject j_obj = hotelDetails.getJSONObject(j);
                String name = j_obj.getString("name");
                schedule = name;
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
        return schedule;

    }

    public String loadJSONFromAsset(String filename) {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
    public String getLatLong(String name) {
        try {
            JSONObject hotelData = new JSONObject(loadJSONFromAsset("monuments.json"));
            JSONObject obj = hotelData.getJSONObject("Document");
            JSONArray hotelDetails = obj.getJSONArray("Placemark");
            for (int i = 0; i < hotelDetails.length(); i++) {
                JSONObject j_obj = hotelDetails.getJSONObject(i);
                if (j_obj.getString("name").equals(name)) {
                    JSONObject point = j_obj.getJSONObject("Point");
                    Log.v("BAK",point.getString("coordinates"));
                    return point.getString("coordinates");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return null;
    }

}
