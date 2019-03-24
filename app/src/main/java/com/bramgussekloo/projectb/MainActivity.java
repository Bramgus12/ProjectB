package com.bramgussekloo.projectb;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bramgussekloo.projectb.helpers.Userhelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextInputEditText email;
    private TextInputEditText password;
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private Button login_button;
    private Button register_button;
    private Button recyclerview_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LoginButton();
        mAuth = FirebaseAuth.getInstance();

    }


    public void LoginButton() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference mRootRef = database.getReference();
        email = findViewById(R.id.Email);
        password = findViewById(R.id.Password);
        emailLayout = findViewById(R.id.textInputLayoutEmail);
        passwordLayout = findViewById(R.id.textInputLayoutPassword);
        login_button = findViewById(R.id.LoginButton);
        register_button = findViewById(R.id.RegisterButton);
        recyclerview_button = findViewById(R.id.recyclerViewButton);

        register_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent Registerintent = new Intent(getBaseContext(), Register.class);
                        startActivity(Registerintent);
                    }
                }
        );
        recyclerview_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getBaseContext(), ProductActivity.class);
                        startActivity(intent);
                    }
                }
        );
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email == null || email.getText().toString().trim().isEmpty()){
                    emailLayout.setError("Email required.");
                    email.requestFocus();
                    return;
                }
                if (password == null || password.getText().toString().trim().isEmpty()){
                    passwordLayout.setError("Password required.");
                    password.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()){
                    email.setError("Enter a valid email.");
                    email.requestFocus();
                    return;
                }
                mAuth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = currentFirebaseUser.getUid();
                            Toast.makeText(MainActivity.this, "Logged in as " + email.getText().toString().trim(), Toast.LENGTH_SHORT).show();
                            getUser();
                        } else {
                            Toast.makeText(MainActivity.this, "Log in failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
    private void getUser(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference mRootRef = database.getReference();
        FirebaseUser CurrentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final String Uid = CurrentFirebaseUser.getUid();
        mRootRef.child("users").child(Uid).child("Role").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String role = dataSnapshot.getValue(String.class);
                if (role != null && role.equals("Admin")){
                    Intent intent = new Intent(getBaseContext(), Admin.class);
                    startActivity(intent);
                } else if (role != null && role.equals("Beheerder")){
                    Intent intent = new Intent(getBaseContext(), Beheerder.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getBaseContext(), User.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void UpdateUI(DataSnapshot dataSnapshot){

    }
}

