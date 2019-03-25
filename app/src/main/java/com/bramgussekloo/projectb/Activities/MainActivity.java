package com.bramgussekloo.projectb.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bramgussekloo.projectb.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextInputEditText email;
    private TextInputEditText password;
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LoginButton();
        mAuth = FirebaseAuth.getInstance();

    }


    public void LoginButton() {
        email = findViewById(R.id.Email);
        password = findViewById(R.id.Password);
        emailLayout = findViewById(R.id.textInputLayoutEmail);
        passwordLayout = findViewById(R.id.textInputLayoutPassword);
        Button login_button = findViewById(R.id.LoginButton);
        Button register_button = findViewById(R.id.RegisterButton);
        Button recyclerview_button = findViewById(R.id.recyclerViewButton);

        register_button.setOnClickListener( // button to go to the register page
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent Registerintent = new Intent(getBaseContext(), Register.class);
                        startActivity(Registerintent);
                    }
                }
        );
        recyclerview_button.setOnClickListener( // button to go to recyclerview (temporarily)
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
            public void onClick(View v) { // button to login
                if (email == null || email.getText().toString().trim().isEmpty()){
                    emailLayout.setError("Email required.");
                    email.requestFocus();
                    return; // error if name is empty
                }
                if (password == null || password.getText().toString().trim().isEmpty()){
                    passwordLayout.setError("Password required.");
                    password.requestFocus();
                    return; // check if password is empty
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()){
                    email.setError("Enter a valid email.");
                    email.requestFocus();
                    return; // check if email is an email-address or not
                }
                mAuth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){ // sign in to the app
                            Toast.makeText(MainActivity.this, "Logged in as " + email.getText().toString().trim(), Toast.LENGTH_SHORT).show();
                            getUser();
                            emptyInputEditText();
                        } else {
                            Toast.makeText(MainActivity.this, "Log in failed", Toast.LENGTH_SHORT).show();
                            emptyInputEditText();
                        }
                    }
                });
            }
        });

    }
    private void getUser(){
        FirebaseDatabase database = FirebaseDatabase.getInstance(); // get the database
        final DatabaseReference mRootRef = database.getReference(); // ref to the database
        FirebaseUser CurrentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser(); // get info about the user that is trying to log in
        final String Uid = CurrentFirebaseUser.getUid(); // get the UID of the user
        mRootRef.child("users").child(Uid).child("Role").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String role = dataSnapshot.getValue(String.class); // get the value of the role
                if (role != null && role.equals("Admin")){
                    Intent intent = new Intent(getBaseContext(), Admin.class);
                    startActivity(intent); // check if the user is an admin
                } else if (role != null && role.equals("Beheerder")){
                    Intent intent = new Intent(getBaseContext(), Beheerder.class);
                    startActivity(intent); // check if the user is an beheerder
                } else {
                    Intent intent = new Intent(getBaseContext(), User.class);
                    startActivity(intent); // if no admin and no beheerder, its a user, so go to the user screen
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void emptyInputEditText() { // empty input fields
        email.setText(null);
        password.setText(null);
    }
}

