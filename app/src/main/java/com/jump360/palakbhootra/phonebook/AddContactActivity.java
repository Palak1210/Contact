package com.jump360.palakbhootra.phonebook;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddContactActivity extends AppCompatActivity {

    private EditText name,phone;
    private TextView latitude,longitude;
    private Button addLocation,addsave;
    Contacts c,contacts;
    private DatabaseReference db;
    int PLACE_PICKER_REQUEST = 1;
    String n,p,key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name = findViewById(R.id.editTextName);
        phone = findViewById(R.id.editTextPhone);
        latitude = findViewById(R.id.textViewLatitude);
        longitude = findViewById(R.id.textViewLongitude);
        addLocation = findViewById(R.id.btnAddLocation);
        addsave = findViewById(R.id.buttonAdd);

        c = (Contacts) getIntent().getSerializableExtra("contact");

        if (c != null) {
            setTitle("Edit Contact");
            name.setText(c.getName());
            phone.setText(c.getPhone());

            if(c.getLat()!=0){
                longitude.setText(c.getLongi()+"");
                latitude.setText(c.getLat()+"");
            }
        }else {
            setTitle("Add Contact");
        }

        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try{
                    Intent intent=builder.build(AddContactActivity.this);
                    startActivityForResult(intent,PLACE_PICKER_REQUEST);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        addsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                db = FirebaseDatabase.getInstance().getReference();

                n = name.getText().toString().trim();
                p = phone.getText().toString().trim();



                if(p.length()<10 || p.length()>10){
                    Snackbar.make(view, "Enter valid Phone Number", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    return;
                }

                if(getIntent().getExtras()!=null){
                    key = c.getKey();
                }else {
                    key = db.push().getKey();
                }

                if(longitude.getText().equals("-") && latitude.getText().equals("-")) {
                    contacts = new Contacts(n, p, key);
                }else{
                    double longi = Double.parseDouble(longitude.getText().toString().trim());
                    double lati = Double.parseDouble(latitude.getText().toString().trim());
                    contacts = new Contacts(n,p,key,longi,lati);
                }

                db.child(user.getUid()).child(key).setValue(contacts);
                name.setText("");
                phone.setText("");
                longitude.setText("");
                latitude.setText("");
                Toast.makeText(getApplicationContext(), "Info saved..!", Toast.LENGTH_LONG).show();
                return;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==PLACE_PICKER_REQUEST && resultCode==RESULT_OK){
            Place place=PlacePicker.getPlace(data,this);
            latitude.setText(place.getLatLng().latitude+"");
            longitude.setText(place.getLatLng().longitude+"");
        }
    }
}