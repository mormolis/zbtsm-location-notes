package com.example.georgioslamprakis.zboutsam.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.georgioslamprakis.zboutsam.R;
import com.example.georgioslamprakis.zboutsam.database.entities.Note;
import com.example.georgioslamprakis.zboutsam.database.entities.helpers.ZbtsmLocation;
import com.example.georgioslamprakis.zboutsam.helpers.AccessDB;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final float MAP_ZOOM = 15f;

    private GoogleMap mMap;
    private Bundle b;
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        b = getIntent().getExtras();
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

        int value = -1;
        if (b != null) {
            value = b.getInt("id");
        }
        if (value!=-1){
            note = AccessDB.returnNoteByID(value);
            ZbtsmLocation location = note.getZbtsmLocation();
            LatLng latLng = new LatLng(location.getLat(),location.getLng());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, MAP_ZOOM));
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(note.getTitle() + " | " + note.getSummary());
            mMap.addMarker(markerOptions);
        }
    }

    private void openInGoogleMaps(ZbtsmLocation zbtsmLocation){
        String uri = String.format(Locale.ENGLISH, "geo:%f,%f", zbtsmLocation.getLat(), zbtsmLocation.getLng());
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }
}
