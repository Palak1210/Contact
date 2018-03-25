package com.jump360.palakbhootra.phonebook;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.card.OnActionClickListener;
import com.dexafree.materialList.card.action.TextViewAction;
import com.dexafree.materialList.listeners.RecyclerItemClickListener;
import com.dexafree.materialList.view.MaterialListView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference db;
    private MaterialListView materialListView;
    Contacts c,editContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ContactsActivity.this,AddContactActivity.class));
            }
        });

        materialListView = findViewById(R.id.material_listview);
        TextView tv = new TextView(getApplicationContext());
        tv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tv.setText("Nothing here!");
        materialListView.setEmptyView(tv);
        final String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db = FirebaseDatabase.getInstance().getReference().child(user);
        db.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                materialListView.getAdapter().clearAll();
                final ArrayList<Contacts> contacts = new ArrayList<>();
                for(DataSnapshot contactSnap:dataSnapshot.getChildren()){
                    final String key = contactSnap.getKey();
                    Contacts c = contactSnap.getValue(Contacts.class);
                    contacts.add(c);
                    /*Loop through the updated tree and update local copy*/
                    final Card card = new Card.Builder(getApplicationContext())
                            .setTag(c.getPhone())
                            .withProvider(new CardProvider())
                            .setLayout(R.layout.contact_card)
                            .setDescription(c.getPhone())
                            .setTitle(c.getName())
                            .addAction(R.id.right_text_button, new TextViewAction(getApplicationContext())
                                    .setText("Delete")
                                    .setTextResourceColor(R.color.colorAccent)
                                    .setListener(new OnActionClickListener() {
                                        @Override
                                        public void onActionClicked(View view, Card card) {
                                            db.child(editContact.getKey()).removeValue();
                                        }
                                    }))
                            .addAction(R.id.left_text_button, new TextViewAction(getApplicationContext())
                                    .setText("Edit")
                                    .setTextResourceColor(R.color.colorAccent)
                                    .setListener(new OnActionClickListener() {
                                        @Override
                                        public void onActionClicked(View view, Card card) {
                                            Toast.makeText(getApplicationContext(),"Edit pressed..!",Toast.LENGTH_LONG).show();
                                            Intent i = new Intent(ContactsActivity.this,AddContactActivity.class);
                                            i.putExtra("contact",editContact);
                                            startActivity(i);
                                        }
                                    }))
                            .addAction(R.id.view_button, new TextViewAction(getApplicationContext())
                                    .setText("View")
                                    .setTextResourceColor(R.color.colorAccent)
                                    .setListener(new OnActionClickListener() {
                                        @Override
                                        public void onActionClicked(View view, Card card) {
                                            Intent i = new Intent(ContactsActivity.this,ViewContactActivity.class);
                                            i.putExtra("contact",editContact);
                                            startActivity(i);
                                        }
                                    }))
                            .endConfig()
                            .build();
                    materialListView.getAdapter().add(card);
                }

                materialListView.scrollToPosition(0);

                /*Keep track of selected item for editing*/
                materialListView.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(@NonNull Card card, int position) {
                        Log.d("Aww", contacts.get(position).toString());
                        editContact = contacts.get(position);
                    }

                    @Override
                    public void onItemLongClick(@NonNull Card card, int position) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contacts, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(ContactsActivity.this,LogIn.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStart() {
        super.onStart();
    }


}
