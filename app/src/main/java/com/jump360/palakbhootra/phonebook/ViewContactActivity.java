package com.jump360.palakbhootra.phonebook;

import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewContactActivity extends AppCompatActivity implements OnMapReadyCallback{

    Contacts c;
    private TextView name,phone;
    private MapView mapView;
    private DatabaseReference db;
    Contacts contacts;
    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact);
        c = (Contacts) getIntent().getSerializableExtra("contact");


        name = findViewById(R.id.textViewName);
        phone = findViewById(R.id.textViewPhone);
        //mapView = findViewById(R.id.map);

        Toast.makeText(getApplicationContext(),""+c.getLongi(),Toast.LENGTH_SHORT).show();
        name.setText(c.getName());
        phone.setText(c.getPhone());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        if(c.getLat()!=0||c.getLongi()!=0) {

        }else {
        }
    }

    private void loadMap(){
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng location = new LatLng(c.getLat(), c.getLongi());
        googleMap.addMarker(new MarkerOptions().position(location)
                .title(c.getName()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
    }
}
