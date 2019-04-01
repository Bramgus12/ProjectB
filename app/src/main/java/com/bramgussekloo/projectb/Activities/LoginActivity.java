package com.bramgussekloo.projectb.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
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

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextInputEditText email;
    private TextInputEditText password;
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();//hide action bar
        LoginButton();
        mAuth = FirebaseAuth.getInstance();

    }


    public void LoginButton() {
        progressBar = findViewById(R.id.progressbar);
        email = findViewById(R.id.Email);
        password = findViewById(R.id.Password);
        emailLayout = findViewById(R.id.textInputLayoutEmail);
        passwordLayout = findViewById(R.id.textInputLayoutPassword);
        Button login_button = findViewById(R.id.LoginButton);
        Button register_button = findViewById(R.id.RegisterButton);
        Button recyclerview_button = findViewById(R.id.recyclerViewButton);
        TextView reset_password = findViewById(R.id.rest_pass);

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
                    return; // error if email is empty
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

                progressBar.setVisibility(View.VISIBLE);

                mAuth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){ // sign in to the app
                            Toast.makeText(LoginActivity.this, "Logged in as " + email.getText().toString().trim(), Toast.LENGTH_SHORT).show();
                            sendToMain();
                            emptyInputEditText();
                        } else {
                            Toast.makeText(LoginActivity.this, "Log in failed", Toast.LENGTH_SHORT).show();
                            emptyInputEditText();
                        }

                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });
        reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ResetPassword.class);
                startActivity(intent);
            }
        });

    }

    private void emptyInputEditText() { // empty input fields
        email.setText(null);
        password.setText(null);
    }

    @Override
    protected void onStart() { // will automatically run in background
        super.onStart();
        
        FirebaseUser currentUser = mAuth.getCurrentUser(); // get current user
        
        if(currentUser != null){
            sendToMain(); // sends user to mainactivity
        }
    }

    private void sendToMain() {
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainIntent);
        finish(); // ensures user can't go back
    }
}

