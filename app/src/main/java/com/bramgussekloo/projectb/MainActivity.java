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
                            Intent UserIntent = new Intent(getBaseContext(),User.class);
                            startActivity(UserIntent);
                        }
                        else if(username.getText().toString().equals("TestAdmin") && password.getText().toString().equals("TestAdmin")){
                            Toast.makeText(MainActivity.this,"Username and password are correct.", Toast.LENGTH_SHORT).show();
                            Intent AdminIntent = new Intent(getBaseContext(),Admin.class);
                            startActivity(AdminIntent);
                        }
                        else if(username.getText().toString().equals("TestBeheerder") && password.getText().toString().equals("TestBeheerder")){
                            Toast.makeText(MainActivity.this, "Username and password are correct.", Toast.LENGTH_SHORT).show();
                            Intent ModeratorIntent = new Intent(getBaseContext(),Beheerder.class);
                            startActivity(ModeratorIntent);
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Username or password is not correct. Try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

    }
}