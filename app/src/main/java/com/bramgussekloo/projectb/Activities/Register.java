package com.bramgussekloo.projectb.Activities;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Register extends AppCompatActivity {
    private TextInputEditText editTextPassword;
    private TextInputEditText editTextPasswordConfirmation;
    private TextInputEditText editTextEmail;
    private TextInputEditText editTextName;

    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private TextInputLayout confirmPasswordLayout;
    private TextInputLayout nameLayout;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Register_Button();
    }

    public void Register_Button(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRootRef = database.getReference();
        Button registerButton = findViewById(R.id.RegisterRegisterButton);
        editTextPassword = findViewById(R.id.registerPassword);
        editTextPasswordConfirmation = findViewById(R.id.registerConfirmPassword);
        editTextEmail = findViewById(R.id.registerEmail);
        editTextName = findViewById(R.id.registerName);
        mAuth = FirebaseAuth.getInstance();

        nameLayout = findViewById(R.id.registerNameLayout);
        emailLayout = findViewById(R.id.registerEmailLayout);
        passwordLayout = findViewById(R.id.registerPasswordLayout);
        confirmPasswordLayout = findViewById(R.id.registerConfirmPasswordLayout);

        registerButton.setOnClickListener( // the back-end of the register button
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (editTextName == null || editTextName.getText().toString().trim().isEmpty()){
                            editTextEmail.setError("Name is required.");
                            editTextName.requestFocus();
                            return;
                        }
                        if (editTextEmail  == null || editTextEmail.getText().toString().trim().isEmpty()){
                            emailLayout.setError("Email is required");
                            return;
                        }

                        if (editTextPassword == null || editTextPassword.getText().toString().trim().isEmpty()){
                            passwordLayout.setError("Password is required.");
                            editTextPassword.requestFocus();
                            return;
                        }
                        if (!editTextPassword.getText().toString().trim().matches(editTextPasswordConfirmation.getText().toString().trim())){
                            passwordLayout.setError("Passwords must match");
                            editTextPasswordConfirmation.requestFocus();
                            return;
                        }
                        if (!Patterns.EMAIL_ADDRESS.matcher(editTextEmail.getText().toString().trim()).matches()){
                            editTextEmail.setError("Enter a valid email.");
                            editTextEmail.requestFocus();
                            return;
                        }
                        if (editTextPassword.getText().toString().trim().length() < 6){
                            passwordLayout.setError("Your password should be at least 6 characters long.");
                            editTextPassword.requestFocus();
                            return;
                        }

                        mAuth.createUserWithEmailAndPassword(editTextEmail.getText().toString().trim(), editTextPassword.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Register.this, "Register Successful.", Toast.LENGTH_SHORT).show();
                                    setUser();
                                    emptyInputEditText();

                                }



                            }
                        });

                    }
                }
        );

    }
    private void emptyInputEditText() {
        editTextName.setText(null);
        editTextEmail.setText(null);
        editTextPassword.setText(null);
        editTextPasswordConfirmation.setText(null);
    }
    private void setUser(){
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String email = currentFirebaseUser.getEmail();
        String name = editTextName.getText().toString().trim();
        String uid = currentFirebaseUser.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRootRef = database.getReference();
        DatabaseReference emailRef = mRootRef.child("users").child(uid).child("Email");
        DatabaseReference nameRef = mRootRef.child("users").child(uid).child("Name");
        DatabaseReference roleRef = mRootRef.child("users").child(uid).child("Role");
        emailRef.setValue(email);
        nameRef.setValue(name);
        roleRef.setValue("user");
    }

}
