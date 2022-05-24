package com.example.a112;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.example.a112.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private float Latitude;
    private float Longitude;
    private String country;
    private String city;
    private String postalCode;
    private String addressLine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.location_map);
        mapFragment.getMapAsync(this);

        // check if GPS enabled
        GPSTracker gpsTracker = new GPSTracker(this);

        Places.initialize(getApplicationContext(), "AIzaSyB4awvszSPnzIce89NF_tFNkkOFED5Nd6w");

        if (gpsTracker.getIsGPSTrackingEnabled())
        {
            Toast.makeText(this, "GPS tracker available", Toast.LENGTH_LONG).show();

            Latitude = (float) gpsTracker.latitude;
            Longitude = (float) gpsTracker.longitude;
            country = String.valueOf(gpsTracker.getCountryName(this));
            city = String.valueOf(gpsTracker.getLocality(this));
            postalCode = String.valueOf(gpsTracker.getPostalCode(this));
            addressLine = String.valueOf(gpsTracker.getAddressLine(this));
        }
        else
        {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gpsTracker.showSettingsAlert();
            Latitude = (float) 45.8150;
            Longitude = (float) 15.9819;
            country = "Croatia";
            city = "Zagreb";
            postalCode = "10000";
            addressLine = "Vukovarska";
            Toast.makeText(this, "GPS tracker unavailable", Toast.LENGTH_LONG).show();
        }
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



        LatLng cur = new LatLng(Latitude, Longitude);
        mMap.addMarker(new MarkerOptions().position(cur).title("Current Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(cur));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12.0f));



    }

    public void findHospital(View view) {
        GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces();
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(new LatLng(Latitude, Longitude)).title("Current Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Latitude,Longitude)));

        String url = getUrl(Latitude, Longitude, "hospital");
        Object transferData[] = {mMap, url};

        getNearbyPlaces.execute(transferData);
        Toast.makeText(this, "Searching for hospitals", Toast.LENGTH_SHORT).show();

    }

    private String getUrl(double la, double lo, String nearbyPlace)
    {
        StringBuilder googleUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googleUrl.append("location=" + la + "," + lo);
        googleUrl.append("&radius=" + 10000);
        googleUrl.append("&keyword=" + nearbyPlace);
        googleUrl.append("&key=" + "AIzaSyB4awvszSPnzIce89NF_tFNkkOFED5Nd6w");

        Log.d("MapsActivity", "url = " + googleUrl.toString());

        return googleUrl.toString();
    }

    public void findPolice(View view) {
        GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces();
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(new LatLng(Latitude, Longitude)).title("Current Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Latitude,Longitude)));

        String url = getUrl(Latitude, Longitude, "police station");
        Object transferData[] = {mMap, url};

        getNearbyPlaces.execute(transferData);
        Toast.makeText(this, "Searching for police station", Toast.LENGTH_SHORT).show();

    }

}