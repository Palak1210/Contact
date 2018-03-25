package com.jump360.palakbhootra.phonebook;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText text_email,text_password;
    private Button btn_register, btn_loginLink;
    private ProgressBar pb;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //if user is already signed in directly shows contact
        auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser()!= null){
            startActivity(new Intent(MainActivity.this,ContactsActivity.class));
        }

        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        //referencing
        btn_register = findViewById(R.id.btnRegister);
        btn_loginLink = findViewById(R.id.btnLinktoLogin);
        text_email = findViewById(R.id.editTextEmail);
        text_password = findViewById(R.id.editTextPassword);
        pb = findViewById(R.id.progressBar2);

        btn_loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,LogIn.class));
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = text_email.getText().toString().trim();
                String password = text_password.getText().toString().trim();

                if (TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                pb.setVisibility(View.VISIBLE);
                pb.setIndeterminate(true);
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                pb.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    startActivity(new Intent(MainActivity.this, LogIn.class));
                                    finish();
                                }
                            }
                        });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        pb.setVisibility(View.GONE);
    }
}