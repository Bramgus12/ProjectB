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
import android.widget.ProgressBar;
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
    private ProgressBar progressBar;

    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private TextInputLayout nameLayout;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Register_Button();
    }

    public void Register_Button(){
        Button registerButton = findViewById(R.id.RegisterRegisterButton);
        editTextPassword = findViewById(R.id.registerPassword);
        editTextPasswordConfirmation = findViewById(R.id.registerConfirmPassword);
        editTextEmail = findViewById(R.id.registerEmail);
        editTextName = findViewById(R.id.registerName);
        progressBar = findViewById(R.id.progress_bar_reg);

        mAuth = FirebaseAuth.getInstance();

        nameLayout = findViewById(R.id.registerNameLayout);
        emailLayout = findViewById(R.id.registerEmailLayout);
        passwordLayout = findViewById(R.id.registerPasswordLayout);

        registerButton.setOnClickListener( // the back-end of the register button
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (editTextName == null || editTextName.getText().toString().trim().isEmpty()){
                            nameLayout.setError("Name is required.");
                            editTextName.requestFocus();
                            return; // check if name is empty or not
                        }
                        if (editTextEmail  == null || editTextEmail.getText().toString().trim().isEmpty()){
                            emailLayout.setError("Email is required");
                            return; // check if email is empty or not
                        }

                        if (editTextPassword == null || editTextPassword.getText().toString().trim().isEmpty()){
                            passwordLayout.setError("Password is required.");
                            editTextPassword.requestFocus();
                            return; // check if password is empty or not
                        }
                        if (!editTextPassword.getText().toString().trim().matches(editTextPasswordConfirmation.getText().toString().trim())){
                            passwordLayout.setError("Passwords must match");
                            editTextPasswordConfirmation.requestFocus();
                            return; // check if password is the same as password confirmation
                        }
                        if (!Patterns.EMAIL_ADDRESS.matcher(editTextEmail.getText().toString().trim()).matches()){
                            editTextEmail.setError("Enter a valid email.");
                            editTextEmail.requestFocus();
                            return; // check if email is a real email address
                        }
                        if (editTextPassword.getText().toString().trim().length() < 6){
                            passwordLayout.setError("Your password should be at least 6 characters long.");
                            editTextPassword.requestFocus(); // check if email is 6 characters or longer
                            return;
                        }

                        progressBar.setVisibility(View.VISIBLE);

                        mAuth.createUserWithEmailAndPassword(editTextEmail.getText().toString().trim(), editTextPassword.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) { // send email and password to authentication database
                                    Toast.makeText(Register.this, "Register Successful.", Toast.LENGTH_SHORT).show(); // show message that register was complete
                                    setUser(); // send name, UID, Role and email to realtime database
                                    emptyInputEditText(); //empty the input fields

                                }

                                progressBar.setVisibility(View.INVISIBLE);




                            }
                        });

                    }
                }
        );

    }
    private void emptyInputEditText() { // empty the fields
        editTextName.setText(null);
        editTextEmail.setText(null);
        editTextPassword.setText(null);
        editTextPasswordConfirmation.setText(null);
    }
    private void setUser(){
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser(); // get the info about the currentuser
        String email = currentFirebaseUser.getEmail(); // get email from the firebase
        String name = editTextName.getText().toString().trim(); // get name from input field
        String uid = currentFirebaseUser.getUid(); // get UID from firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance(); // initialise the database
        DatabaseReference mRootRef = database.getReference(); // reference to the database
        DatabaseReference emailRef = mRootRef.child("users").child(uid).child("Email"); // get the ref to the email
        DatabaseReference nameRef = mRootRef.child("users").child(uid).child("Name"); // get the ref for the Name
        DatabaseReference roleRef = mRootRef.child("users").child(uid).child("Role"); // get the ref for the Role
        emailRef.setValue(email); // set the email in the database
        nameRef.setValue(name); // set the name in the database
        roleRef.setValue("User"); // set the role in the database (standard = "User")
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            sendToMain();
        }
    }

    private void sendToMain() {
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
