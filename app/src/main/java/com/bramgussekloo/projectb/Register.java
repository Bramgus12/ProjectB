package com.bramgussekloo.projectb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends AppCompatActivity {
    private Button registerButton;
    private EditText password;
    private EditText passwordConfirmation;
    private EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Register_Button();
    }

    public void Register_Button(){
        registerButton = findViewById(R.id.RegisterRegisterButton);
        password = findViewById(R.id.Password);
        passwordConfirmation = findViewById(R.id.registerConfirmPassword);
        email = findViewById(R.id.registerEmail);

        registerButton.setOnClickListener( // the back-end of the register button
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (email.getText().toString().contains("@hr.nl")) {
                            Toast.makeText(Register.this, "It contains @hr.nl", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(Register.this, "It does NOT contain @hr.nl", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

    }

}
