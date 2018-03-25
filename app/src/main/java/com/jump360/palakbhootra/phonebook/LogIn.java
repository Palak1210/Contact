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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import javax.security.auth.login.LoginException;

public class LogIn extends AppCompatActivity {

    private EditText text_logemail,text_logpassword;
    private Button btnLogin,btnLinkregister;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser()!= null){
            startActivity(new Intent(LogIn.this,ContactsActivity.class));
        }

        setContentView(R.layout.activity_log_in);

        auth = FirebaseAuth.getInstance();

        text_logemail = findViewById(R.id.editTextLogEmail);
        text_logpassword = findViewById(R.id.editTextLogPassword);
        btnLinkregister = findViewById(R.id.btnLinkToRegister);
        btnLogin = findViewById(R.id.btnLogin);

        btnLinkregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogIn.this,MainActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = text_logemail.getText().toString();
                final String password = text_logpassword.getText().toString();

                if (TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(LogIn.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Username or Password Incorrect", Toast.LENGTH_LONG).show();
                        }else{
                            startActivity(new Intent(LogIn.this,ContactsActivity.class));
                        }
                    }
                });
            }
        });
    }
}
