package com.bramgussekloo.projectb.Activities.Login;

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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ResetEmail extends AppCompatActivity {
    private TextInputEditText email;
    private TextInputEditText password;
    private TextInputEditText passwordConfirmation;
    private TextInputEditText newEmail;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private TextInputLayout passwordConfirmationLayout;
    private TextInputLayout newEmailLayout;
    private FirebaseUser user;
    private String UID;
    private DatabaseReference mref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_email);
        Buttons();
    }


    private void Buttons() {
        Button ResetEmailButton = findViewById(R.id.REResetEmailButton);
        email = findViewById(R.id.REEmailEditText);
        password = findViewById(R.id.REPasswordEditText);
        passwordConfirmation = findViewById(R.id.REConfirmPasswordEditText);
        newEmail = findViewById(R.id.RENewEmailEditText);
        user = mAuth.getCurrentUser();
        UID = user.getEmail().replace(".", "").replace("@", "");
        mref = FirebaseDatabase.getInstance().getReference();


        emailLayout = findViewById(R.id.REEmailLayout);
        passwordLayout = findViewById(R.id.REPasswordLayout);
        passwordConfirmationLayout = findViewById(R.id.REConfirmPasswordLayout);
        newEmailLayout = findViewById(R.id.RENewEmailLayout);
        ResetEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email == null || email.getText().toString().trim().isEmpty()) {
                    emailLayout.setError("Email is empty");
                    email.requestFocus();
                    return;
                }
                if (password == null || password.getText().toString().trim().isEmpty()) {
                    passwordLayout.setError("Password is empty");
                    password.requestFocus();
                    return;
                }
                if (password.getText().toString().trim().equals(passwordConfirmation)) {
                    passwordConfirmationLayout.setError("Passwords don't match");
                    passwordConfirmation.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()) {
                    emailLayout.setError("Enter a valid email.");
                    email.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(newEmail.getText().toString().trim()).matches()){
                    newEmailLayout.setError("Enter a valid email");
                    newEmail.requestFocus();
                    return;
                }
                FirebaseUser user = mAuth.getInstance().getCurrentUser();
                AuthCredential credential = EmailAuthProvider.getCredential(email.getText().toString().trim(), password.getText().toString().trim());
                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                user.updateEmail(newEmail.getText().toString().trim())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    mref.child("users").child(UID).child("Email").setValue(newEmail.getText().toString().trim());
                                                    Toast.makeText(ResetEmail.this, "Email has been reset", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        });

            }
        });
    }
}
