package com.bramgussekloo.projectb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //http://hackpundit.com/simple-login-android-app/ Bron
    private EditText username;
    private EditText password;
    private Button login_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LoginButton();
    }

    public void LoginButton() {
        username = findViewById(R.id.Username);
        password = findViewById(R.id.Password);
        login_button = findViewById(R.id.LoginButton);
        login_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (username.getText().toString().equals("Test") && password.getText().toString().equals("Test")) {
                            Toast.makeText(MainActivity.this, "Username and password are correct.", Toast.LENGTH_SHORT).show();
                            Intent Userintent = new Intent(getBaseContext(),User.class);
                            startActivity(Userintent);
                        }
                        else if(username.getText().toString().equals("TestAdmin") && password.getText().toString().equals("TestAdmin")){
                            Toast.makeText(MainActivity.this,"Username and password are correct.", Toast.LENGTH_SHORT).show();
                            Intent Adminintent = new Intent(getBaseContext(),Admin.class);
                            startActivity(Adminintent);
                        }
                        else if(username.getText().toString().equals("TestBeheerder") && password.getText().toString().equals("TestBeheerder")){
                            Toast.makeText(MainActivity.this, "Username and password are correct.", Toast.LENGTH_SHORT).show();
                            Intent BeheerderIntent = new Intent(getBaseContext(),Beheerder.class);
                            startActivity(BeheerderIntent);
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Username or password is not correct. Try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

    }
}