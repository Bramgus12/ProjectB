package com.bramgussekloo.projectb;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private TextInputEditText email;
    private TextInputEditText password;
    private Button login_button;
    private Button register_button;
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private Button recyclerview_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LoginButton();

    }


    public void LoginButton() {
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

    }
}


