package com.bramgussekloo.projectb;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bramgussekloo.projectb.sql.DatabaseHelper;
import com.bramgussekloo.projectb.helpers.inputValidation;

public class MainActivity extends AppCompatActivity {
    //http://hackpundit.com/simple-login-android-app/ Bron
    private TextInputEditText email;
    private TextInputEditText password;
    private Button login_button;
    private Button register_button;
    private DatabaseHelper databaseHelper;
    private inputValidation inputValidation;
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private Button recyclerview_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LoginButton();
        initObjects();

    }


    private void initObjects() {
        databaseHelper = new DatabaseHelper(getBaseContext());
        inputValidation = new inputValidation(getBaseContext());
    }

    public void LoginButton() {
        email = findViewById(R.id.Email);
        password = findViewById(R.id.Password);
        emailLayout = findViewById(R.id.textInputLayoutEmail);
        passwordLayout = findViewById(R.id.textInputLayoutPassword);
        login_button = findViewById(R.id.LoginButton);
        register_button = findViewById(R.id.RegisterButton);
        recyclerview_button = findViewById(R.id.recyclerViewButton);
        login_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!inputValidation.isInputEditTextFilled(email, emailLayout, getString(R.string.error_email))) {
                            return;
                        }
                        if (!inputValidation.isInputEditTextEmail(email, emailLayout, getString(R.string.error_email))) {
                            return;
                        }
                        if (!inputValidation.isInputEditTextFilled(password, passwordLayout, getString(R.string.error_password))) {
                            return;
                        }
                        if (databaseHelper.checkUser(email.getText().toString().trim(), password.getText().toString().trim())) {
                            Intent userIntent = new Intent(getBaseContext(), User.class);
                            emptyInputEditText();
                            startActivity(userIntent);
                        } else {
                            Toast.makeText(getBaseContext(), "Something is not right. Try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
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

    private void emptyInputEditText() {
        email.setText(null);
        password.setText(null);
    }
}



