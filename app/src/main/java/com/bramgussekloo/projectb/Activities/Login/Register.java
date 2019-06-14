package com.bramgussekloo.projectb.Activities.Login;

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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bramgussekloo.projectb.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;


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

    private static final String TAG = "Register";


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        Register_Button();
    }

    public void Register_Button(){
        Button registerButton = findViewById(R.id.RegisterRegisterButton);
        Button backToSignIn = findViewById(R.id.back_to_sign_in);
        editTextPassword = findViewById(R.id.registerPassword);
        editTextPasswordConfirmation = findViewById(R.id.registerConfirmPassword);
        editTextEmail = findViewById(R.id.registerEmail);
        editTextName = findViewById(R.id.registerName);
        progressBar = findViewById(R.id.progress_bar_reg);

        mAuth = FirebaseAuth.getInstance();

        nameLayout = findViewById(R.id.registerNameLayout);
        emailLayout = findViewById(R.id.registerEmailLayout);
        passwordLayout = findViewById(R.id.registerPasswordLayout);

        backToSignIn.setOnClickListener(// button to go to the signIn page
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent signinintent = new Intent(getBaseContext(), LoginActivity.class);
                        startActivity(signinintent);
                    }
                }
        );

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
                                    initFCM(); // send name, UID, Role and email to realtime database
                                    sendToMain();

                                }
                                if (!task.isSuccessful()){
                                    Log.e("SignUpToFirebase", task.getException().toString());
                                    Toast.makeText(Register.this, "Something went wrong. Try another email or another password that is stronger than this one.", Toast.LENGTH_SHORT).show();
                                    emptyInputEditText();
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
    private void setUserInDatabase(String token){

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser(); // get the info about the currentuser
        String email = editTextEmail.getText().toString().trim().replace(".", "").replace("@", "").toLowerCase(); // get email from the firebase
        String name = editTextName.getText().toString().trim(); // get name from input field
        String uid = currentFirebaseUser.getUid(); // get UID from firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance(); // initialise the database
        DatabaseReference mRootRef = database.getReference(); // reference to the database
        DatabaseReference uidRef = mRootRef.child("users").child(email).child("uid"); // get the ref to the email
        DatabaseReference nameRef = mRootRef.child("users").child(email).child("Name"); // get the ref for the Name
        DatabaseReference roleRef = mRootRef.child("users").child(email).child("Role"); // get the ref for the Role
        DatabaseReference emailRef = mRootRef.child("users").child(email).child("Email");
        emailRef.setValue(editTextEmail.getText().toString().trim().toLowerCase());
        uidRef.setValue(uid); // set the email in the database
        nameRef.setValue(name); // set the name in the database
        roleRef.setValue("User"); // set the role in the database (standard = "User")
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("users").child(email).child("messaging_token").setValue(token);
        currentFirebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    Toast.makeText(Register.this, "Check your email for a verification.", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(Register.this, "verification email is not sent. Make sure you have the right email put in.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        emptyInputEditText();
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

    private void initFCM(){
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String deviceToken = instanceIdResult.getToken();
                setUserInDatabase(deviceToken);
            }
        });
    }
}
