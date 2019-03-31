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
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;

public class ResetPassword extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextInputEditText Email;
    private TextInputLayout EmailLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        mAuth = FirebaseAuth.getInstance();
        resetPassword();
    }
    private void resetPassword(){
        Email = findViewById(R.id.ResetPasswordEmail);
        Button resetPasswordButton = findViewById(R.id.ResetPasswordButton);
        EmailLayout = findViewById(R.id.ResetPasswordEmailLayout);

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Email.getText().toString().trim().isEmpty()){
                    EmailLayout.setError("Enter an email address.");
                    Email.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(Email.getText().toString().trim()).matches()){
                    EmailLayout.setError("Enter a valid email.");
                    Email.requestFocus();
                    return; // check if email is an email-address or not
                }
                mAuth.sendPasswordResetEmail(Email.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ResetPassword.this, "Email has been sent.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(ResetPassword.this, "Email has not been sent. Try registering first before asking for a new password.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }
}
